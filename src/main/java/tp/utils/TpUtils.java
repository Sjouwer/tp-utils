package tp.utils;

import net.fabricmc.api.ModInitializer;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import tp.utils.config.ModConfig;

public class TpUtils implements ModInitializer {

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        KeyBindings keyBindings = new KeyBindings();
        keyBindings.setKeyBindings();

        Commands commands = new Commands();
        commands.registerCommands(ClientCommandManager.DISPATCHER);
    }
}