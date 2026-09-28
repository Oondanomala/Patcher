package club.sk1er.patcher.mixins.performance;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chunk.class)
public class ChunkMixin_Optimization {
    @Shadow @Final
    private ExtendedBlockStorage[] storageArrays;

    /**
     * @author LlamaLad7
     * @reason Optimization
     */
    @Overwrite
    public IBlockState getBlockState(BlockPos pos) {
        final int y = pos.getY();
        if (y >= 0 && y >> 4 < storageArrays.length) {
            final ExtendedBlockStorage storage = storageArrays[y >> 4];
            if (storage != null) {
                return storage.get(pos.getX() & 15, y & 15, pos.getZ() & 15);
            }
        }
        return Blocks.air.getDefaultState();
    }
}
