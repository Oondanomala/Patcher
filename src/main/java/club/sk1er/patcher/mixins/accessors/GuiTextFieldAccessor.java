package club.sk1er.patcher.mixins.accessors;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiTextField.class)
public interface GuiTextFieldAccessor {
    @Accessor
    int getLineScrollOffset();

    @Accessor
    int getCursorCounter();

    @Accessor
    FontRenderer getFontRendererInstance();

    @Invoker
    void invokeDrawCursorVertical(int i, int j, int k, int l);
}
