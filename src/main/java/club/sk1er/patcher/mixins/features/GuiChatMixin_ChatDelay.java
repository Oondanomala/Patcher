package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.hooks.GuiNewChatHook;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class GuiChatMixin_ChatDelay {
    @Inject(method = "mouseClicked", at = @At(value = "RETURN", ordinal = 0))
    private void patcher$chatDelayMouseClicked(CallbackInfo ci) {
        GuiNewChatHook.mouseClicked();
    }
}
