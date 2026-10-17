package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntityEndPortal.class)
public class TileEntityEndPortal_FixCulling extends TileEntity {
    //#if MC==1.8.9
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = getPos();
        return new AxisAlignedBB(pos.getX(), pos.getY() + 0.7499, pos.getZ(), pos.getX() + 1, pos.getY() + 0.7501, pos.getZ() + 1);
    }
    //#endif
}
