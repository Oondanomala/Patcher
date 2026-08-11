package club.sk1er.patcher.mixins.bugfixes.forge;

import net.minecraftforge.client.model.pipeline.LightUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LightUtil.class)
public class LightUtilMixin_FixLighting {
    //#if MC==1.8.9
    /**
     * @author asbyth, Oondanomala
     * @reason Fix lighting bugs
     */
    @Overwrite
    public static float diffuseLight(float x, float y, float z) {
        return Math.min(x * x * 0.6f + y * y * ((3f + y) / 4f) + z * z * 0.8f, 1f);
    }
    //#endif
}
