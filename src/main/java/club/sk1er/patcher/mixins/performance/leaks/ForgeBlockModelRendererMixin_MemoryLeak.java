package club.sk1er.patcher.mixins.performance.leaks;

import club.sk1er.patcher.ducks.VertexLighterFlatExt;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeBlockModelRenderer.class)
public class ForgeBlockModelRendererMixin_MemoryLeak {
    //#if MC==1.8.9
    @Inject(method = "render", at = @At("TAIL"))
    private static void patcher$cleanupBlockInfo(VertexLighterFlat lighter, IBlockAccess world, IBakedModel model, Block block, BlockPos pos, WorldRenderer wr, boolean checkSides, CallbackInfoReturnable<Boolean> cir) {
        ((VertexLighterFlatExt) lighter).patcher$resetBlockInfo();
    }
    //#endif
}
