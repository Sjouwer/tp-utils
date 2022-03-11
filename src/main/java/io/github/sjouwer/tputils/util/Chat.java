package io.github.sjouwer.tputils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

import static net.minecraft.text.Style.EMPTY;

public final class Chat {
    private Chat() {
    }

    /**
     * Function to send a message in dark red color to the player's chatbox
     * @param message Message to send
     */
    public static void sendError(BaseText message) {
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        MinecraftClient.getInstance().player.sendMessage(message, false);
    }
}
