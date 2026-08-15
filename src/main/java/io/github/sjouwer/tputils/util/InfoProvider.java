package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.TpUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;

public final class InfoProvider {
    private static final Minecraft client = Minecraft.getInstance();

    private InfoProvider() {
    }

    /**
     * Function to send a message in green color to the player's chatbox
     * @param message Message to send to the player
     */
    public static void sendMessage(MutableComponent message) {
        if (client.player == null) {
            TpUtils.LOGGER.info(message.getString());
            return;
        }

        message.withStyle(ChatFormatting.GREEN);
        client.player.sendSystemMessage(message);
    }

    /**
     * Function to send a message in dark red color to the player's chatbox
     * @param errorMessage Error message to send to the player
     */
    public static void sendError(MutableComponent errorMessage) {
        if (client.player == null) {
            TpUtils.LOGGER.error(errorMessage.getString());
            return;
        }

        errorMessage.withStyle(ChatFormatting.DARK_RED);
        client.player.sendSystemMessage(errorMessage);
    }
}
