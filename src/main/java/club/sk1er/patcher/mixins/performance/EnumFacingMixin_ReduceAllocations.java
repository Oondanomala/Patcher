package club.sk1er.patcher.mixins.performance;

import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnumFacing.class)
public class EnumFacingMixin_ReduceAllocations {
    @Shadow @Final
    public static EnumFacing[] VALUES;

    @Redirect(method = {"random", "getFacingFromVector", "getFacingFromAxis"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    private static EnumFacing[] patcher$avoidValuesAllocation() {
        return VALUES;
    }
}
