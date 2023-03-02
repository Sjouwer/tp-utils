package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.TpUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public final class InfoProvider {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private InfoProvider() {
    }

    /**
     * Function to send a message in green color to the player's chatbox
     * @param message Message to send to the player
     */
    public static void sendMessage(MutableText message) {
        if (client.player == null) {
            TpUtils.LOGGER.info(message.getString());
            return;
        }

        message.formatted(Formatting.GREEN);
        client.player.sendMessage(message, false);
    }

    /**
     * Function to send a message in dark red color to the player's chatbox
     * @param errorMessage Error message to send to the player
     */
    public static void sendError(MutableText errorMessage) {
        if (client.player == null) {
            TpUtils.LOGGER.error(errorMessage.getString());
            return;
        }

        errorMessage.formatted(Formatting.DARK_RED);
        client.player.sendMessage(errorMessage, false);
    }
}
