package tp.utils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import org.lwjgl.glfw.GLFW;
import tp.utils.methods.TpBack;
import tp.utils.methods.TpForward;
import tp.utils.methods.TpOnTop;
import tp.utils.methods.TpThrough;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import tp.utils.config.ModConfig;

public class TpUtils implements ModInitializer {
    private String category = "key.categories.tp_utils";
    private TpThrough tpThrough;
    private TpOnTop tpOnTop;
    private TpForward tpForward;
    private TpBack tpBack;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        tpThrough = new TpThrough();
        tpOnTop = new TpOnTop();
        tpForward = new TpForward();
        tpBack = new TpBack();

        setKeyBindingTPThrough();
        setKeyBindingTPOnTop();
        setKeyBindingTPForward();
        setKeyBindingTPBack();

        Commands commands = new Commands();
        commands.registerCommands(ClientCommandManager.DISPATCHER);
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
