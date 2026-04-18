package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.GuiButton;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiButton.class)
public class GuiButton_WhiteHoverText {
    @Shadow
    protected boolean hovered;

    @Redirect(method = "drawButton", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiButton;hovered:Z", opcode = Opcodes.GETFIELD, ordinal = 1))
    private boolean patcher$whiteHoverText(GuiButton instance) {
        return hovered && !PatcherConfig.whiteButtonText;
    }
}
