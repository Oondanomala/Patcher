package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.hooks.VertexLighterSmoothAoHook;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VertexLighterSmoothAo.class)
public abstract class VertexLighterSmoothAoMixin_FasterLightmap
    //#if MC==1.8.9
    extends VertexLighterFlat
    //#endif
{
    //#if MC==1.8.9
    /**
     * @author asbyth, Oondanomala
     * @reason Optimize lightmap calculation
     */
    @Overwrite
    @Override
    protected void updateLightmap(float[] normal, float[] lightmap, float x, float y, float z) {
        VertexLighterSmoothAoHook.calcLightmap(blockInfo, lightmap, x, y, z);
    }
    //#endif
}
