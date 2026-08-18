package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin_FixBorderWithBlindness {
    @Inject(method = "renderWorldBorder", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;getClosestDistance(Lnet/minecraft/entity/Entity;)D"))
    private void patcher$fixWorldBorderRendering(CallbackInfo ci) {
        GlStateManager.disableFog();
    }

    @Inject(method = "renderWorldBorder", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V", ordinal = 1, shift = At.Shift.AFTER))
    private void patcher$fixWorldBorderRendering2(CallbackInfo ci) {
        GlStateManager.enableFog();
    }
}
