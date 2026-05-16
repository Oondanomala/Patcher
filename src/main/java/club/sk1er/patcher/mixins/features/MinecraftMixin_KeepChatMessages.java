package club.sk1er.patcher.mixins.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin_KeepChatMessages {
    // this is so ugly : (
    @Redirect(method = "displayGuiScreen", at = @At(value = "INVOKE", target =
        //#if MC==1.8.9
        "Lnet/minecraft/client/gui/GuiNewChat;clearChatMessages()V"
        //#else
        //$$ "Lnet/minecraft/client/gui/GuiNewChat;clearChatMessages(Z)V"
        //#endif
    ))
    private void patcher$keepChatMessages(GuiNewChat instance
                                          //#if MC==1.12.2
                                          //$$ , boolean clearSentMessages
                                          //#endif
    ) {
        // No-op
    }
}
