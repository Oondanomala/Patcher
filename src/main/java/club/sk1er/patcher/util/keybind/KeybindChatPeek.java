package club.sk1er.patcher.util.keybind;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeybindChatPeek extends KeyBinding {
    public KeybindChatPeek() {
        // Use §r to have a different name from Essential's Chat Peek keybind
        super("Chat Peek§r", Keyboard.KEY_NONE, "Patcher");
    }
}
