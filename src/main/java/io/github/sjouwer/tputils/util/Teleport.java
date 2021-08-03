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
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " "  + x + " " + y + " " + z);
    }

    public static void toExactPos(Vec3d pos, ModConfig config){
        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }
}
