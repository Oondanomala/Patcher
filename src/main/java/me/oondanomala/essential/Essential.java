package me.oondanomala.essential;

import gg.essential.universal.UChat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Essential {
    static Logger logger;
    static String modName;
    static String modVersion;

    private Essential() {
    }

    /**
     * Call this in the {@link net.minecraftforge.fml.common.event.FMLInitializationEvent FMLInitializationEvent}
     *
     * @param modName    The name of your mod, should be the same value passed in the {@link net.minecraftforge.fml.common.Mod @Mod} annotation
     * @param modVersion The version of your mod, should be the same value passed in the {@link net.minecraftforge.fml.common.Mod @Mod} annotation
     */
    public static void init(String modName, String modVersion) {
        Essential.modName = modName;
        Essential.modVersion = modVersion;
        logger = LogManager.getLogger(modName);
        MinecraftForge.EVENT_BUS.register(GuiUtil.INSTANCE);
        MinecraftForge.EVENT_BUS.register(Notifications.INSTANCE);
    }

    /**
     * Returns <tt>true</tt> if Essential is loaded.
     * Useful if you still want to support Essential, but not depend on it.
     */
    public static boolean isEssential() {
        return Loader.isModLoaded("essential");
    }

    // Utility methods
    static void sendMessage(String message, boolean prefix) {
        if (prefix) {
            message = "&e[" + modName + "]&r " + message;
        }
        UChat.chat(message);
    }
}
