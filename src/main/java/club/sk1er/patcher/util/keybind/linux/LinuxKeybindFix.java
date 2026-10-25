package club.sk1er.patcher.util.keybind.linux;

import club.sk1er.patcher.config.PatcherConfig;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Keyboard;

import java.util.Map;

//#if MC==1.12.2
//$$ import net.minecraft.inventory.ClickType;
//#endif

public class LinuxKeybindFix {
    private final Minecraft mc = Minecraft.getMinecraft();
    @SuppressWarnings("unchecked") // Stupid...
    private static final Map<Character, Integer>[] triggers = new Map[]{
        // BE AZERTY
        ImmutableMap.builder()
            .put('&', 0)
            .put('é', 1)
            .put('"', 2)
            .put('\'', 3)
            .put('(', 4)
            .put('§', 5)
            .put('è', 6)
            .put('!', 7)
            .put('ç', 8)
        .build(),
        // FR AZERTY
        ImmutableMap.builder()
            .put('&', 0)
            .put('é', 1)
            .put('"', 2)
            .put('\'', 3)
            .put('(', 4)
            .put('-', 5)
            .put('è', 6)
            .put('_', 7)
            .put('ç', 8)
        .build(),
        // DE QWERTZ
        ImmutableMap.builder()
            .put('!', 0)
            .put('"', 1)
            .put('§', 2)
            .put('$', 3)
            .put('%', 4)
            .put('&', 5)
            .put('/', 6)
            .put('(', 7)
            .put(')', 8)
        .build()
    };

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (mc.thePlayer != null && Keyboard.isCreated() && Keyboard.getEventKeyState()) {
            if (PatcherConfig.keyboardLayout == 0) {
                final int eventKey = Keyboard.getEventKey();
                switch (eventKey) {
                    case 145:
                        if (mc.gameSettings.keyBindsHotbar[1].getKeyCode() == 3) mc.thePlayer.inventory.currentItem = 1;
                        break;

                    case 144:
                        if (mc.gameSettings.keyBindsHotbar[5].getKeyCode() == 7) mc.thePlayer.inventory.currentItem = 5;
                        break;
                }
            } else {
                char charPressed = Keyboard.getEventCharacter();
                Integer i = triggers[PatcherConfig.keyboardLayout - 1].get(charPressed);
                if (i != null && mc.gameSettings.keyBindsHotbar[i].getKeyCode() == i + 2) {
                    mc.thePlayer.inventory.currentItem = i;
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiPress(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        //#if MC==1.8.9
        GuiScreen guiScreen = event.gui;
        //#else
        //$$ GuiScreen guiScreen = event.getGui();
        //#endif
        if (PatcherConfig.keyboardLayout != 0 && guiScreen instanceof GuiContainer && mc.thePlayer != null
            && Keyboard.isCreated() && Keyboard.getEventKeyState()) {
            char charPressed = Keyboard.getEventCharacter();
            GuiContainer gui = (GuiContainer) guiScreen;
            Slot slot = gui.getSlotUnderMouse();
            if (slot != null) {
                Integer i = triggers[PatcherConfig.keyboardLayout - 1].get(charPressed);
                if (i != null) {
                    mc.playerController.windowClick(
                        gui.inventorySlots.windowId,
                        slot.slotNumber,
                        i,
                        //#if MC==1.8.9
                        2,
                        //#else
                        //$$ ClickType.SWAP,
                        //#endif
                        mc.thePlayer
                    );
                    event.setCanceled(true);
                }
            }
        }
    }
}
