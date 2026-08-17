package club.sk1er.patcher.mixins.performance;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(VertexFormat.class)
public class VertexFormatMixin_CacheHashcode {
    //#if MC==1.8.9
    @Unique
    private int patcher$cachedHashcode;

    @Inject(method = "hashCode", at = @At("HEAD"), cancellable = true)
    private void patcher$cacheHashCode(CallbackInfoReturnable<Integer> cir) {
        if (patcher$cachedHashcode != 0) {
            cir.setReturnValue(patcher$cachedHashcode);
        }
    }
    @Inject(method = "hashCode", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void patcher$saveCachedHashCode(CallbackInfoReturnable<Integer> cir, int i) {
        patcher$cachedHashcode = i;
    }

    @Inject(method = "addElement", at = @At("TAIL"))
    private void patcher$resetCachedHashcode1(VertexFormatElement element, CallbackInfoReturnable<VertexFormat> cir) {
        patcher$cachedHashcode = 0;
    }

    @Inject(method = "clear", at = @At("TAIL"))
    private void patcher$resetCachedHashcode2(CallbackInfo ci) {
        patcher$cachedHashcode = 0;
    }
    //#endif
}
