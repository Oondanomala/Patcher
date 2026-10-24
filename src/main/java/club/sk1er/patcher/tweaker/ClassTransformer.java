package club.sk1er.patcher.tweaker;

import club.sk1er.patcher.asm.external.forge.loader.ASMModParserTransformer;
import club.sk1er.patcher.asm.external.forge.loader.ModClassLoaderTransformer;
import club.sk1er.patcher.asm.external.lwjgl.KeyboardTransformer;
import club.sk1er.patcher.asm.external.lwjgl.LibraryLWJGLOpenALTransformer;
import club.sk1er.patcher.asm.external.lwjgl.WindowsDisplayTransformer;
import club.sk1er.patcher.asm.external.lwjgl.WindowsKeycodesTransformer;
import club.sk1er.patcher.asm.external.mods.optifine.witherfix.EntityWitherTransformer;
import club.sk1er.patcher.asm.network.NetHandlerPlayClientTransformer;
import club.sk1er.patcher.asm.network.NetHandlerPlayServerTransformer;
import club.sk1er.patcher.asm.network.packet.S0EPacketSpawnObjectTransformer;
import club.sk1er.patcher.asm.network.packet.S34PacketMapsTransformer;
import club.sk1er.patcher.asm.render.particle.EffectRendererTransformer;
import club.sk1er.patcher.asm.render.screen.GuiChatTransformer;
import club.sk1er.patcher.asm.render.screen.GuiNewChatTransformer;
import club.sk1er.patcher.asm.render.screen.GuiPlayerTabOverlayTransformer;
import club.sk1er.patcher.asm.render.screen.InventoryEffectRendererTransformer;
import club.sk1er.patcher.asm.render.world.RenderGlobalTransformer;
import club.sk1er.patcher.asm.render.world.VisGraphTransformer;
import club.sk1er.patcher.asm.render.world.entity.LayerCustomHeadTransformer;
import club.sk1er.patcher.asm.render.world.entity.RenderFireballTransformer;
import club.sk1er.patcher.asm.render.world.entity.RenderWitherTransformer;
import club.sk1er.patcher.asm.render.world.entity.RenderXPOrbTransformer;
import club.sk1er.patcher.asm.world.entity.data.nbt.NBTTagCompoundTransformer;
import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class ClassTransformer implements IClassTransformer {
    public static final boolean OUTPUT_BYTECODE = Boolean.getBoolean("patcher.debugBytecode");
    private final Logger logger = LogManager.getLogger("Patcher - Class Transformer");
    private final Multimap<String, PatcherTransformer> transformerMap = ArrayListMultimap.create();

    public ClassTransformer() {
        MixinEnvironment.getCurrentEnvironment().addTransformerExclusion(getClass().getName());
        ModCompatManager.detectMods(this.getClass());

        //#if MC==1.8.9
        registerTransformer(new GuiNewChatTransformer());
        registerTransformer(new S0EPacketSpawnObjectTransformer());
        registerTransformer(new RenderXPOrbTransformer());
        registerTransformer(new RenderFireballTransformer());
        //#endif
        registerTransformer(new RenderGlobalTransformer());
        registerTransformer(new GuiPlayerTabOverlayTransformer());
        registerTransformer(new NetHandlerPlayClientTransformer());
        registerTransformer(new LayerCustomHeadTransformer());
        registerTransformer(new NBTTagCompoundTransformer());
        registerTransformer(new GuiChatTransformer());
        registerTransformer(new VisGraphTransformer());
        registerTransformer(new EffectRendererTransformer());
        registerTransformer(new NetHandlerPlayServerTransformer());
        registerTransformer(new EntityWitherTransformer());
        registerTransformer(new S34PacketMapsTransformer());
        registerTransformer(new RenderWitherTransformer());
        registerTransformer(new LibraryLWJGLOpenALTransformer());
        if (isDevelopment()) registerTransformer(new InventoryEffectRendererTransformer());

        // Forge classes
        //#if MC==1.8.9
        registerTransformer(new ModClassLoaderTransformer());
        registerTransformer(new ASMModParserTransformer());
        //#endif
        //registerTransformer(new JarDiscovererTransformer());

        // LWJGL
        registerTransformer(new WindowsDisplayTransformer());
        registerTransformer(new WindowsKeycodesTransformer());
        registerTransformer(new KeyboardTransformer());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static byte[] createTransformer(String transformedName, byte[] bytes, Multimap<String, PatcherTransformer> transformerMap, Logger logger) {
        if (bytes == null) return null;

        Collection<PatcherTransformer> transformers = transformerMap.get(transformedName);
        if (transformers.isEmpty()) return bytes;

        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        for (PatcherTransformer transformer : transformers) {
            transformer.transform(classNode, transformedName);
        }

        PatcherClassWriter classWriter = new PatcherClassWriter(PatcherClassWriter.COMPUTE_FRAMES);

        try {
            classNode.accept(classWriter);
        } catch (Throwable e) {
            logger.error("Exception when transforming {} : {}", transformedName, e.getClass().getSimpleName(), e);
        }

        if (OUTPUT_BYTECODE) {
            File bytecodeDirectory = new File("bytecode");
            if (!bytecodeDirectory.exists()) bytecodeDirectory.mkdirs();

            int lastIndex = transformedName.lastIndexOf('.');
            if (lastIndex != -1) {
                transformedName = transformedName.substring(lastIndex + 1) + ".class";
            }

            try {
                File bytecodeOutput = new File(bytecodeDirectory, transformedName);
                if (!bytecodeOutput.exists()) bytecodeOutput.createNewFile();

                try (FileOutputStream os = new FileOutputStream(bytecodeOutput)) {
                    os.write(classWriter.toByteArray());
                } catch (IOException e) {
                    logger.error("Failed to create bytecode output for {}.", transformedName, e);
                }
            } catch (Exception ignored) {
            }
        }

        return classWriter.toByteArray();
    }

    private void registerTransformer(PatcherTransformer transformer) {
        for (String cls : transformer.getClassName()) {
            transformerMap.put(cls, transformer);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        return createTransformer(transformedName, bytes, transformerMap, logger);
    }

    public static boolean isDevelopment() {
        return Launch.blackboard.get("fml.deobfuscatedEnvironment") == Boolean.TRUE;
    }
}
