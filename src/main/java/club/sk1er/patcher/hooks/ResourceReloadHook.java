package club.sk1er.patcher.hooks;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.mixins.accessors.SimpleReloadableResourceManagerAccessor;
import club.sk1er.patcher.util.enhancement.ReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraftforge.client.model.ICustomModelLoader;

public class ResourceReloadHook {
    private static ResourceType currentResourceType;

    public static void reload(Minecraft mc, ResourceType resourceType) {
        if (PatcherConfig.selectiveResourceReloading) {
            try {
                currentResourceType = resourceType;
                ((SimpleReloadableResourceManagerAccessor) mc.getResourceManager()).invokeNotifyReloadListeners();
            } finally {
                currentResourceType = null;
            }
        } else {
            mc.refreshResources();
        }
    }

    public static boolean shouldReload(IResourceManagerReloadListener reloadListener) {
        if (currentResourceType == null) return true;

        if (reloadListener instanceof ICustomModelLoader
            || reloadListener instanceof ModelManager || reloadListener instanceof RenderItem
            || reloadListener instanceof RenderGlobal || reloadListener instanceof BlockRendererDispatcher) {
            return currentResourceType == ResourceType.MODELS;
        } else if (reloadListener instanceof FontRenderer || reloadListener instanceof TextureManager
            || reloadListener instanceof FoliageColorReloadListener || reloadListener instanceof GrassColorReloadListener
            || reloadListener instanceof ReloadListener) {
            return currentResourceType == ResourceType.TEXTURES;
        } else if (reloadListener instanceof SoundHandler || reloadListener instanceof club.sk1er.patcher.util.world.sound.SoundHandler) {
            return currentResourceType == ResourceType.SOUNDS;
        } else if (reloadListener instanceof LanguageManager) {
            return currentResourceType == ResourceType.LANGUAGES;
        }
        return true;
    }

    public enum ResourceType {
        MODELS,
        TEXTURES,
        SOUNDS,
        LANGUAGES
    }
}
