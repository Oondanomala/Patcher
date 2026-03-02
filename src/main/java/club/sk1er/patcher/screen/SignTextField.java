package club.sk1er.patcher.screen;

import club.sk1er.patcher.mixins.accessors.GuiTextFieldAccessor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

public class SignTextField extends GuiTextField {
    public SignTextField(int buttonID, FontRenderer fontRenderer, int x, int y, int width, int height) {
        super(buttonID, fontRenderer, x, y, width, height);
    }

    /**
     * Modified method that does not draw text shadows and centers the text.
     */
    @Override
    public void drawTextBox() {
        GuiTextFieldAccessor thisAccessor = (GuiTextFieldAccessor) this;
        FontRenderer fontRenderer = thisAccessor.getFontRendererInstance();
        int lineScrollOffset = thisAccessor.getLineScrollOffset();
        String text = fontRenderer.trimStringToWidth(getText().substring(lineScrollOffset), getWidth());
        GlStateManager.pushMatrix();
        //noinspection IntegerDivisionInFloatingPointContext
        GlStateManager.translate(-fontRenderer.getStringWidth(text) / 2, 0, 0);

        int j = getCursorPosition() - lineScrollOffset;
        int k = getSelectionEnd() - lineScrollOffset;
        boolean bl = j >= 0 && j <= text.length();
        boolean bl2 = isFocused() && thisAccessor.getCursorCounter() / 6 % 2 == 0 && bl;
        int n = this.xPosition;
        if (k > text.length()) {
            k = text.length();
        }
        if (!text.isEmpty()) {
            String string2 = bl ? text.substring(0, j) : text;
            n = fontRenderer.drawString(string2, n, this.yPosition, 0) + 1;
        }
        boolean bl3 = getCursorPosition() < getText().length() || getText().length() >= getMaxStringLength();
        int o = n;
        if (!bl) {
            o = j > 0 ? this.xPosition + width : this.xPosition;
        } else if (bl3) {
            --o;
            --n;
        }
        if (!text.isEmpty() && bl && j < text.length()) {
            fontRenderer.drawString(text.substring(j), n, this.yPosition, 0);
        }
        if (bl2) {
            if (bl3) {
                Gui.drawRect(o, this.yPosition - 1, o + 1, this.yPosition + 1 + fontRenderer.FONT_HEIGHT, 0xFFD0D0D0);
            } else {
                fontRenderer.drawString("_", o, this.yPosition, 0);
            }
        }
        if (k != j) {
            int p = this.xPosition + fontRenderer.getStringWidth(text.substring(0, k));
            thisAccessor.invokeDrawCursorVertical(o, this.yPosition - 1, p - 1, this.yPosition + 1 + fontRenderer.FONT_HEIGHT);
        }
        GlStateManager.popMatrix();
    }
}

