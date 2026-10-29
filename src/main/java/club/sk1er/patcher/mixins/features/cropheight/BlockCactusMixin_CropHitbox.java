package club.sk1er.patcher.mixins.features.cropheight;

import club.sk1er.patcher.config.PatcherConfig;
import me.oondanomala.assential.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockCactus.class)
public abstract class BlockCactusMixin_CropHitbox extends Block {
    public BlockCactusMixin_CropHitbox(Material materialIn) {
        super(materialIn);
    }

    //#if MC==1.8.9
    // TODO: These never change, there's no point in setting them every time
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        if (PatcherConfig.futureHitBoxes && (MinecraftUtil.isHypixel() || Minecraft.getMinecraft().isIntegratedServerRunning())) {
            setBlockBounds(0.0625f, 0, 0.0625f, 0.9375f, 1, 0.9375f);
        } else {
            setBlockBounds(0, 0, 0, 1, 1, 1);
        }
    }
    //#endif
}
