package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.Teleport;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class TpBack {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private final Style style = new Style();

    public TpBack() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            Teleport.toExactPos(coordinates, config);
        }
        else {
            BaseText message = new TranslatableText("text.tp_utils.message.noPreviousLocation");
            style.setColor(Formatting.DARK_RED);
            message.setStyle(style);
            minecraft.player.sendMessage(message);
        }
    }
}
