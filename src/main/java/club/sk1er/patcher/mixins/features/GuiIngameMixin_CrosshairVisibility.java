package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.hooks.CrosshairHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiIngame.class)
public class GuiIngameMixin_CrosshairVisibility {
    //#if MC==10809
    @Shadow
    @Final
    protected Minecraft mc;

    @Inject(method = "showCrosshair", at = @At("HEAD"), cancellable = true)
    private void patcher$checkStates(CallbackInfoReturnable<Boolean> cir) {
        if (!CrosshairHook.shouldRenderCrosshair(mc)) {
            cir.setReturnValue(false);
        }
    }
    //#endif
}
