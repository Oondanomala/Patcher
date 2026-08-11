package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GuiIngameForge.class)
public abstract class GuiIngameForgeMixin_FixAbsorptionHealth extends GuiIngame {
    public GuiIngameForgeMixin_FixAbsorptionHealth(Minecraft mc) {
        super(mc);
    }

    @ModifyArg(method = "renderHealth", index = 2,
        at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;drawTexturedModalRect(IIIIII)V"),
        slice = @Slice(
            from = @At(value = "CONSTANT", args = "intValue=153"),
            to = @At(value = "CONSTANT:LAST", args = "intValue=36")))
    private int patcher$fixAbsorptionHearts(int original) {
        if (mc.thePlayer.isPotionActive(Potion.poison)) original -= 36;
        else if (mc.thePlayer.isPotionActive(Potion.wither)) original -= 108;
        return original;
    }
}
