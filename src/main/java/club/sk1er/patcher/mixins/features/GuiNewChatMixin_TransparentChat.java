package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Can set priority back to default once MixinExtras comes back
@Mixin(value = GuiNewChat.class, priority = 1099)
public abstract class GuiNewChatMixin_TransparentChat extends Gui {

    @Shadow
    public abstract boolean getChatOpen();

    // TODO: Use MixinExtras @WrapWithCondition
    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void patcher$transparentChat(int left, int top, int right, int bottom, int color) {
        if (!PatcherConfig.transparentChat || PatcherConfig.transparentChatOnlyWhenClosed && getChatOpen()) {
            drawRect(left, top, right, bottom, color);
        }
    }
}
