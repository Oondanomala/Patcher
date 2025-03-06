package me.oondanomala.essential;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class GuiUtil {
    static final GuiUtil INSTANCE = new GuiUtil();
    private static GuiScreen guiToDisplay;

    private GuiUtil() {
    }

    public static void open(GuiScreen gui) {
        guiToDisplay = gui;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (guiToDisplay != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToDisplay);
            guiToDisplay = null;
        }
    }
}
