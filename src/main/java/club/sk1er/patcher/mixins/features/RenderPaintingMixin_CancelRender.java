package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.entity.item.EntityPainting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPainting.class)
public abstract class RenderPaintingMixin_CancelRender {
    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityPainting;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void patcher$cancelPaintingRender(EntityPainting entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (PatcherConfig.disablePaintings) ci.cancel();
    }
}
