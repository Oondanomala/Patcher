package club.sk1er.patcher.mixins.performance.resources;

import club.sk1er.patcher.hooks.ResourceReloadHook;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SimpleReloadableResourceManager.class)
public class SimpleReloadableResourceManager_SelectiveResourceReload {
    // TODO: MixinExtras @WrapWithCondition
    @Redirect(method = "notifyReloadListeners", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IResourceManagerReloadListener;onResourceManagerReload(Lnet/minecraft/client/resources/IResourceManager;)V"))
    private void patcher$selectiveReload(IResourceManagerReloadListener instance, IResourceManager iResourceManager) {
        if (ResourceReloadHook.shouldReload(instance))  {
            instance.onResourceManagerReload(iResourceManager);
        }
    }
}
