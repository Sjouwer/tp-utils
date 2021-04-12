package tp.utils;

import net.fabricmc.api.ModInitializer;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import tp.utils.config.ModConfig;

public class TpUtils implements ModInitializer {

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        KeyBindings keyBindings = new KeyBindings();
        keyBindings.SetKeyBindings();
    }
}
