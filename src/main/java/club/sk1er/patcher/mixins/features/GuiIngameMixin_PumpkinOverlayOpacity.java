package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiIngame.class)
public class GuiIngameMixin_PumpkinOverlayOpacity {
    @ModifyArg(method = "renderPumpkinOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V", ordinal = 0), index = 3)
    private float patcher$modifyOverlayOpacity(float alpha) {
        return PatcherConfig.pumpkinOverlayOpacity;
    }
}
