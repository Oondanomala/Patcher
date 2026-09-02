package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.ducks.BlockInfoExt;
import club.sk1er.patcher.ducks.VertexLighterFlatExt;
import club.sk1er.patcher.hooks.BlockInfoHook;
import net.minecraftforge.client.model.pipeline.BlockInfo;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VertexLighterFlat.class)
public class VertexLighterFlatMixin_CacheFlatLighting
    //#if MC==1.8.9
    implements VertexLighterFlatExt
    //#endif
{
    //#if MC==1.8.9
    @Shadow @Final
    protected BlockInfo blockInfo;

    /**
     * @author asbyth, Oondanomala
     * @reason Use cached flat lighting
     */
    @Overwrite
    protected void updateLightmap(float[] normal, float[] lightmap, float x, float y, float z) {
        BlockInfoHook.updateLightmap(normal, lightmap, x, y, z);
    }

    @Inject(method = "updateBlockInfo", at = @At("TAIL"))
    private void patcher$cacheFlatLighting(CallbackInfo ci) {
        ((BlockInfoExt) blockInfo).patcher$updateFlatLighting();
    }

    @Override
    public void patcher$resetBlockInfo() {
        ((BlockInfoExt) blockInfo).patcher$reset();
    }
    //#endif
}
