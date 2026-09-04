package club.sk1er.patcher.mixins.performance.resources;

import club.sk1er.patcher.hooks.ResourceReloadHook;
import club.sk1er.patcher.hooks.ResourceReloadHook.ResourceType;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin_SelectiveResourceReload {
    //#if MC==1.8.9
    // F3 + S
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V", ordinal = 0))
    private void patcher$separateSoundReloading(Minecraft minecraft) {
        ResourceReloadHook.reload((Minecraft)(Object) this, ResourceType.SOUNDS);
    }
    //#endif
}
