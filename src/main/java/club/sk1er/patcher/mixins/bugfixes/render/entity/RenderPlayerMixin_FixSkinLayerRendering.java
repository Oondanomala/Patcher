package club.sk1er.patcher.mixins.bugfixes.render.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class RenderPlayerMixin_FixSkinLayerRendering {
    // No good way of using @ModifyConstant :/
    @Redirect(method = "setModelVisibilities", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelRenderer;showModel:Z", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void patcher$fixSpectatorSkinLayer(ModelRenderer instance, boolean value, AbstractClientPlayer player) {
        instance.showModel = player.isWearing(EnumPlayerModelParts.HAT);
    }

    //#if MC==1.8.9
    // TODO: use MixinExtras @WrapOperation
    @Inject(
        method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V")
    )
    private void patcher$fixSkinLayerTransparency(CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }
    @Inject(
        method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", shift = At.Shift.AFTER)
    )
    private void patcher$fixSkinLayerTransparency2(CallbackInfo ci) {
        GlStateManager.disableBlend();
    }

    @Inject(
        method = {"renderRightArm", "renderLeftArm"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", shift = At.Shift.AFTER)
    )
    private void patcher$fixFirstPersonSkinLayerTransparency(CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }
    @Inject(method = {"renderRightArm", "renderLeftArm"}, at = @At(value = "TAIL"))
    private void patcher$fixFirstPersonSkinLayerTransparency2(CallbackInfo ci) {
        GlStateManager.disableBlend();
    }
    //#endif
}
