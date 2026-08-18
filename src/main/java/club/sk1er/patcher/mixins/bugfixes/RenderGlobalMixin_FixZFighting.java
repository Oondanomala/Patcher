package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin_FixZFighting {
    //#if MC==1.8.9
    @ModifyConstant(method = "preRenderDamagedBlocks", constant = @Constant(floatValue = -3f, ordinal = 0))
    private float patcher$fixZFighting(float original) {
        return -1;
    }
    @ModifyConstant(method = "preRenderDamagedBlocks", constant = @Constant(floatValue = -3f, ordinal = 1))
    private float patcher$fixZFighting2(float original) {
        return -10;
    }
    //#endif
}
