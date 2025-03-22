package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.hooks.DebugChunkBordersHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRenderer_DebugChunkBorders {
    //#if MC==10809
    @Shadow
    private Minecraft mc;

    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;debugView:Z", opcode = Opcodes.GETFIELD, ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=destroyProgress")))
    private void patcher$renderDebugChunkBorders(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        mc.mcProfiler.endStartSection("outline");
        if (DebugChunkBordersHook.shouldRender && !mc.thePlayer.hasReducedDebug() && !mc.gameSettings.reducedDebugInfo) {
            DebugChunkBordersHook.render(partialTicks);
        }
    }
    //#endif
}
