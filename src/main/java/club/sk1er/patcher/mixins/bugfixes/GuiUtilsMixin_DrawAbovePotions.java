package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiUtils.class)
public class GuiUtilsMixin_DrawAbovePotions {
    //#if MC==1.8.9
    @Redirect(method = "drawHoveringText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableDepth()V"))
    private static void patcher$fixDepthAndZLevel() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0f, -1f);
    }

    @Inject(method = "drawHoveringText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableRescaleNormal()V", shift = At.Shift.AFTER))
    private static void patcher$resetZLevel(CallbackInfo ci) {
        GlStateManager.popMatrix();
    }

    @Inject(method = "drawHoveringText", slice = @Slice(from = @At(value = "INVOKE:LAST", target = "Lnet/minecraftforge/fml/client/config/GuiUtils;drawGradientRect(IIIIIII)V")),
        at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/config/GuiUtils;drawGradientRect(IIIIIII)V", shift = At.Shift.AFTER))
    private static void patcher$fixDepth(CallbackInfo ci) {
        GlStateManager.disableDepth();
    }
    //#endif
}
