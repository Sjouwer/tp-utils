package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class Teleport {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private Teleport() {
    }

    public static void toBlockPos(BlockPos pos, ModConfig config){
        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    public static void toExactPos(Vec3d pos, ModConfig config){
        if (config.tpMethod().equals("/tp")) {
            config.setPreviousLocation(minecraft.player.getPos());
            minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        }
        else {
            BlockPos blockPos = new BlockPos(pos.getX(), Math.ceil(pos.getY()), pos.getZ());
            toBlockPos(blockPos, config);
        }
    }
}
