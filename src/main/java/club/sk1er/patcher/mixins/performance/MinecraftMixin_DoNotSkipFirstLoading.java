package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Minecraft.class)
public class MinecraftMixin_DoNotSkipFirstLoading {
    //#if MC==1.8.9
    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureMap;<init>(Ljava/lang/String;Z)V"))
    private boolean patcher$doNotSkipFirstLoading(boolean original) {
        if (PatcherConfig.skipSecondResourceLoad) {
            return false;
        }
        return original;
    }
    //#endif
}
