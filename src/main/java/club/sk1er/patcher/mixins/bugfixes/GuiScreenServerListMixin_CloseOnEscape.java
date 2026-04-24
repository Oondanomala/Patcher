package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenServerList.class)
public class GuiScreenServerListMixin_CloseOnEscape {
    @Shadow @Final
    private GuiScreen field_146303_a;

    @Inject(method = "keyTyped", at = @At(value = "HEAD"), cancellable = true)
    private void patcher$closeOnEscape(char typedChar, int keyCode, CallbackInfo ci) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.field_146303_a.confirmClicked(false, 0);
            ci.cancel();
        }
    }
}
