package club.sk1er.patcher.optifine;

import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.mixins.accessors.optifine.ConfigAccessor;
import club.sk1er.patcher.mixins.accessors.optifine.CustomColorsAccessor;
import club.sk1er.patcher.tweaker.ModCompatManager;

public class OptiFineFontRendererHandler {
    private static boolean caughtError = false;
    public static int getTextColor(int index, int originalColor) {
        if (caughtError || ModCompatManager.optifineVersion.equals("NONE")) {
            return originalColor;
        }
        try {
            if (ConfigAccessor.invokeIsCustomColors()) {
                return CustomColorsAccessor.invokeGetTextColor(index, originalColor);
            }
            return originalColor;
        } catch (Throwable t) {
            caughtError = true;
            Patcher.instance.getLogger().error("Unable to get OptiFine's Custom Color", t);
            return originalColor;
        }
    }
}
