package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.hooks.ModContainerFactoryHook;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModContainerFactory;
import net.minecraftforge.fml.common.discovery.ModCandidate;
import net.minecraftforge.fml.common.discovery.asm.ASMModParser;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.regex.Pattern;

@Mixin(ModContainerFactory.class)
public class ModContainerFactoryMixin_SkipBaseMods {
    //#if MC==1.8.9
    @Shadow @SuppressWarnings("unused")
    private static Pattern modClass = null;
    @Shadow
    public static Map<Type, Constructor<? extends ModContainer>> modTypes;

    /**
     * @author asbyth, Oondanomala
     * @reason Skip BaseMod detection
     */
    @Overwrite
    public ModContainer build(ASMModParser modParser, File modSource, ModCandidate container) {
        return ModContainerFactoryHook.build(modParser, container, modTypes);
    }
    //#endif
}
