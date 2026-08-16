package club.sk1er.patcher.util.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;

public class ChatUtilities {

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player.addChatMessage(new ChatComponentText(
                prefix ? "§e[Patcher] §r" + message : message
            ));
        }
    }
}
