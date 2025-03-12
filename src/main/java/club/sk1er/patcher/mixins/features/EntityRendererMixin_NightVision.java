package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_NightVision {
    @Inject(method = "getNightVisionBrightness", at = @At("HEAD"), cancellable = true)
    public void patcher$disableNightVision(EntityLivingBase entityLivingBaseIn, float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (PatcherConfig.disableNightVision) {
            cir.setReturnValue(0f);
        }
    }
}
