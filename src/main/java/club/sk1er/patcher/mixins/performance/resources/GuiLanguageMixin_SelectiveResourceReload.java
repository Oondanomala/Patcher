package club.sk1er.patcher.mixins.performance.resources;

import club.sk1er.patcher.hooks.ResourceReloadHook;
import club.sk1er.patcher.hooks.ResourceReloadHook.ResourceType;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.gui.GuiLanguage$List")
public class GuiLanguageMixin_SelectiveResourceReload {
    //#if MC==1.8.9
    @Redirect(method = "elementClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V"))
    private void patcher$selectiveResourceReload(Minecraft instance) {
        ResourceReloadHook.reload(instance, ResourceType.LANGUAGES);
    }
    //#endif
}
