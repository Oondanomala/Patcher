package club.sk1er.patcher.mixins.performance.resources;

import club.sk1er.patcher.ducks.GameSettingsExt;
import club.sk1er.patcher.hooks.ResourceReloadHook;
import club.sk1er.patcher.hooks.ResourceReloadHook.ResourceType;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameSettings.class)
public class GameSettingsMixin_SelectiveResourceReload
    //#if MC==1.8.9
    implements GameSettingsExt
    //#endif
{
    //#if MC==1.8.9
    @Shadow
    protected Minecraft mc;

    @SuppressWarnings("MissingUnique") // Merged with the field from the optifine mixin
    private boolean patcher$needsResourceRefresh;

    @Override
    public void patcher$onSettingsGuiClosed() {
        if (patcher$needsResourceRefresh) {
            mc.addScheduledTask(() -> ResourceReloadHook.reload(mc, ResourceType.MODELS));
            patcher$needsResourceRefresh = false;
        }
    }

    // Mipmap levels
    @Redirect(
        method = "setOptionFloatValue",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;scheduleResourcesRefresh()Lcom/google/common/util/concurrent/ListenableFuture;")
    )
    private ListenableFuture<Object> patcher$scheduleResourceRefresh(Minecraft instance) {
        patcher$needsResourceRefresh = true;
        return null;
    }

    // 3D Anaglyph
    @Redirect(
        method = "setOptionValue",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V")
    )
    private void patcher$selectiveResourceReload(Minecraft instance) {
        ResourceReloadHook.reload(instance, ResourceType.TEXTURES);
    }
    //#endif
}
