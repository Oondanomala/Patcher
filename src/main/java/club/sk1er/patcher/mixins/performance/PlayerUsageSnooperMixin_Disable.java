package club.sk1er.patcher.mixins.performance;

import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Map;
import java.util.Timer;

@Mixin(PlayerUsageSnooper.class)
public class PlayerUsageSnooperMixin_Disable {
    @Shadow
    private boolean isRunning;

    @Shadow
    @Final
    private Timer threadTrigger;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void patcher$killSnooper(String string, IPlayerUsage iPlayerUsage, long l, CallbackInfo ci) {
        threadTrigger.cancel();
        isRunning = true;
    }

    /**
     * @author Oondanomala
     * @reason Skip tracking stuff that won't be used anyway
     */
    @Overwrite
    public void addMemoryStatsToSnooper() {
        // No-op
    }

    /**
     * @author Oondanomala
     * @reason Return no results
     */
    @Overwrite
    @SideOnly(value = Side.CLIENT)
    public Map<String, String> getCurrentStats() {
        return Collections.emptyMap();
    }
}
