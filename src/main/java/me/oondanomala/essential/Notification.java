package me.oondanomala.essential;

import gg.essential.universal.UMouse;
import gg.essential.universal.UResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

class Notification extends Gui {
    // Behold, my constants!
    private static final int WIDTH = 175;
    private static final int RIGHT_MARGIN = 2;
    private static final int BOTTOM_MARGIN = 5;
    private static final int LEFT_PADDING = 8;
    private static final int RIGHT_PADDING = 7;
    private static final int TOP_PADDING = 8;
    private static final int BOTTOM_PADDING = 7;
    private static final int TEXT_SPACE = WIDTH - (LEFT_PADDING + RIGHT_PADDING);

    private static final int BACKGROUND_COLOR = 0xFF181818;
    private static final int BORDER_COLOR = 0xFF303030;
    private static final int BORDER_COLOR_HOVER = 0xFF757575;
    private static final int PROGRESS_BAR_COLOR = 0xFF474747;
    private static final int PROGRESS_BAR_COLOR_HOVER = 0xFF5C5C5C;
    private static final int TITLE_COLOR = 0xFFE5E5E5;
    private static final int TEXT_COLOR = 0xFFBFBFBF;
    private static final int TEXT_SHADOW = 0xFF3F3F3F;

    private final String title;
    private final String message;
    private final float duration;
    private long timeRemaining;
    public final Runnable clickAction;

    public final int height;
    private final int titleHeight;
    private final int titlePadding;
    public boolean hovered;

    // TODO: Animations?
    Notification(String title, String message, float duration, Runnable clickAction) {
        this.title = title;
        this.message = message;
        this.duration = duration * 1000;
        this.timeRemaining = (long) this.duration;
        this.clickAction = clickAction;

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        titleHeight = title.isEmpty() ? 0 : fontRenderer.listFormattedStringToWidth(title, TEXT_SPACE).size() * fontRenderer.FONT_HEIGHT;
        int messageHeight = message.isEmpty() ? 0 : fontRenderer.listFormattedStringToWidth(message, TEXT_SPACE).size() * fontRenderer.FONT_HEIGHT;
        titlePadding = title.isEmpty() || message.isEmpty() ? 0 : 5;

        height = titleHeight + messageHeight + TOP_PADDING + BOTTOM_PADDING + titlePadding;
    }

    public void draw(long deltaTime, int yOffset) {
        final int screenWidth = UResolution.getScaledWidth();
        final int screenHeight = UResolution.getScaledHeight();
        final double mouseX = UMouse.Scaled.getX();
        final double mouseY = UMouse.Scaled.getY();

        final int left = screenWidth - WIDTH - RIGHT_MARGIN;
        final int right = screenWidth - RIGHT_MARGIN;
        final int top = screenHeight - height - BOTTOM_MARGIN - yOffset;
        final int bottom = screenHeight - BOTTOM_MARGIN - yOffset;

        hovered = (mouseX > left && mouseX <= right && mouseY > top && mouseY < bottom) && !Minecraft.getMinecraft().inGameHasFocus;
        if (!hovered) timeRemaining -= deltaTime;

        GlStateManager.translate(0, 0, 1000);
        // Inner background
        drawRect(left, top, right, bottom, BACKGROUND_COLOR);
        // Outer border
        drawHorizontalLine(left, right - 1, top, hovered ? BORDER_COLOR_HOVER : BORDER_COLOR);
        drawHorizontalLine(left, right - 1, bottom - 1, hovered ? BORDER_COLOR_HOVER : BORDER_COLOR);
        drawVerticalLine(left, top, bottom, hovered ? BORDER_COLOR_HOVER : BORDER_COLOR);
        drawVerticalLine(right - 1, top, bottom, hovered ? BORDER_COLOR_HOVER : BORDER_COLOR);
        // Progress bar
        drawRect(left + 1, bottom - 4, left + 1 + (timeRemaining / duration) * (WIDTH - 2), bottom - 1, hovered ? PROGRESS_BAR_COLOR_HOVER : PROGRESS_BAR_COLOR);
        // Title
        drawSplitString(title, left + LEFT_PADDING, top + TOP_PADDING, TEXT_SPACE, TITLE_COLOR);
        // Message
        drawSplitString(message, left + LEFT_PADDING, top + TOP_PADDING + titleHeight + titlePadding, TEXT_SPACE, TEXT_COLOR);
        GlStateManager.translate(0, 0, -1000);
    }

    public boolean isExpired() {
        return timeRemaining <= 0;
    }

    /**
     * Same as {@link FontRenderer#drawSplitString(String, int, int, int, int)} but also draws the shadow.
     */
    private static void drawSplitString(String str, int x, int y, int wrapWidth, int color) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        fontRenderer.drawSplitString(str, x + 1, y + 1, wrapWidth, TEXT_SHADOW);
        fontRenderer.drawSplitString(str, x, y, wrapWidth, color);
    }

    /**
     * Slightly modified version of {@link Gui#drawRect(int, int, int, int, int)}.<br>
     * Changes:
     * <ul>
     *     <li>Take <tt>float</tt>s instead of <tt>int</tt>s as arguments for a smoother progress bar</li>
     *     <li>Removed transparency as it's not used</li>
     * </ul>
     */
    private static void drawRect(float left, float top, float right, float bottom, int color) {
        float i;
        if (left < right) {
            i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            i = top;
            top = bottom;
            bottom = i;
        }
        float r = (color >> 16 & 0xFF) / 255f;
        float g = (color >> 8 & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.color(r, g, b);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0).endVertex();
        worldRenderer.pos(right, bottom, 0).endVertex();
        worldRenderer.pos(right, top, 0).endVertex();
        worldRenderer.pos(left, top, 0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }
}
