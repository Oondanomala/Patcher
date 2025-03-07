package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.RomanNumerals;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryEffectRenderer.class)
public class InventoryEffectRendererMixin_FixPotionEffectNumerals {
    @Unique
    private int patcher$potionAmplifierLevel;

    // TODO: Use MixinExtras @ModifyExpressionValue
    @Redirect(
        method = "drawActivePotionEffects",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionEffect;getAmplifier()I", ordinal = 0)
    )
    private int patcher$skipOriginalCode(PotionEffect potionEffect) {
        if (PatcherConfig.betterRomanNumerals) {
            this.patcher$potionAmplifierLevel = potionEffect.getAmplifier();
            return 1;
        }
        return potionEffect.getAmplifier();
    }

    @Redirect(
        method = "drawActivePotionEffects",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", ordinal = 1)
    )
    private String patcher$addRomanNumeral(String translateKey, Object... parameters) {
        if (PatcherConfig.betterRomanNumerals) {
            if (patcher$potionAmplifierLevel > 0) {
                return RomanNumerals.toRoman(patcher$potionAmplifierLevel + 1);
            }
            return "";
        }
        return I18n.format(translateKey, parameters);
    }

}
