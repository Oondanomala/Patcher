package club.sk1er.patcher.mixins.performance.forge;

import net.minecraftforge.fml.common.FMLModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(FMLModContainer.class)
public class FMLModContainerMixin_SkipBlamingTransformer {
    @Redirect(method = "constructMod", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/asm/transformers/BlamingTransformer;addClasses(Ljava/lang/String;Ljava/util/Set;)V"))
    private void patcher$skipBlamingTransformer(String cls, Set<String> modId) {
        // Useless, we know we are on Java 8
    }
}
