package club.sk1er.patcher.hooks;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.chat.ChatUtilities;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.GameSettings;

public class DebugKeybindHook {
    public static void handleDebugChunkKeybind() {
        DebugChunkBordersHook.shouldRender = !DebugChunkBordersHook.shouldRender;
        sendDebugMessage("Chunk borders: " + (DebugChunkBordersHook.shouldRender ? "shown" : "hidden"));
    }

    public static void handleDebugSpectatorSwitcherKeybind(EntityPlayerSP player, NetHandlerPlayClient netHandler) {
        NetworkPlayerInfo playerInfo = netHandler.getPlayerInfo(player.getGameProfile().getId());
        if (playerInfo != null) {
            if (playerInfo.getGameType().isCreative()) {
                player.sendChatMessage("/gamemode spectator");
            } else if (player.isSpectator()) {
                player.sendChatMessage("/gamemode creative");
            }
        }
    }

    public static void handleDebugHelpKeybind(int debugKeycode) {
        String debugKeybind = GameSettings.getKeyDisplayString(debugKeycode);
        ChatUtilities.sendMessage("&e&l[Debug]:&r Key bindings:", false);
        ChatUtilities.sendMessage(debugKeybind + " + A = Reload chunks", false);
        ChatUtilities.sendMessage(debugKeybind + " + B = Show hitboxes", false);
        ChatUtilities.sendMessage(debugKeybind + " + D = Clear chat", false);
        //ChatUtilities.sendMessage(debugKeybind + " + F = Cycle render distance (Shift to invert)", false);
        ChatUtilities.sendMessage(debugKeybind + " + G = Show chunk boundaries", false);
        ChatUtilities.sendMessage(debugKeybind + " + H = Advanced tooltips", false);
        ChatUtilities.sendMessage(debugKeybind + " + N = Cycle creative <-> spectator", false);
        ChatUtilities.sendMessage(debugKeybind + " + P = Pause on lost focus", false);
        ChatUtilities.sendMessage(debugKeybind + " + Q = Show this list", false);
        ChatUtilities.sendMessage(debugKeybind + " + T = Reload resource packs", false);
        ChatUtilities.sendMessage(debugKeybind + " + S = " + (PatcherConfig.separateResourceLoading ? "Reload sounds" : "Reload resource packs"), false);
    }

    public static void sendDebugMessage(String message) {
        if (PatcherConfig.debugKeybindFeedback) {
            ChatUtilities.sendMessage("&e&l[Debug]:&r " + message, false);
        }
    }
}
