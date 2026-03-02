package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.DebugKeybindHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin_BetterDebugKeys {
    //#if MC==10809
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    private RenderManager renderManager;
    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public abstract NetHandlerPlayClient getNetHandler();

    // TODO: Use MixinExtras @WrapOperation for all of these

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V", ordinal = 0, shift = At.Shift.AFTER))
    private void patcher$soundReloadingFeedback(CallbackInfo ci) {
        if (PatcherConfig.separateResourceLoading) {
            DebugKeybindHook.sendDebugMessage("Reloaded sounds");
        } else {
            DebugKeybindHook.sendDebugMessage("Reloaded resource packs");
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V", ordinal = 1, shift = At.Shift.AFTER))
    private void patcher$resourceReloadingFeedback(CallbackInfo ci) {
        DebugKeybindHook.sendDebugMessage("Reloaded resource packs");
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;setOptionValue(Lnet/minecraft/client/settings/GameSettings$Options;I)V", shift = At.Shift.AFTER))
    private void patcher$renderDistanceFeedback(CallbackInfo ci) {
        // TODO: The game doesn't do anything when you run this, maybe fix that?
        //DebugKeybindHook.sendDebugMessage("Render distance: " + gameSettings.renderDistanceChunks);
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;loadRenderers()V", shift = At.Shift.AFTER))
    private void patcher$reloadChunksFeedback(CallbackInfo ci) {
        DebugKeybindHook.sendDebugMessage("Reloading all chunks");
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;advancedItemTooltips:Z", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    private void patcher$advancedTooltipsFeedback(CallbackInfo ci) {
        DebugKeybindHook.sendDebugMessage("Advanced tooltips: " + (gameSettings.advancedItemTooltips ? "shown" : "hidden"));
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;setDebugBoundingBox(Z)V", shift = At.Shift.AFTER))
    private void patcher$boundingBoxesFeedback(CallbackInfo ci) {
        DebugKeybindHook.sendDebugMessage("Hitboxes: " + (renderManager.isDebugBoundingBox() ? "shown" : "hidden"));
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;pauseOnLostFocus:Z", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    private void patcher$pauseFocusLostFeedback(CallbackInfo ci) {
        DebugKeybindHook.sendDebugMessage("Pause on lost focus: " + (gameSettings.pauseOnLostFocus ? "disabled" : "enabled"));
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void patcher$additionalKeybinds(CallbackInfo ci, int k) {
        // TODO: Use MixinExtras @Local
        int debugKeycode = Patcher.instance.getCustomDebug().getKeyCode();
        if (debugKeycode == Keyboard.KEY_NONE) {
            debugKeycode = Keyboard.KEY_F3;
        }
        if (Keyboard.isKeyDown(debugKeycode)) {
            if (k == Keyboard.KEY_G) {
                DebugKeybindHook.handleDebugChunkKeybind();
            } else if (k == Keyboard.KEY_N) {
                DebugKeybindHook.handleDebugSpectatorSwitcherKeybind(thePlayer, getNetHandler());
            } else if (k == Keyboard.KEY_Q) {
                DebugKeybindHook.handleDebugHelpKeybind(debugKeycode);
            }
        }
    }
    //#endif
}
