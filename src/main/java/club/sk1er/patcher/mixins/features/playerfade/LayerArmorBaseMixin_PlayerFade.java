package club.sk1er.patcher.mixins.features.playerfade;

import club.sk1er.patcher.util.world.render.entity.EntityRendering;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class LayerArmorBaseMixin_PlayerFade {
    //#if MC==1.8.9
    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorModel(I)Lnet/minecraft/client/model/ModelBase;"))
    private void patcher$setupTransparency(CallbackInfo ci) {
        if (EntityRendering.shouldFadePlayer) {
            EntityRendering.setupPlayerFade();
        }
    }

    // FIXME: enchant glints will not render when the player has depthMask(false),
    //  because they use a depthFunc of GL_EQUAL to only draw where the armor is,
    //  but with depthMask disabled the armor will not draw on the depth buffer,
    //  making the check always fail.
    @ModifyArg(method = {"renderLayer", "renderGlint"}, index = 3,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"))
    private float patcher$modifyAlpha(float alpha) {
        return EntityRendering.shouldFadePlayer ? EntityRendering.playerFadeAlpha : alpha;
    }
    //#endif
}
