package club.sk1er.patcher.mixins.bugfixes.forge;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiModList.class)
public class GuiModListMixin_RemoveErroneousSlash {
    //#if MC==1.8.9
    // TODO: MixinExtras @ModifyReturnValue
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    private String patcher$removeSlash(String translateKey, Object[] parameters) {
        return I18n.format(translateKey, parameters).replace("\\", "");
    }
    //#endif
}
