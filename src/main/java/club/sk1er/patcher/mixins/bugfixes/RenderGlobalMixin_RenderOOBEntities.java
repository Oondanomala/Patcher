package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin_RenderOOBEntities {
    //#if MC==10809
    // Fixes entities outside the world (such as below y0 or above y255) not rendering at specific camera angles (MC-73139)
    @Redirect(method = "setupTerrain", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;boundingBox:Lnet/minecraft/util/AxisAlignedBB;", opcode = Opcodes.GETFIELD, ordinal = 0))
    private AxisAlignedBB patcher$fixOOBEntityRendering(RenderChunk instance, Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks; // TODO: Use MixinExtras @Local
        return instance.boundingBox.addCoord(0, MathHelper.floor_double(d4 + viewEntity.getEyeHeight()) > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, 0);
    }
    //#endif
}
