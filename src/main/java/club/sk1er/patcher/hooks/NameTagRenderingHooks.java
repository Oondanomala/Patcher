package club.sk1er.patcher.hooks;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.mixins.accessors.FontRendererAccessor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class NameTagRenderingHooks {
    public static int drawNametagText(FontRenderer fr, String text, int x, int y, int color) {
        FontRendererAccessor fontRenderer = (FontRendererAccessor) fr;
        if (PatcherConfig.shadowedNametagText) {
            GlStateManager.depthMask(false);
            int render = fontRenderer.invokeRenderString(text, PatcherConfig.alternateTextShadow ? x : x + 1.0F, y + 1.0F, color, true);
            GlStateManager.depthMask(true);

            render = Math.max(render, fontRenderer.invokeRenderString(text, x, y, color, false));

            GlStateManager.colorMask(false, false, false, false);
            render = Math.max(render, fr.drawString(text, x, y, color, true));
            GlStateManager.colorMask(true, true, true, true);
            return render;
        } else {
            return fontRenderer.invokeRenderString(text, x, y, color, false);
        }
    }
}
