package club.sk1er.patcher.mixins.performance;

//#if MC==1.8.9
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//#endif
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Minecraft.class)
public class MinecraftMixin_SkipTwitchStuff {
    //#if MC==1.8.9
    @Shadow
    private IStream stream;

    /**
     * @author Oondanomala
     * @reason Skip Twitch code
     */
    @Overwrite
    private void initStream() {
        this.stream = new NullStream(new Throwable(""));
    }

    @Redirect(
        method = "runGameLoop",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/stream/IStream;func_152935_j()V")
    )
    private void patcher$skipTwitchCode1(IStream instance) {
        // No-op
    }

    @Redirect(
        method = "runGameLoop",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/stream/IStream;func_152922_k()V")
    )
    private void patcher$skipTwitchCode2(IStream instance) {
        // No-op
    }
    //#endif
}
