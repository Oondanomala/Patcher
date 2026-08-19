package club.sk1er.patcher.mixins.bugfixes.forge;

import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftForge.class)
public class MinecraftForgeMixin_FixTypo {
    //#if MC==1.8.9
    @ModifyConstant(method = "initialize", constant = {
        @Constant(stringValue = "net.minecraft.client.particle,EffectRenderer$1"),
        @Constant(stringValue = "net.minecraft.client.particle,EffectRenderer$2"),
        @Constant(stringValue = "net.minecraft.client.particle,EffectRenderer$3"),
        @Constant(stringValue = "net.minecraft.client.particle,EffectRenderer$4"),
    })
    private static String patcher$fixTypo(String constant) {
        return constant.replace(',', '.');
    }
    //#endif
}
