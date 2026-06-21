package club.sk1er.patcher.util.world.render.entity;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.world.render.culling.EntityCulling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class EntityRendering {
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<? extends EntityLivingBase> event) {
        //#if MC==1.8.9
        EntityLivingBase entity = event.entity;
        //#else
        //$$ EntityLivingBase entity = event.getEntity();
        //#endif
        if ((PatcherConfig.disableArmorstands && entity instanceof EntityArmorStand)
            || (PatcherConfig.disableSemitransparentEntities && entity instanceof EntityPlayer && entity.isInvisible())) {
            event.setCanceled(true);
        } else if (EntityCulling.shouldPerformCulling && PatcherConfig.entityRenderDistanceToggle) {
            final double distanceSq = entity.getDistanceSqToEntity(mc.thePlayer);
            boolean isPlayer = entity instanceof EntityPlayer && mc.getRenderViewEntity() != entity;

            if (   (distanceSq > PatcherConfig.entityRenderDistanceSq)
                || (entity instanceof IMob && distanceSq > PatcherConfig.hostileEntityRenderDistanceSq)
                || ((entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntityWaterMob)
                        && distanceSq > PatcherConfig.passiveEntityRenderDistanceSq)
                || (isPlayer && distanceSq > PatcherConfig.playerRenderDistanceSq)) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public void setHorseTransparent(RenderLivingEvent.Pre<EntityHorse> event) {
        if (PatcherConfig.riddenHorseOpacity >= 1.0F) {
            return;
        }
        //#if MC==1.8.9
        Entity ridingEntity = mc.thePlayer.ridingEntity;
        EntityLivingBase entity = event.entity;
        //#else
        //$$ Entity ridingEntity = mc.player.getRidingEntity();
        //$$ EntityLivingBase entity = event.getEntity();
        //#endif
        if (ridingEntity == entity) {
            GlStateManager.color(1, 1, 1, PatcherConfig.riddenHorseOpacity);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        }
    }
}
