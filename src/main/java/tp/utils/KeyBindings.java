package tp.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import tp.utils.methods.*;

public class KeyBindings {
    private String category = "key.categories.tp_utils";
    private TpThrough tpThrough;
    private TpOnTop tpOnTop;
    private TpForward tpForward;
    private TpBack tpBack;

    public void setKeyBindings() {
        tpThrough = new TpThrough();
        tpOnTop = new TpOnTop();
        tpForward = new TpForward();
        tpBack = new TpBack();

        setKeyBindingTPThrough();
        setKeyBindingTPOnTop();
        setKeyBindingTPForward();
        setKeyBindingTPBack();
    }

    private void setKeyBindingTPThrough() {
        KeyBinding tpThroughKey = new KeyBinding("key.tp_utils.tp_through", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_5, category);
        KeyBindingHelper.registerKeyBinding(tpThroughKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpThroughKey.wasPressed()) {
                tpThrough.tpThrough();
            }
        });
    }

    private void setKeyBindingTPOnTop() {
        KeyBinding tpOnTopKey = new KeyBinding("key.tp_utils.tp_on_top", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, category);
        KeyBindingHelper.registerKeyBinding(tpOnTopKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpOnTopKey.wasPressed()) {
                tpOnTop.tpOnTop();
            }
        });
    }

    private void setKeyBindingTPForward() {
        KeyBinding tpForwardKey = new KeyBinding("key.tp_utils.tp_forward", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, category);
        KeyBindingHelper.registerKeyBinding(tpForwardKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpForwardKey.wasPressed()) {
                tpForward.tpForward();
            }
        });
    }

    private void setKeyBindingTPBack() {
        KeyBinding tpBackKey = new KeyBinding("key.tp_utils.tp_back", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, category);
        KeyBindingHelper.registerKeyBinding(tpBackKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpBackKey.wasPressed()) {
                tpBack.tpBack();
            }
        });
    }
}
