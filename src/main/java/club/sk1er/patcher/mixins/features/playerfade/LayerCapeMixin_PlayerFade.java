package club.sk1er.patcher.mixins.features.playerfade;

import club.sk1er.patcher.util.world.render.entity.EntityRendering;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerCape.class)
public class LayerCapeMixin_PlayerFade {
    //#if MC==1.8.9
    // TODO: MixinExtras @WrapWithCondition
    @Redirect(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"))
    private void patcher$makeTransparent(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
        if (EntityRendering.shouldFadePlayer) {
            EntityRendering.setupPlayerFade();
        } else {
            GlStateManager.color(colorRed, colorGreen, colorBlue, colorAlpha);
        }
    }
    //#endif
}
