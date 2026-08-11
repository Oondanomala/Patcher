package club.sk1er.patcher.hooks;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;

@SuppressWarnings("unused")
public class GuiIngameForgeHook {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int drawActionbarText(String recordPlaying, int color) {
        int stringWidth = mc.fontRendererObj.getStringWidth(recordPlaying);
        if (PatcherConfig.actionbarBackground && !recordPlaying.trim().isEmpty()) {
            Gui.drawRect((-stringWidth >> 1) - 6, 8, (stringWidth >> 1) + 6, -8, Integer.MIN_VALUE);
        }
        return mc.fontRendererObj.drawString(recordPlaying, -stringWidth >> 1, -4, color, PatcherConfig.shadowedActionbarText);
    }
}
