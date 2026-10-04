package club.sk1er.patcher.mixins.bugfixes.modelfixes;

import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerHeldItem.class)
public class LayerHeldItemMixin_FixHeldItemRendering {
    //#if MC==1.8.9
    // TODO: @Redirect instanceof directly with Mixin 0.8
    @Redirect(method = "doRenderLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;fishEntity:Lnet/minecraft/entity/projectile/EntityFishHook;", opcode = Opcodes.GETFIELD))
    private EntityFishHook patcher$fixEnchantedFishingRodInThirdPerson(EntityPlayer instance) {
        return null;
    }

    @ModifyConstant(method = "doRenderLayer", constant = @Constant(floatValue = 0.625f))
    private float patcher$fixBabyHeldItem(float constant) {
        return 0.75f;
    }
    @Redirect(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 0))
    private void patcher$fixBabyHeldItem(float angle, float x, float y, float z) {
        // No-op
    }
    //#endif
}
