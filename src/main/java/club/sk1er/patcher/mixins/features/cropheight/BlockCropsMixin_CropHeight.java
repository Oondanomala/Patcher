package club.sk1er.patcher.mixins.features.cropheight;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.CropUtilities;
import club.sk1er.patcher.mixins.accessors.BlockAccessor;
import me.oondanomala.assential.MinecraftUtil;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockCrops.class)
public abstract class BlockCropsMixin_CropHeight extends BlockBush {
    //#if MC==1.8.9
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        if (PatcherConfig.futureHitBoxes && (MinecraftUtil.isHypixel() || Minecraft.getMinecraft().isIntegratedServerRunning())) {
            CropUtilities.updateCropsMaxY(worldIn, pos, this);
        } else {
            ((BlockAccessor) this).setMaxY(0.25F);
        }
    }
    //#endif
}
