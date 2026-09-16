package io.github.sjouwer.tputils;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(TpUtils.NAMESPACE, "all"));
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
        KeyMapping tpThroughKey = new KeyMapping(BASE_KEY + ".tpThrough", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_5, CATEGORY);
        KeyMappingHelper.registerKeyMapping(tpThroughKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpThroughKey.consumeClick()) {
                Teleports.tpThrough();
            }
        });
    }

    private static void registerTPOnTopKey() {
        KeyMapping tpOnTopKey = new KeyMapping(BASE_KEY + ".tpOnTop", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, CATEGORY);
        KeyMappingHelper.registerKeyMapping(tpOnTopKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpOnTopKey.consumeClick()) {
                Teleports.tpOnTop(null);
            }
        });
    }

    private static void registerTPForwardKey() {
        KeyMapping tpForwardKey = new KeyMapping(BASE_KEY + ".tpForward", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
        KeyMappingHelper.registerKeyMapping(tpForwardKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpForwardKey.consumeClick()) {
                Teleports.tpForward();
            }
        });
    }

    private static void registerTPBackKey() {
        KeyMapping tpBackKey = new KeyMapping(BASE_KEY + ".tpBack", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
        KeyMappingHelper.registerKeyMapping(tpBackKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (tpBackKey.consumeClick()) {
                Teleports.tpBack();
            }
        });
    }
}
