package club.sk1er.patcher.mixins.performance;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_SkipTwitchRendering {
    //#if MC==1.8.9
    /**
     * @author asbyth, Oondanomala
     * @reason Skip Twitch rendering
     */
    @Overwrite
    public void renderStreamIndicator(float partialTicks) {
        // No-op
    }
    //#endif
}
