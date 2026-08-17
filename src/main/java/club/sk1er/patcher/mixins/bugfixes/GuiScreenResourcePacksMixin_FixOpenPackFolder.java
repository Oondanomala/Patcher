package club.sk1er.patcher.mixins.bugfixes;

import gg.essential.universal.UDesktop;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;

@Mixin(GuiScreenResourcePacks.class)
public class GuiScreenResourcePacksMixin_FixOpenPackFolder {
    @Inject(
        method = "actionPerformed",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/ResourcePackRepository;getDirResourcepacks()Ljava/io/File;"),
        cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void patcher$fixFolderOpening(GuiButton button, CallbackInfo ci, File file) {
        if (UDesktop.open(file)) {
            ci.cancel();
        }
    }
}
