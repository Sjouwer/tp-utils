package tp.utils.methods;

import tp.utils.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;

public class ChunkTp {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public ChunkTp() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void chunkTp(int x, int z) {
        int xPos = x * 16 + 8;
        int yPos = 100;
        int zPos = z * 16 + 8;

        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " " + xPos + " " + yPos + " " + zPos);
    }
}