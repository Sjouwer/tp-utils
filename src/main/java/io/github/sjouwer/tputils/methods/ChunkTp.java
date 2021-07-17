package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;

public class ChunkTp {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public ChunkTp() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void chunkTp(int x, int y, int z) {
        int xPos = x * 16 + 8;
        int yPos = y * 16 + 8;
        int zPos = z * 16 + 8;

        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " " + xPos + ".0 " + yPos + ".0 " + zPos + ".0");
    }
}
