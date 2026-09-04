package club.sk1er.patcher.mixins.performance.resources.optifine;

import club.sk1er.patcher.hooks.ResourceReloadHook;
import club.sk1er.patcher.hooks.ResourceReloadHook.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

// Has to be in a separate class or mixin explodes
@Mixin(GameSettings.class)
public class GameSettingsMixin_SelectiveResourceReloadOptifine {
    //#if MC==1.8.9
    // Merged with the field in the main mixin
    @SuppressWarnings({"unused", "FieldCanBeLocal", "MissingUnique"})
    private boolean patcher$needsResourceRefresh;

    @Dynamic("Optifine mipmap type setting")
    @Redirect(
        method = "setOptionFloatValueOF",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V"),
        slice = @Slice(from = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/client/settings/GameSettings$Options;MIPMAP_TYPE:Lnet/minecraft/client/settings/GameSettings$Options;"))
    )
    private void patcher$scheduleResourceRefreshOptifine(Minecraft instance) {
        patcher$needsResourceRefresh = true;
    }

    @Dynamic("Optifine anisotropic filtering setting")
    @Redirect(
        method = "setOptionFloatValueOF",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V"),
        slice = @Slice(to = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/client/settings/GameSettings$Options;MIPMAP_TYPE:Lnet/minecraft/client/settings/GameSettings$Options;"))
    )
    private void patcher$refreshResourcesOptifine(Minecraft instance) {
        ResourceReloadHook.reload(instance, ResourceType.MODELS);
        instance.renderGlobal.loadRenderers();
    }
    //#endif
}
