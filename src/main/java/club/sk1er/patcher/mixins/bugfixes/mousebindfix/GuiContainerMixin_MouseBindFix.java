package club.sk1er.patcher.mixins.bugfixes.mousebindfix;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC>1.8.9
//$$ import net.minecraft.inventory.ClickType;
//#endif

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin_MouseBindFix extends GuiScreen {
    @Shadow
    private Slot theSlot;

    //#if MC==1.8.9
    @Shadow
    protected abstract void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType);
    //#else
    //$$ @Shadow
    //$$ protected abstract void handleMouseClick(Slot slotIn, int slotId, int clickedButton, ClickType type);
    //#endif

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void patcher$checkCloseClick(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        int key = mouseButton - 100;
        if (key == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.thePlayer.closeScreen();
            ci.cancel();
        } else if (key == mc.gameSettings.keyBindDrop.getKeyCode() && this.theSlot != null && this.theSlot.getHasStack()) {
            //#if MC==1.8.9
            handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
            //#else
            //$$ handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            //#endif
            ci.cancel();
        }
    }
}
