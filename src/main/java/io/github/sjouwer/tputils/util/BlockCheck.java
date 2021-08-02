package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

public final class BlockCheck {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private BlockCheck() {
    }

    public static boolean canCollide(BlockPos pos, ModConfig config) {
        if (!config.isLavaAllowed() && isLava(pos)) {
            return true;
        }
        BlockState state = minecraft.world.getBlockState(pos);
        VoxelShape collider = state.getCollisionShape(minecraft.world, pos);

        return !collider.isEmpty();
    }

    public static boolean isLava(BlockPos pos) {
        BlockState state = minecraft.world.getBlockState(pos);
        return state.getMaterial() == Material.LAVA;
    }
}
