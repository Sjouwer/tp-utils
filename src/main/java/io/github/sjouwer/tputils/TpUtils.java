package io.github.sjouwer.tputils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import io.github.sjouwer.tputils.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TpUtils implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("TP Utils");
    private static ConfigHolder<ModConfig> configHolder;

    public static ModConfig getConfig() {
        return configHolder.getConfig();
    }

    public static void saveConfig() {
        configHolder.save();
    }

    @Override
    public void onInitializeClient() {
        configHolder = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        KeyBindings.registerKeyBindings();
        Commands.registerCommands();
    }
}
