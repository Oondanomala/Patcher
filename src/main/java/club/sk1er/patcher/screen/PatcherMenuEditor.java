package club.sk1er.patcher.screen;

import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.mixins.accessors.GuiMainMenuAccessor;
import club.sk1er.patcher.screen.disconnect.SmartDisconnectScreen;
import club.sk1er.patcher.screen.quit.ConfirmQuitScreen;
import club.sk1er.patcher.tweaker.PatcherTweaker;
import gg.essential.api.EssentialAPI;
import gg.essential.api.config.EssentialConfig;
import gg.essential.universal.UDesktop;
import gg.essential.universal.USound;
import me.oondanomala.assential.Assential;
import me.oondanomala.assential.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.net.URI;
import java.util.List;

public class PatcherMenuEditor {
    public static boolean tripped = false;
    private boolean isFirstLaunch = true;

    private final Minecraft mc = Minecraft.getMinecraft();
    private final int[] sequence = new int[]{
        50,
        100,
        156,
        208,
        325,
        425,
        425,
        525,
        25,
        72
    };

    // button ids
    private final int serverList = 231423;
    private final int allSounds = 85348;
    private final int refreshSounds = 85634;

    private List<GuiButton> mcButtonList;
    private GuiButton realmsButton;

    private int next = 0;

    @SubscribeEvent
    public void openMenu(GuiScreenEvent.InitGuiEvent.Post event) {
        //#if MC==10809
        mcButtonList = event.buttonList;
        GuiScreen gui = event.gui;
        //#else
        //$$ mcButtonList = event.getButtonList();
        //$$ GuiScreen gui = event.getGui();
        //#endif
        final int width = gui.width;
        final int height = gui.height;

        if (gui instanceof GuiMainMenu) {
            if (isFirstLaunch) {
                long time = (System.currentTimeMillis() - PatcherTweaker.clientLoadTime);
                if (PatcherConfig.startupNotification) {
                    Notifications.push("Minecraft Startup", "Minecraft started in " + time / 1000L + " seconds.", 6);
                }
                Patcher.instance.getLogger().info("Minecraft started in {}ms.", time);

                if (PatcherConfig.notifyModUpdates) {
                    for (ModContainer mod : Loader.instance().getActiveModList()) {
                        ForgeVersion.CheckResult updateResult = ForgeVersion.getResult(mod);
                        if (updateResult.status == ForgeVersion.Status.OUTDATED) {
                            pushModUpdateNotification(mod.getName(), updateResult);
                        }
                    }
                } else {
                    ForgeVersion.CheckResult updateResult = ForgeVersion.getResult(Loader.instance().activeModContainer());
                    if (updateResult.status == ForgeVersion.Status.OUTDATED) {
                        pushModUpdateNotification("Patcher", updateResult);
                    }
                }

                if (PatcherConfig.startupSound == 1) {
                    USound.INSTANCE.playExpSound();
                } else if (PatcherConfig.startupSound == 2) {
                    USound.INSTANCE.playPlingSound();
                }
                isFirstLaunch = false;
            }
            if (PatcherConfig.cleanMainMenu) {
                realmsButton = ((GuiMainMenuAccessor) gui).getRealmsButton();
                for (GuiButton button : mcButtonList) {
                    if (button.displayString.equals(I18n.format("fml.menu.mods"))) {
                        button.width = 200;
                        break;
                    }
                }
            }
        }
        //#if MC==10809
        else if (gui instanceof GuiOptions && PatcherConfig.cleanOptionsMenu) {
            for (GuiButton button : mcButtonList) {
                if (button.displayString.equals(I18n.format("options.stream"))) {
                    button.visible = false;
                    button.enabled = false;
                } else if (button.displayString.equals(I18n.format("options.sounds"))) {
                    button.xPosition = width / 2 + 5;
                } else if (button.displayString.equals(I18n.format("options.skinCustomisation"))) {
                    button.yPosition = height / 6 + 72 - 6;
                }
            }
        }
        //#endif
        else if (gui instanceof GuiScreenResourcePacks) {
            if (!Loader.isModLoaded("ResourcePackOrganizer")) {
                for (GuiButton button : mcButtonList) {
                    button.width = 200;
                    if (button.id == 2) button.xPosition = (width >> 1) - 204;
                }
            }
        } else if (gui instanceof GuiIngameMenu) {
            if (mc.getCurrentServerData() != null && PatcherConfig.openToLanReplacement > 0) {
                mcButtonList.get(4).visible = false;
                mcButtonList.get(4).enabled = false;
                if (PatcherConfig.openToLanReplacement == 1) {
                    mcButtonList.add(new GuiButton(serverList,
                        (width >> 1) - 100, (height >> 2) + 56,
                        200, 20,
                        "Server List"
                    ));
                }
            }
        } else if (gui instanceof GuiScreenOptionsSounds) {
            //#if MC==10809
            int buttonHeight = height / 6 + 146;
            //#else
            //$$ int buttonHeight = height / 6 + 190;
            //#endif
            mcButtonList.add(new GuiButton(allSounds, (width >> 1) - 100, buttonHeight, 100, 20, "All Sounds"));
            mcButtonList.add(new GuiButton(refreshSounds, (width >> 1), buttonHeight, 100, 20, "Refresh Sounds"));
        }
    }

    @SubscribeEvent
    public void preActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        //#if MC==10809
        GuiScreen gui = event.gui;
        GuiButton button = event.button;
        //#else
        //$$ GuiScreen gui = event.getGui();
        //$$ GuiButton button = event.getButton();
        //#endif
        if (gui instanceof GuiIngameMenu && button.displayString.equals(I18n.format("menu.disconnect")) && !mc.isIntegratedServerRunning() && PatcherConfig.smartDisconnect) {
            mc.displayGuiScreen(new SmartDisconnectScreen(gui));
            event.setCanceled(true);
        } else if (gui instanceof GuiMainMenu && button.displayString.equals(I18n.format("menu.quit")) && PatcherConfig.confirmQuit) {
            mc.displayGuiScreen(new ConfirmQuitScreen(gui));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void actionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        //#if MC==10809
        int buttonId = event.button.id;
        GuiScreen gui = event.gui;
        //#else
        //$$ int buttonId = event.getButton().id;
        //$$ GuiScreen gui = event.getGui();
        //#endif
        if (gui instanceof GuiIngameMenu && buttonId == serverList) {
            mc.displayGuiScreen(new FakeMultiplayerMenu(gui));
        } else if (gui instanceof GuiScreenOptionsSounds) {
            if (buttonId == allSounds) {
                mc.displayGuiScreen(Patcher.instance.getPatcherSoundConfig().gui());
            } else if (buttonId == refreshSounds) {
                mc.getSoundHandler().onResourceManagerReload(mc.getResourceManager());
            }
        }
    }

    @SubscribeEvent
    public void drawMenu(GuiScreenEvent.DrawScreenEvent.Post event) {
        //#if MC==10809
        GuiScreen gui = event.gui;
        //#else
        //$$ GuiScreen gui = event.getGui();
        //#endif
        if (PatcherConfig.cleanMainMenu && gui instanceof GuiMainMenu) {
            if (realmsButton != null) {
                realmsButton.visible = false;
                realmsButton.enabled = false;
            }
        }

        if (PatcherConfig.openToLanReplacement == 2 && gui instanceof GuiIngameMenu && Assential.isEssential()) {
            EssentialConfig config = EssentialAPI.getConfig();
            if (config.getOpenToFriends() && config.getEssentialFull() && EssentialAPI.getOnboardingData().hasAcceptedEssentialTOS()) {
                for (GuiButton button : mcButtonList) {
                    if (button != null && button.displayString.equals("Invite Friends")) {
                        button.width = 200;
                        button.xPosition = (gui.width / 2) - 100;
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void keyboardInput(GuiScreenEvent.KeyboardInputEvent.Post event) {
        //#if MC==10809
        GuiScreen gui = event.gui;
        //#else
        //$$ GuiScreen gui = event.getGui();
        //#endif
        if (gui instanceof GuiMainMenu) {
            int key = Keyboard.getEventKey();
            if (Keyboard.isKeyDown(key) && !Keyboard.isRepeatEvent()) {
                int i = next + 1;
                next = (key >> 3) * ((7 & key) + (i << 1)) == sequence[next] ? i : 0;
                if (next > 9) {
                    next = 0;
                    tripped = !tripped;
                    Patcher.instance.getLogger().info("yep");
                }
            }
        }
    }

    private void pushModUpdateNotification(String modName, ForgeVersion.CheckResult updateCheckResult) {
        Notifications.push(modName + " Update", "A new " + modName + " update is available: "
            + updateCheckResult.target + ". Click to open the download page.", 30, () -> {
            if (updateCheckResult.url == null) {
                Notifications.push("Patcher", modName + " does not link to an update download page.");
                return;
            }
            try {
                UDesktop.browse(new URI(updateCheckResult.url));
            } catch (Exception openException) {
                Patcher.instance.getLogger().error("Failed to open the update download page.", openException);
                Notifications.push("Patcher", "Failed to open the update download page. Link is now copied to your clipboard.");
                try {
                    UDesktop.setClipboardString(updateCheckResult.url);
                } catch (Exception clipboardException) {
                    Patcher.instance.getLogger().error("Failed to copy the update download link to clipboard.", clipboardException);
                    Notifications.push("Patcher", "Failed to copy the update download link to clipboard.");
                }
            }
        });
    }

    public static String modify(String s) {
        // Don't destroy formatting
        s = s.replace("§l", "§\uE4C1");
        s = s.replace("§L", "§\uE4C2");
        s = s.replace("§r", "§\uE4C3");
        s = s.replace("§R", "§\uE4C4");

        s = s.replace("you", "u");
        s = s.replace("YOU", "U");
        s = s.replace("why", "y");
        s = s.replace("WHY", "Y");

        // Avoid false positives
        s = s.replace("are", "a\uE4C5e");
        s = s.replace("ARE", "A\uE4C6E");

        s = s.replace("l", "w");
        s = s.replace("L", "W");
        s = s.replace("r", "w");
        s = s.replace("R", "W");

        s = s.replace("a\uE4C5e", "r");
        s = s.replace("A\uE4C6E", "R");

        s = s.replace("§\uE4C1", "§l");
        s = s.replace("§\uE4C2", "§L");
        s = s.replace("§\uE4C3", "§r");
        s = s.replace("§\uE4C4", "§R");
        return s;
    }
}
