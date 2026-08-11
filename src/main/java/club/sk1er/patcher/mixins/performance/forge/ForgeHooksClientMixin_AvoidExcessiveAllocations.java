package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin_AvoidExcessiveAllocations {
    //#if MC==1.8.9
    @Redirect(method = "getSkyBlendColour", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockPos;getY()I"))
    private static int patcher$useCorrectMethod(BlockPos instance) {
        return instance.getZ();
    }
    //#endif
}
