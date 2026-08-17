package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiChat.class)
public class GuiChatMixin_TransparentChatInput {
    // TODO: Use MixinExtras @WrapWithCondition
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;drawRect(IIIII)V"))
    public void patcher$transparentChatInputField(int left, int top, int right, int bottom, int color) {
        if (!PatcherConfig.transparentChatInputField) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}
