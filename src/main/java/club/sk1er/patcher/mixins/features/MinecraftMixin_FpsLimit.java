package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = 900)
public class MinecraftMixin_FpsLimit {
    @Shadow
    public GameSettings gameSettings;

    /**
     * @author Oondanomala
     * @reason Lower FPS when not in view, custom FPS when in view, avoid hardcoding FPS in main menu
     */
    @Overwrite
    public int getLimitFramerate() {
        if (!Display.isVisible() && PatcherConfig.minimizedFPS) {
            return PatcherConfig.minimizedFPSAmount;
        } else if (!Display.isActive() && PatcherConfig.unfocusedFPS) {
            return PatcherConfig.unfocusedFPSAmount;
        } else if (PatcherConfig.customFpsLimit > 0) {
            return PatcherConfig.customFpsLimit;
        }
        return gameSettings.limitFramerate;
    }

    @Inject(method = "isFramerateLimitBelowMax", at = @At("HEAD"), cancellable = true)
    private void patcher$useCustomFrameLimit(CallbackInfoReturnable<Boolean> cir) {
        if (PatcherConfig.customFpsLimit > 0) {
            cir.setReturnValue(true);
        }
    }
}
