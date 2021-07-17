package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import net.minecraft.util.math.BlockPos;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.text.Style.EMPTY;

public class TpBack {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpBack() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            config.setPreviousLocation(minecraft.player.getPos());
            BlockPos blockPos = new BlockPos(coordinates);
            minecraft.player.sendChatMessage(config.tpMethod() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
        }
        else {
            BaseText message = new TranslatableText("text.tp_utils.message.noPreviousLocation");
            message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
            minecraft.player.sendMessage(message, false);
        }
    }
}