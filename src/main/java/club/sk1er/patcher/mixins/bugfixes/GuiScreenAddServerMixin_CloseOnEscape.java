package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenAddServer.class)
public class GuiScreenAddServerMixin_CloseOnEscape {
    @Shadow @Final
    private GuiScreen parentScreen;

    @Inject(method = "keyTyped", at = @At(value = "HEAD"), cancellable = true)
    private void patcher$closeOnEscape(char typedChar, int keyCode, CallbackInfo ci) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.parentScreen.confirmClicked(false, 0);
            ci.cancel();
        }
    }
}
