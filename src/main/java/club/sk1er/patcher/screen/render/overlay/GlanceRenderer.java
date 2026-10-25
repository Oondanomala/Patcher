package club.sk1er.patcher.screen.render.overlay;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.mixins.accessors.ItemAccessor;
import club.sk1er.patcher.mixins.accessors.ItemStackAccessor;
import com.google.common.collect.Multimap;
import gg.essential.universal.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
//#if MC==1.8.9
import net.minecraft.world.WorldSettings;
//#else
//$$ import net.minecraft.potion.PotionUtils;
//$$ import net.minecraft.world.GameType;
//$$ import net.minecraft.enchantment.Enchantment;
//$$ import net.minecraft.inventory.EntityEquipmentSlot;
//$$ import net.minecraft.entity.SharedMonsterAttributes;
//#endif
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to render stuff above or around the hotbar.
 * <p>
 * Planned to be removed once we start work on a new ChromaHUD version, as this does not fit nor make any
 * sense to be in Patcher, and would fit much better in a centralized HUD mod.
 */
public class GlanceRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final DecimalFormat format = new DecimalFormat("#.###");
    private boolean renderingArrows;
    private boolean renderingDamage;

    /**
     * Create a map for short enchantment name identification.
     * Protection -> P, Fire Protection -> FP, Blast Protection -> BP, etc.
     */
    private final Map<Integer, String> shortEnchantmentNames = new HashMap<>();
    {
        shortEnchantmentNames.put(0, "P");
        shortEnchantmentNames.put(1, "FP");
        shortEnchantmentNames.put(2, "FF");
        shortEnchantmentNames.put(3, "BP");
        shortEnchantmentNames.put(4, "PP");
        shortEnchantmentNames.put(5, "R");
        shortEnchantmentNames.put(6, "AA");
        shortEnchantmentNames.put(7, "T");
        shortEnchantmentNames.put(8, "DS");
        shortEnchantmentNames.put(9, "FW"); // frost walker - 1.12 only
        shortEnchantmentNames.put(16, "SH");
        shortEnchantmentNames.put(17, "SM");
        shortEnchantmentNames.put(18, "BoA");
        shortEnchantmentNames.put(19, "KB");
        shortEnchantmentNames.put(20, "FA");
        shortEnchantmentNames.put(21, "L");
        shortEnchantmentNames.put(32, "EFF");
        shortEnchantmentNames.put(33, "ST");
        shortEnchantmentNames.put(34, "UNB");
        shortEnchantmentNames.put(35, "F");
        shortEnchantmentNames.put(48, "POW");
        shortEnchantmentNames.put(49, "PUN");
        shortEnchantmentNames.put(50, "FLA");
        shortEnchantmentNames.put(51, "INF");
        shortEnchantmentNames.put(61, "LoS");
        shortEnchantmentNames.put(62, "LU");
        shortEnchantmentNames.put(70, "MEN"); // mending - 1.12 only
    }

    /**
     * Render the damage of the currently held item above the hotbar for easy viewing.
     *
     * @param event {@link RenderGameOverlayEvent.Post}
     */
    @SubscribeEvent
    public void renderDamage(RenderGameOverlayEvent.Post event) {
        EntityPlayerSP player = mc.thePlayer;
        //#if MC==1.8.9
        RenderGameOverlayEvent.ElementType type = event.type;
        ScaledResolution res = event.resolution;
        //#else
        //$$ RenderGameOverlayEvent.ElementType type = event.getType();
        //$$ ScaledResolution res = event.getResolution();
        //#endif
        if (type != RenderGameOverlayEvent.ElementType.TEXT || !PatcherConfig.damageGlance || player == null || player.isSpectator()) {
            return;
        }

        final ItemStack heldItemStack = player.inventory.getCurrentItem();
        if (heldItemStack != null) {
            final String attackDamage = getAttackDamageString(heldItemStack);
            if (attackDamage == null) {
                return;
            }

            renderingDamage = true;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);

            final int x = res.getScaledWidth() - (mc.fontRendererObj.getStringWidth(attackDamage) >> 1);
            final int y = (res.getScaledHeight() - 56 + (mc.playerController.shouldDrawHUD() ? -1 : 14) + 9 << 1) + 9;

            mc.fontRendererObj.drawString(attackDamage, x, y, -1, true);

            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.popMatrix();
        }
    }

    /**
     * Render the item count of the currently held item for easy viewing.
     *
     * @param event {@link RenderGameOverlayEvent.Post}
     */
    @SubscribeEvent
    public void renderItemCount(final RenderGameOverlayEvent.Post event) {
        EntityPlayerSP player = mc.thePlayer;
        //#if MC==1.8.9
        RenderGameOverlayEvent.ElementType type = event.type;
        ScaledResolution res = event.resolution;
        //#else
        //$$ RenderGameOverlayEvent.ElementType type = event.getType();
        //$$ ScaledResolution res = event.getResolution();
        //#endif
        if (type != RenderGameOverlayEvent.ElementType.TEXT || !PatcherConfig.itemCountGlance || player == null || player.isSpectator()) {
            return;
        }

        ItemStack currentItem = player.inventory.getCurrentItem();
        if (currentItem != null) {
            // air counts for some reason on 1.12?
            //#if MC==1.12.2
            //$$ if (currentItem.getItem() == Items.AIR) return;
            //#endif

            boolean holdingBow = currentItem.getItem() instanceof ItemBow;
            int count = getHeldItemCount(holdingBow);
            boolean shouldRenderArrowCount = holdingBow && count > 0;
            this.renderingArrows = shouldRenderArrowCount;

            if (count > 1 || shouldRenderArrowCount) {
                int offset = mc.playerController.getCurrentGameType() ==
                    //#if MC==1.8.9
                    WorldSettings.GameType.CREATIVE
                    //#else
                    //$$ GameType.CREATIVE
                    //#endif
                    ? -12 : 0;
                mc.fontRendererObj.drawString(String.valueOf(count),
                    res.getScaledWidth() - mc.fontRendererObj.getStringWidth(String.valueOf(count)) >> 1,
                    res.getScaledHeight() - 43 - offset,
                    -1,
                    true);
            }
        }
    }

    /**
     * Render the enchantments of the currently held item for easy viewing.
     *
     * @param event {@link RenderGameOverlayEvent.Post}
     */
    @SubscribeEvent
    public void renderEnchantments(final RenderGameOverlayEvent.Post event) {
        final EntityPlayerSP player = mc.thePlayer;
        //#if MC==1.8.9
        RenderGameOverlayEvent.ElementType type = event.type;
        ScaledResolution res = event.resolution;
        //#else
        //$$ RenderGameOverlayEvent.ElementType type = event.getType();
        //$$ ScaledResolution res = event.getResolution();
        //#endif
        if (type != RenderGameOverlayEvent.ElementType.TEXT || !PatcherConfig.enchantmentsGlance || player == null || player.isSpectator()) {
            return;
        }

        final ItemStack heldItemStack = player.inventory.getCurrentItem();
        if (heldItemStack != null) {
            final String toDraw = heldItemStack.getItem() instanceof ItemPotion ? getPotionEffectString(heldItemStack) : getEnchantmentString(heldItemStack);
            if (toDraw == null) {
                return;
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);

            final int x = res.getScaledWidth() - (mc.fontRendererObj.getStringWidth(toDraw) >> 1);
            int y = res.getScaledHeight() - 56 + (mc.playerController.shouldDrawHUD() ? 2 : 14) + 9 << 1;

            if (this.renderingArrows || this.renderingDamage) {
                y -= 7;
            }

            mc.fontRendererObj.drawString(toDraw, x, y, -1, true);

            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.popMatrix();
        }
    }

    /**
     * Get the currently held items attack damage by searching through the item's attribute modifiers.
     *
     * @param stack Currently held item.
     * @return If the item has an "x Attack Damage" string in the lore, return the number, otherwise return empty.
     */
    private String getAttackDamageString(ItemStack stack) {
        if (stack != null) {
            //#if MC==1.12.2
            //$$ final Multimap<String, AttributeModifier> modifiers = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            //#else
            final Multimap<String, AttributeModifier> modifiers = stack.getAttributeModifiers();
            //#endif

            if (!modifiers.isEmpty()) {
                double damage = 0;
                int operation = 0;

                for (Map.Entry<String, AttributeModifier> entry : modifiers.entries()) {
                    final AttributeModifier modifier = entry.getValue();
                    if (modifier.getID() == ItemAccessor.getItemModifierUUID()) {
                        double baseDamage = modifier.getAmount()
                            + EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);

                        //#if MC==1.12.2
                        //$$ baseDamage += mc.player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                        //#endif

                        if (baseDamage > 0) {
                            operation = modifier.getOperation();
                            damage = operation != 1 && operation != 2 ? baseDamage : baseDamage * 100.0D;
                            break;
                        }
                    }
                }

                //noinspection ConstantConditions - IntelliJ incorrectly identifies this as a constant condition evaluating to false. It doesn't seem to notice the `damage = ...` statement just a few lines above.
                if (damage > 0) {
                    //#if MC==1.12.2
                    //$$ return I18n.translateToLocalFormatted(
                    //#else
                    return StatCollector.translateToLocalFormatted(
                    //#endif
                        "attribute.modifier.plus." + operation,
                        this.format.format(damage),
                        ""
                    );
                }
            }
        }

        return null;
    }

    /**
     * Get the amount of the currently held item in the players inventory.
     *
     * @param holdingBow If the player is holding a bow, count arrows instead.
     * @return The amount of the currently held item.
     */
    private int getHeldItemCount(boolean holdingBow) {
        ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
        if (currentItem == null) return 0;

        int id = Item.getIdFromItem(currentItem.getItem());
        int data = currentItem.getItemDamage();
        int count = 0;

        if (holdingBow) {
            id = Item.getIdFromItem(Items.arrow);
            data = 0;
        }

        for (ItemStack itemStack : mc.thePlayer.inventory.mainInventory) {
            if (itemStack != null) {
                Item item = itemStack.getItem();

                if (Item.getIdFromItem(item) == id && itemStack.getItemDamage() == data) {
                    count += ((ItemStackAccessor) (Object) itemStack).getStackSize();
                }
            }
        }

        return count;
    }

    /**
     * Get the duration and name of the held potion.
     *
     * @param heldItemStack Currently held item.
     * @return Potion duration & name.
     */
    private String getPotionEffectString(ItemStack heldItemStack) {
        ItemPotion potion = (ItemPotion) heldItemStack.getItem();
        List<PotionEffect> effects =
            //#if MC==1.8.9
            potion.getEffects(heldItemStack);
            //#else
            //$$ PotionUtils.getEffectsFromStack(heldItemStack);
            //#endif
        if (effects == null) return null;

        StringBuilder potionBuilder = new StringBuilder();

        for (PotionEffect entry : effects) {
            int duration = entry.getDuration() / 20;
            potionBuilder
                .append(ChatColor.BOLD)
                .append(StatCollector.translateToLocal(entry.getEffectName()))
                .append(" ")
                .append(entry.getAmplifier() + 1)
                .append(" ")
                .append("(")
                .append(duration / 60)
                .append(String.format(":%02d", duration % 60))
                .append(") ");
        }

        return potionBuilder.toString().trim();
    }

    /**
     * Get the enchantments of the currently held item using our short enchantment mapping.
     *
     * @param heldItemStack Currently held item.
     * @return Currently held items enchantments.
     */
    private String getEnchantmentString(ItemStack heldItemStack) {
        StringBuilder builder = new StringBuilder();
        //#if MC==1.8.9
        Map<Integer, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(heldItemStack);
        for (Map.Entry<Integer, Integer> entry : enchantmentMap.entrySet()) {
            builder.append(ChatColor.BOLD).append(shortEnchantmentNames.get(entry.getKey()))
        //#else
        //$$ Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(heldItemStack);
        //$$ for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
        //$$    builder.append(ChatColor.BOLD).append(shortEnchantmentNames.get(Enchantment.getEnchantmentID(entry.getKey())))
        //#endif
                .append(" ")
                .append(entry.getValue())
                .append(" ");
        }

        return builder.toString().trim();
    }
}
