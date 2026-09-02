package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.ducks.BlockInfoExt;
import club.sk1er.patcher.hooks.BlockInfoHook;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockInfo.class)
public class BlockInfoMixin_CacheFlatLighting
    //#if MC==1.8.9
    implements BlockInfoExt
    //#endif
{
    //#if MC==1.8.9
    @Shadow
    private Block block;
    @Shadow
    private IBlockAccess world;
    @Shadow
    private BlockPos blockPos;
    @Shadow
    private int cachedTint;
    @Shadow
    private float shz;
    @Shadow
    private float shy;
    @Shadow
    private float shx;
    @Shadow
    private int cachedMultiplier;

    @Override
    public void patcher$updateFlatLighting() {
        BlockInfoHook.updateFlatLighting(block, world, blockPos);
    }

    @Override
    public void patcher$reset() {
        world = null;
        blockPos = null;
        cachedTint = -1;
        cachedMultiplier = -1;
        shx = 0;
        shy = 0;
        shz = 0;
    }
    //#endif
}
