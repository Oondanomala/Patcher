package club.sk1er.patcher.tweaker.asm;

import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class AbstractResourcePackTransformer implements PatcherTransformer {

    /**
     * The class name that's being transformed
     *
     * @return the class name
     */
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.resources.AbstractResourcePack"};
    }

    /**
     * Perform any asm in order to transform code
     *
     * @param classNode the transformed class node
     * @param name      the transformed class name
     */
    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("getPackImage") || methodName.equals("func_110586_a")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), downscaleImageSize());
                break;
            }
        }
    }

    private InsnList downscaleImageSize() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, getPatcherConfigClass(), "packImageOptimization", "Z"));
        LabelNode l1 = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new LdcInsnNode("pack.png"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/resources/AbstractResourcePack", "func_110591_a", // getInputStreamByName
                "(Ljava/lang/String;)Ljava/io/InputStream;", false));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/renderer/texture/TextureUtil", "func_177053_a", // readBufferedImage
                "(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;", false));
        list.add(new VarInsnNode(Opcodes.ASTORE, 1));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        LabelNode l4 = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNONNULL, l4));
        list.add(new InsnNode(Opcodes.ACONST_NULL));
        list.add(new InsnNode(Opcodes.ARETURN));
        list.add(l4);
        list.add(new TypeInsnNode(Opcodes.NEW, "java/awt/image/BufferedImage"));
        list.add(new InsnNode(Opcodes.DUP));
        list.add(new IntInsnNode(Opcodes.BIPUSH, 64));
        list.add(new IntInsnNode(Opcodes.BIPUSH, 64));
        list.add(new InsnNode(Opcodes.ICONST_2));
        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/awt/image/BufferedImage", "<init>", "(III)V", false));
        list.add(new VarInsnNode(Opcodes.ASTORE, 2));
        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/awt/image/BufferedImage", "getGraphics", "()Ljava/awt/Graphics;", false));
        list.add(new VarInsnNode(Opcodes.ASTORE, 3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new IntInsnNode(Opcodes.BIPUSH, 64));
        list.add(new IntInsnNode(Opcodes.BIPUSH, 64));
        list.add(new InsnNode(Opcodes.ACONST_NULL));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/awt/Graphics", "drawImage", "(Ljava/awt/Image;IIIILjava/awt/image/ImageObserver;)Z", false));
        list.add(new InsnNode(Opcodes.POP));
        list.add(new VarInsnNode(Opcodes.ALOAD, 3));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/awt/Graphics", "dispose", "()V", false));
        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
        list.add(new InsnNode(Opcodes.ARETURN));
        list.add(l1);
        return list;
    }
}
