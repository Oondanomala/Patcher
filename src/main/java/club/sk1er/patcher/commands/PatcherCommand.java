package club.sk1er.patcher.commands;

import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.chat.ChatUtilities;
import me.oondanomala.assential.GuiUtil;
import club.sk1er.patcher.util.screenshot.AsyncScreenshots;
import me.oondanomala.assential.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.SyntaxErrorException;

import java.util.Locale;
import java.util.Objects;

public class PatcherCommand extends Command {
    public PatcherCommand() {
        super("patcher");
    }

    @Override
    protected void onCommand(String subCommand, String[] args) throws CommandException {
        if (subCommand.isEmpty()) {
            GuiUtil.open(Objects.requireNonNull(Patcher.instance.getPatcherConfig().gui()));

        } else if (subCommand.equals("help")) {
            ChatUtilities.sendMessage("&bUsage for /" + name, false);
            ChatUtilities.sendMessage("&c/" + name + " blacklist <ip>&7 - Tell the client that you don't want to use the 1.11+ chat length on the specified server IP.", false);
            ChatUtilities.sendMessage("&c/" + name + " fov <amount>&7 - Change your FOV to a custom value.", false);
            ChatUtilities.sendMessage("&c/" + name + " fps <amount>&7 - Choose what to limit the game's framerate to outside of Minecraft's options. 0 will use your normal framerate.", false);
            ChatUtilities.sendMessage("&c/" + name + " scale <0|1|2|3|4|5|auto|help|large|none|normal|off|small>&7 - Change the scale of your inventory independent of your GUI scale.", false);
            ChatUtilities.sendMessage("&c/" + name + " sendcoords [additional information]&7 - Send your current coordinates in chat. Anything after 'sendcoords' will be put at the end of the message.", false);
            ChatUtilities.sendMessage("&c/" + name + " sounds&7 - Open the Sound Configuration GUI.", false);

        } else if (subCommand.equals("blacklist")) {
            if (args.length == 0) {
                throw new SyntaxErrorException("blacklist <ip>");
            }
            blacklist(CommandBase.buildString(args, 0));

        } else if (subCommand.equals("fov")) {
            final String error = "fov <amount>";
            if (args.length != 1) {
                throw new SyntaxErrorException(error);
            }
            float fov = parseFloat(args[0], error);
            fov(fov);

        } else if (subCommand.equals("scale") || subCommand.equals("invscale") || subCommand.equals("inventoryscale")) {
            if (args.length != 1) {
                throw new SyntaxErrorException("scale <0|1|2|3|4|5|auto|help|large|none|normal|off|small>");
            }
            scale(args[0].toLowerCase(Locale.ENGLISH));

        } else if (subCommand.equals("sendcoords")) {
            sendcoords(CommandBase.buildString(args, 0));

        } else if (subCommand.equals("sounds")) {
            if (args.length != 0) {
                throw new SyntaxErrorException("sounds");
            }
            GuiUtil.open(Objects.requireNonNull(Patcher.instance.getPatcherSoundConfig().gui()));

        } else if (subCommand.equals("fps")) {
            final String error = "fps <amount>";
            if (args.length != 1) {
                throw new SyntaxErrorException(error);
            }
            int fps = parseInt(args[0], error);
            fps(fps);

        } else if (subCommand.equals("$favorite")) {
            AsyncScreenshots.favoriteScreenshot();
        } else if (subCommand.equals("$delete")) {
            AsyncScreenshots.deleteScreenshot();
        } else if (subCommand.equals("$upload")) {
            AsyncScreenshots.uploadScreenshot();
        } else if (subCommand.equals("$copyss")) {
            AsyncScreenshots.copyScreenshot();
        } else {
            throw new SyntaxErrorException("<help|blacklist|fov|fps|inventoryscale|invscale|scale|sendcoords|sounds>");
        }
    }

    private void blacklist(String ip) {
        String status = Patcher.instance.addOrRemoveBlacklist(ip) ? "&cnow" : "&ano longer";
        ChatUtilities.sendMessage("Server &e\"" + ip + "\" &ris " + status + " &rblacklisted from chat length extension.");
        Patcher.instance.saveBlacklistedServers();
    }

    private void fov(float fov) throws CommandException {
        if (fov <= 0) {
            throw new CommandException("Changing your FOV to or below 0 is disabled due to game-breaking visual bugs.");
        } else if (fov > 110) {
            throw new CommandException("Changing your FOV above 110 is disabled due to game-breaking visual bugs.");
        }
        Minecraft mc = Minecraft.getMinecraft();
        ChatUtilities.sendMessage("FOV changed from &e" + mc.gameSettings.fovSetting + "&r to &a" + fov + ".");
        mc.gameSettings.fovSetting = fov;
        mc.gameSettings.saveOptions();
    }

    private void scale(String arg) throws CommandException {
        if (arg.equals("help")) {
            ChatUtilities.sendMessage("             &eInventory Scale", false);
            ChatUtilities.sendMessage("&7Usage: /patcher scale <scaling>", false);
            ChatUtilities.sendMessage("&7Scaling may be a number between 1-5, or", false);
            ChatUtilities.sendMessage("&7small/normal/large/auto", false);
            ChatUtilities.sendMessage("&7Use '/patcher scale off' to disable scaling.", false);
            return;
        }

        if (arg.equals("off") || arg.equals("none")) {
            ChatUtilities.sendMessage("Disabled inventory scaling.");
            PatcherConfig.inventoryScale = 0;
            Patcher.instance.forceSaveConfig();
            return;
        }

        int scaling;
        if (arg.equals("small")) {
            scaling = 1;
        } else if (arg.equals("normal")) {
            scaling = 2;
        } else if (arg.equals("large")) {
            scaling = 3;
        } else if (arg.equals("auto")) {
            scaling = 5;
        } else {
            scaling = parseInt(arg, "scale <0|1|2|3|4|5|auto|help|large|none|normal|off|small>");
        }

        if (scaling < 1) {
            ChatUtilities.sendMessage("Disabled inventory scaling.");
            PatcherConfig.inventoryScale = 0;
            Patcher.instance.forceSaveConfig();
            return;
        } else if (scaling > 5) {
            throw new CommandException("Invalid scaling. Must be between 1-5.");
        }

        ChatUtilities.sendMessage("Set inventory scaling to " + scaling);
        PatcherConfig.inventoryScale = scaling;
        Patcher.instance.forceSaveConfig();
    }

    private void sendcoords(String message) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.sendChatMessage("x: " + (int) player.posX + ", y: " + (int) player.posY + ", z: " + (int) player.posZ +
            // might be an issue if they provide a long message?
            " " + message);
    }

    private void fps(int fps) throws CommandException {
        if (fps < 0) {
            throw new CommandException("You cannot set your framerate to a negative number.");
        } else if (fps == PatcherConfig.customFpsLimit) {
            throw new CommandException("Custom framerate is already set to this value.");
        }

        PatcherConfig.customFpsLimit = fps;
        Patcher.instance.forceSaveConfig();
        ChatUtilities.sendMessage(fps == 0 ? "Custom framerate was reset." : "Custom framerate set to " + fps + ".");
    }

    @Override
    protected String[] addTabCompletions(String[] args) {
        if (args.length == 0) {
            return new String[]{"blacklist", "fov", "fps", "inventoryscale", "invscale", "scale", "sendcoords", "sounds", "help"};
        } else if (args.length == 1 &&
            args[0].equals("scale") ||
            args[0].equals("invscale") ||
            args[0].equals("inventoryscale")) {
            return new String[]{"help", "0", "1", "2", "3", "4", "5", "auto",  "large", "none", "normal", "off", "small"};
        }

        return new String[0];
    }
}
