package io.github.sjouwer.tputils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of(TpUtils.NAMESPACE, "all"));
    private static final String BASE_KEY = "key." + TpUtils.NAMESPACE;

    private KeyBindings() {
    }

    public static void registerKeyBindings() {
        registerTPThroughKey();
        registerTPOnTopKey();
        registerTPForwardKey();
        registerTPBackKey();
    }

    private static void registerTPThroughKey() {
        KeyBinding tpThroughKey = new KeyBinding(BASE_KEY + ".tpThrough", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_5, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpThroughKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpThroughKey.wasPressed()) {
                Teleports.tpThrough();
            }
        });
    }

    private static void registerTPOnTopKey() {
        KeyBinding tpOnTopKey = new KeyBinding(BASE_KEY + ".tpOnTop", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpOnTopKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpOnTopKey.wasPressed()) {
                Teleports.tpOnTop(null);
            }
        });
    }

    private static void registerTPForwardKey() {
        KeyBinding tpForwardKey = new KeyBinding(BASE_KEY + ".tpForward", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpForwardKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpForwardKey.wasPressed()) {
                Teleports.tpForward();
            }
        });
    }

    private static void registerTPBackKey() {
        KeyBinding tpBackKey = new KeyBinding(BASE_KEY + ".tpBack", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
        KeyBindingHelper.registerKeyBinding(tpBackKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpBackKey.wasPressed()) {
                Teleports.tpBack();
            }
        });
    }
}
