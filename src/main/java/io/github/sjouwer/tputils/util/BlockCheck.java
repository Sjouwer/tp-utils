package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public final class BlockCheck {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private BlockCheck() {
    }

    public static boolean canCollide(BlockPos pos, boolean isLavaAllowed) {
        if (!isLavaAllowed && isLava(pos)) {
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

    //Direction of 1 is forward and -1 is backwards
    public static BlockPos findOpenSpot(HitResult hit, double distance, int direction, ModConfig config) {
        for (int i = Math.max(0, direction); i < distance * 8; i++) {
            Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
            BlockPos pos = new BlockPos(hit.getPos().add(vector.multiply(direction * 0.125 * i)));

            boolean foundObstacle = canCollide(pos, config.isLavaAllowed());
            boolean isLoaded = minecraft.world.getChunkManager().isChunkLoaded(pos.getX() / 16, pos.getZ() / 16);

            if (isLoaded && !foundObstacle && (!config.isBedrockLimitSet() || pos.getY() > minecraft.world.getBottomY())) {
                boolean isBottomBlockFree = !canCollide(pos.down(1), config.isLavaAllowed());
                boolean isTopBlockFree = !canCollide(pos.up(1), config.isLavaAllowed());

                if (isBottomBlockFree) {
                    return pos.down(1);
                }
                else if (isTopBlockFree || config.isCrawlingAllowed()) {
                    return pos;
                }
            }
        }
        return null;
    }

    public static BlockPos findTopSpot(BlockPos pos, boolean isLavaAllowed, boolean isCrawlingAllowed) {
        for (int j = 1; j < minecraft.world.getHeight() + 1; j++) {
            boolean isBottomBlockFree = !BlockCheck.canCollide(pos.up(j), isLavaAllowed);
            boolean isTopBlockFree = !BlockCheck.canCollide(pos.up(j + 1), isLavaAllowed);

            if (isBottomBlockFree && (isCrawlingAllowed || isTopBlockFree)) {
                return pos.up(j);
            }
        }
        return null;
    }
}
