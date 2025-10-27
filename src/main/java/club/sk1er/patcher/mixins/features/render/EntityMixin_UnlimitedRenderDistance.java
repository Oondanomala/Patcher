package club.sk1er.patcher.mixins.features.render;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin_UnlimitedRenderDistance {
    @Inject(method = "isInRangeToRender3d", at = @At("HEAD"), cancellable = true)
    private void patcher$alwaysRender(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (PatcherConfig.unlimitedEntityRenderDistance) {
            cir.setReturnValue(true);
        }
    }
}
