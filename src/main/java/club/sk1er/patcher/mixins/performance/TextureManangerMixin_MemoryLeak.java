package club.sk1er.patcher.mixins.performance;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TextureManager.class)
public class TextureManangerMixin_MemoryLeak {
    //#if MC==1.8.9
    @Shadow @Final
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;

    @Inject(method = "deleteTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;deleteTexture(I)V"))
    private void patcher$removeEntry(ResourceLocation textureLocation, CallbackInfo ci) {
        mapTextureObjects.remove(textureLocation);
    }
    //#endif
}
