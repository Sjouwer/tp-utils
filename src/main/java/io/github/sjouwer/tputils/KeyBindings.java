package io.github.sjouwer.tputils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private final Teleports teleports = new Teleports();
    private static final String CATEGORY = "key.categories.tp_utils";

    public void setKeyBindings() {
        setKeyBindingTPThrough();
        setKeyBindingTPOnTop();
        setKeyBindingTPForward();
        setKeyBindingTPBack();
    }

    private void setKeyBindingTPThrough() {
        KeyBinding tpThroughKey = new KeyBinding("key.tp_utils.tp_through", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_5, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpThroughKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpThroughKey.wasPressed()) {
                teleports.tpThrough();
            }
        });
    }

    private void setKeyBindingTPOnTop() {
        KeyBinding tpOnTopKey = new KeyBinding("key.tp_utils.tp_on_top", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpOnTopKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpOnTopKey.wasPressed()) {
                teleports.tpOnTop(null);
            }
        });
    }

    private void setKeyBindingTPForward() {
        KeyBinding tpForwardKey = new KeyBinding("key.tp_utils.tp_forward", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpForwardKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpForwardKey.wasPressed()) {
                teleports.tpForward();
            }
        });
    }

    private void setKeyBindingTPBack() {
        KeyBinding tpBackKey = new KeyBinding("key.tp_utils.tp_back", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpBackKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpBackKey.wasPressed()) {
                teleports.tpBack();
            }
        });
    }
}
