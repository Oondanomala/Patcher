package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiChat.class)
public class GuiChatMixin_ShiftChat {
    @Shadow
    protected GuiTextField inputField;

    @Redirect(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", ordinal = 1))
    public void patcher$shiftChat(Minecraft mc, GuiScreen screen) {
        if (PatcherConfig.shiftChat && GuiScreen.isShiftKeyDown()) {
            inputField.setText("");
        } else {
            mc.displayGuiScreen(screen);
        }
    }
}
