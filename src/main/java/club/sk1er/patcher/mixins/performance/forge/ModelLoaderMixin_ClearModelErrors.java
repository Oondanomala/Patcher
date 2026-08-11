package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin_ClearModelErrors {
    //#if MC==1.8.9
    @Shadow @Final
    private Map<ResourceLocation, Exception> loadingExceptions;
    @Shadow @Final
    private Set<ModelResourceLocation> missingVariants;

    @Inject(method = "onPostBakeEvent", at = @At(value = "FIELD", target = "Lnet/minecraftforge/client/model/ModelLoader;isLoading:Z", opcode = Opcodes.PUTFIELD))
    private void patcher$clearModelErrors(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry, CallbackInfo ci) {
        loadingExceptions.clear();
        missingVariants.clear();
    }
    //#endif
}
