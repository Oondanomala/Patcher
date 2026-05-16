package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.screen.SignTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEditSign.class)
public abstract class GuiEditSign_UseTextField extends GuiScreen {
    @Shadow
    //#if MC==1.12.2
    //$$ @org.spongepowered.asm.mixin.Final
    //#endif
    private TileEntitySign tileSign;
    @Shadow
    private int editLine;
    @Shadow
    private GuiButton doneBtn;
    @Shadow
    protected abstract void actionPerformed(GuiButton button);

    @Unique
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    @Unique
    private GuiTextField[] textFields;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void patcher$addTextFields(CallbackInfo ci) {
        int y = -20;
        textFields = new GuiTextField[]{
            new SignTextField(234, this.fontRendererObj, 0, y, 95, 20),
            new SignTextField(235, this.fontRendererObj, 0, y += 10, 95, 20),
            new SignTextField(236, this.fontRendererObj, 0, y += 10, 95, 20),
            new SignTextField(237, this.fontRendererObj, 0, y + 10, 95, 20)
        };
        textFields[editLine].setFocused(true);
        for (int i = 0; i < textFields.length; i++) {
            GuiTextField textField = textFields[i];
            textField.setMaxStringLength(Integer.MAX_VALUE);
            textField.setText(tileSign.signText[i].getUnformattedText());
            textField.setTextColor(0);
            textField.setEnableBackgroundDrawing(false);
            // Allow loading signs with longer text than allowed so they don't get accidentally modified
            textField.setValidator(input -> this.fontRendererObj.getStringWidth(input) <= 90);
            // Focus is handled manually
            textField.setCanLoseFocus(false);
        }
    }

    /**
     * @author Oondanomala
     * @reason Draw {@link GuiTextField}s and draw the background sign as a rect instead of a block
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, I18n.format("sign.edit"), width / 2, 40, 0xFFFFFF);
        boolean standing = tileSign.getBlockType() == Blocks.standing_sign;

        GlStateManager.pushMatrix();
        //noinspection IntegerDivisionInFloatingPointContext
        GlStateManager.translate(width / 2, 123.046875f, 0);
        GlStateManager.scale(-93.75f, -93.75f, 1);
        GlStateManager.rotate(180f, 0, 1, 0);
        if (!standing) GlStateManager.translate(0, -0.3125f, 0);

        // Draw the sign
        mc.getTextureManager().bindTexture(SIGN_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.041666668f, -0.041666668f, 1);
        final int UVWidth = 24;
        final int UVHeight = 12;
        drawModalRectWithCustomSizedTexture(
            -(UVWidth / 2), -14,
            2, 2,
            UVWidth, UVHeight,
            64, 32
        );
        if (standing) {
            final int postUVWidth = 2;
            final int postUVHeight = 14;
            drawModalRectWithCustomSizedTexture(
                -(postUVWidth / 2), -2,
                2, 16,
                postUVWidth, postUVHeight,
                64, 32
            );
        }
        GlStateManager.popMatrix();

        // Draw the text fields
        GlStateManager.translate(0, 0.33333334f, 0);
        GlStateManager.scale(0.010416667f, -0.010416667f, 1);
        for (GuiTextField textField : textFields) {
            textField.drawTextBox();
        }

        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Inject(method = "updateScreen", at = @At("TAIL"))
    private void patcher$updateTextFields(CallbackInfo ci) {
        for (GuiTextField textField : textFields) {
            textField.updateCursorCounter();
        }
    }

    /**
     * @author Oondanomala
     * @reason Use {@link GuiTextField} for text editing
     */
    @Overwrite
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_UP) {
            textFields[editLine].setFocused(false);
            textFields[editLine].setCursorPositionEnd();
            editLine = editLine - 1 & 3;
            textFields[editLine].setFocused(true);
        } else if (keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
            textFields[editLine].setFocused(false);
            textFields[editLine].setCursorPositionEnd();
            editLine = editLine + 1 & 3;
            textFields[editLine].setFocused(true);
        }

        GuiTextField textField = textFields[editLine];
        if (textField.textboxKeyTyped(typedChar, keyCode)) {
            tileSign.signText[editLine] = new ChatComponentText(textField.getText());
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            actionPerformed(doneBtn);
        }
    }
}
