package club.sk1er.patcher.mixins.performance.forge;

//#if MC==1.8.9
import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.FontRendererHook;
import com.google.common.base.CharMatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//#endif
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FMLClientHandler.class)
@SuppressWarnings("UnstableApiUsage")
public class FMLClientHandlerMixin_Optimization {
    //#if MC==1.8.9
    @Unique
    private static final CharMatcher patcher$DISALLOWED_CHAR_MATCHER = CharMatcher.anyOf(FontRendererHook.characterDictionary).negate();

    /**
     * @author LlamaLad7
     * @reason Performance improvement
     */
    @Overwrite(remap = false)
    public String stripSpecialChars(String var1) {
        return patcher$DISALLOWED_CHAR_MATCHER.removeFrom(StringUtils.stripControlCodes(var1));
    }

    @Redirect(method = "addModAsResource", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/LanguageRegistry;loadLanguagesFor(Lnet/minecraftforge/fml/common/ModContainer;Lnet/minecraftforge/fml/relauncher/Side;)V"), remap = false)
    private void patcher$avoidLanguageLoading(LanguageRegistry languageRegistry, ModContainer container, Side side) {
        // No-op
    }

    // TODO: MixinExtras @WrapWithCondition
    @Redirect(method = "finishMinecraftLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V"))
    private void patcher$skipSecondResourceReload(Minecraft instance) {
        if (!PatcherConfig.skipSecondResourceLoad) {
            instance.refreshResources();
        }
    }
    //#endif
}
