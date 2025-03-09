package club.sk1er.patcher.hooks;

import club.sk1er.patcher.config.PatcherConfig;
import gg.essential.universal.UResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class CrosshairHook {
    public static boolean shouldRenderCrosshair(Minecraft mc) {
        return (!PatcherConfig.crosshairPerspective || mc.gameSettings.thirdPersonView == 0) && (!PatcherConfig.guiCrosshair || mc.currentScreen == null);
    }

    public static void renderDebugCrosshair(Minecraft mc, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((UResolution.getScaledWidth() / 2F), (UResolution.getScaledHeight() / 2F), 0);
        Entity entity = mc.getRenderViewEntity();
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1, 0, 0);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0, 1, 0);
        GlStateManager.scale(-1, -1, -1);

        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GL11.glLineWidth(4);
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(0, 0, 0).color(0, 0, 0, 255).endVertex();
        worldRenderer.pos(10, 0, 0).color(0, 0, 0, 255).endVertex();
        worldRenderer.pos(0, 0, 0).color(0, 0, 0, 255).endVertex();
        worldRenderer.pos(0, 10, 0).color(0, 0, 0, 255).endVertex();
        worldRenderer.pos(0, 0, 0).color(0, 0, 0, 255).endVertex();
        worldRenderer.pos(0, 0, 10).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth(2);
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(0, 0, 0).color(255, 0, 0, 255).endVertex();
        worldRenderer.pos(10, 0, 0).color(255, 0, 0, 255).endVertex();
        worldRenderer.pos(0, 0, 0).color(0, 255, 0, 255).endVertex();
        worldRenderer.pos(0, 10, 0).color(0, 255, 0, 255).endVertex();
        worldRenderer.pos(0, 0, 0).color(127, 127, 255, 255).endVertex();
        worldRenderer.pos(0, 0, 10).color(127, 127, 255, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth(1);

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
