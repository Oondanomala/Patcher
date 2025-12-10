package club.sk1er.patcher.mixins.bugfixes.crashes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeBookCloning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RecipeBookCloning.class)
public class RecipeBookCloningMixin_ResolveCrash {
    //#if MC==1.8.9
    @Inject(method = "getCraftingResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemEditableBook;getGeneration(Lnet/minecraft/item/ItemStack;)I", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void patcher$checkIfNBTExists(InventoryCrafting inv, CallbackInfoReturnable<ItemStack> cir, int i, ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            cir.setReturnValue(null);
        }
    }
    //#endif
}
