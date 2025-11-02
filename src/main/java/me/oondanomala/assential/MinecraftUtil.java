package me.oondanomala.assential;

import net.minecraft.client.Minecraft;

import java.util.Locale;

public final class MinecraftUtil {
    private MinecraftUtil() {
    }

    /**
     * Checks whether the player is currently on the Hypixel server.
     *
     * @return <tt>true</tt> if the player is on Hypixel, <tt>false</tt> otherwise
     */
    public static boolean isHypixel() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.isSingleplayer()) {
            return false;
        }
        String brand = mc.thePlayer.getClientBrand();
        return brand != null && brand.toLowerCase(Locale.ENGLISH).contains("hypixel");
    }
}
