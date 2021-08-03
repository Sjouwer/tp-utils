package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.Teleport;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.Vec3d;

public class ChunkTp {
    private final ModConfig config;

    public ChunkTp() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void chunkTp(int x, int y, int z) {
        double xPos = x * 16 + 8.0;
        double yPos = y * 16 + 8.0;
        double zPos = z * 16 + 8.0;

        Teleport.toExactPos(new Vec3d(xPos, yPos, zPos), config);
    }
}
