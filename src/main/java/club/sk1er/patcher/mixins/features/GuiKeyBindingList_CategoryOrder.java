package club.sk1er.patcher.mixins.features;

import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Mixin(GuiKeyBindingList.class)
public class GuiKeyBindingList_CategoryOrder {
    //#if MC==1.8.9
    @Unique
    private static final Map<String, Integer> CATEGORY_ORDER = new HashMap<>();
    static {
        CATEGORY_ORDER.put("key.categories.movement", 1);
        CATEGORY_ORDER.put("key.categories.gameplay", 2);
        CATEGORY_ORDER.put("key.categories.inventory", 3);
        CATEGORY_ORDER.put("key.categories.multiplayer", 4);
        CATEGORY_ORDER.put("key.categories.misc", 5);
        CATEGORY_ORDER.put("key.categories.stream", 6);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;)V"))
    private void patcher$orderCategories(Object[] a) {
        if (a instanceof KeyBinding[]) {
            Arrays.sort((KeyBinding[]) a, (key1, key2) -> {
                if (key1.getKeyCategory().equals(key2.getKeyCategory())) {
                    return I18n.format(key1.getKeyDescription()).compareTo(I18n.format(key2.getKeyDescription()));
                }

                Integer categoryIndex1 = CATEGORY_ORDER.get(key1.getKeyCategory());
                Integer categoryIndex2 = CATEGORY_ORDER.get(key2.getKeyCategory());
                if (categoryIndex1 == null && categoryIndex2 != null) {
                    return 1;
                }
                if (categoryIndex1 != null && categoryIndex2 == null) {
                    return -1;
                }
                if (categoryIndex2 == null) {
                    return I18n.format(key1.getKeyCategory()).compareTo(I18n.format(key2.getKeyCategory()));
                }
                return categoryIndex1.compareTo(categoryIndex2);
            });
        }
    }
    //#endif
}
