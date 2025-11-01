package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiTextField.class)
public class GuiTextField_FixCursorAlignment {
    // TODO: Use MixinExtras @ModifyReturnValue
    @Redirect(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I", ordinal = 0))
    private int patcher$fixCursorAlignment(FontRenderer instance, String text, float x, float y, int color) {
        int n = instance.drawStringWithShadow(text, x, y, color);
        return PatcherConfig.disableShadowedText || PatcherConfig.alternateTextShadow ? n + 1 : n;
    }
}
