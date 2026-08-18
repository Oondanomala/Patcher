package club.sk1er.patcher.tweaker;

import org.objectweb.asm.ClassWriter;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

public class PatcherClassWriter extends ClassWriter {
    public PatcherClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return ClassInfo.getCommonSuperClass(type1, type2).getName();
    }
}
