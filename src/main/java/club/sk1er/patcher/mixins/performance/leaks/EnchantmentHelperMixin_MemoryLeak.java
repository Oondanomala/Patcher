package club.sk1er.patcher.mixins.performance.leaks;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin_MemoryLeak {
    //#if MC==1.8.9
    @Shadow @Final
    private static EnchantmentHelper.ModifierDamage enchantmentModifierDamage;
    @Shadow @Final
    private static EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT;
    @Shadow @Final
    private static EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE;

    @Inject(method = "getEnchantmentModifierDamage", at = @At("TAIL"))
    private static void patcher$fixWorldLeak(ItemStack[] stacks, DamageSource source, CallbackInfoReturnable<Integer> cir) {
        enchantmentModifierDamage.source = null;
    }

    @Inject(method = "applyThornEnchantments", at = @At("TAIL"))
    private static void patcher$fixWorldLeak2(EntityLivingBase p_151384_0_, Entity p_151384_1_, CallbackInfo ci) {
        ENCHANTMENT_ITERATOR_HURT.attacker = null;
        ENCHANTMENT_ITERATOR_HURT.user = null;
    }

    @Inject(method = "applyArthropodEnchantments", at = @At("TAIL"))
    private static void patcher$fixWorldLeak3(EntityLivingBase p_151384_0_, Entity p_151384_1_, CallbackInfo ci) {
        ENCHANTMENT_ITERATOR_DAMAGE.user = null;
        ENCHANTMENT_ITERATOR_DAMAGE.target = null;
    }
    //#endif
}
