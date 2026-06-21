package club.sk1er.patcher.mixins.features.playerfade;

import club.sk1er.patcher.util.world.render.entity.EntityRendering;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LayerCustomHead.class)
public class LayerCustomHeadMixin_PlayerFade {
    //#if MC==1.8.9
    @ModifyArg(method = "doRenderLayer", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"))
    private float patcher$modifyAlpha(float alpha) {
        return EntityRendering.shouldFadePlayer ? EntityRendering.playerFadeAlpha : alpha;
    }
    //#endif
}
