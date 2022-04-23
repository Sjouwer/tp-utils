package io.github.sjouwer.tputils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;

import static net.minecraft.text.Style.EMPTY;

public final class Chat {
    private Chat() {
    }

    /**
     * Function to send a message in dark red color to the player's chatbox
     * @param messageContent Error message content to send to the player
     */
    public static void sendError(TextContent messageContent) {
        MutableText errorMessage = MutableText.of(messageContent);
        errorMessage.setStyle(EMPTY.withColor(Formatting.DARK_RED));

        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.sendMessage(errorMessage, false);
        }
    }
}
