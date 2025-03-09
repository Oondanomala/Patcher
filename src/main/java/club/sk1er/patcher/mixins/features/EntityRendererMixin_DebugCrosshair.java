package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.hooks.CrosshairHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin_DebugCrosshair {
    //#if MC==10809
    @Shadow
    private Minecraft mc;
    @Shadow
    public abstract void setupOverlayRendering();

    @Unique
    private boolean patcher$shouldDraw;

    @Inject(method = "renderWorldDirections", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getRenderViewEntity()Lnet/minecraft/entity/Entity;"), cancellable = true)
    private void patcher$cancelOldDebugCrosshair(float partialTicks, CallbackInfo ci) {
        patcher$shouldDraw = true;
        ci.cancel();
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V"))
    private void patcher$drawDebugCrosshair(float partialTicks, long nanoTime, CallbackInfo ci) {
        if (patcher$shouldDraw && CrosshairHook.shouldRenderCrosshair(mc)) {
            setupOverlayRendering();
            CrosshairHook.renderDebugCrosshair(mc, partialTicks);
            patcher$shouldDraw = false;
        }
    }
    //#endif
}
