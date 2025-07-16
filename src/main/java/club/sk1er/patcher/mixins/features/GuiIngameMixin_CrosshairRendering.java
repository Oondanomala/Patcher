package club.sk1er.patcher.mixins.features;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;

//#if MC==1.12.2
//$$ import club.sk1er.patcher.config.PatcherConfig;
//$$ import net.minecraft.client.renderer.GlStateManager;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$ import org.spongepowered.asm.mixin.injection.Slice;
//#endif

@Mixin(GuiIngame.class)
public class GuiIngameMixin_CrosshairRendering {
    //#if MC==1.12.2
    //$$ @Redirect(
    //$$     method = "renderAttackIndicator",
    //$$     at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;tryBlendFuncSeparate(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V", ordinal = 0)
    //$$ )
    //$$ private void patcher$handleCrosshairInvert(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor, GlStateManager.SourceFactor srcFactorAlpha, GlStateManager.DestFactor dstFactorAlpha) {
    //$$     if (!PatcherConfig.removeInvertFromCrosshair) {
    //$$         GlStateManager.tryBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
    //$$     }
    //$$ }
    //#endif
}
