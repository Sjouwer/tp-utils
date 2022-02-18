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

    /**
     * Function to check if the player would be able to collide at the given block position
     * @param pos Block position to check
     * @param isLavaAllowed If lava isn't allowed it'll be considered collidable
     * @return True if the player would collide
     */
    public static boolean canCollide(BlockPos pos, boolean isLavaAllowed) {
        if (!isLavaAllowed && isLava(pos)) {
            return true;
        }
        BlockState state = minecraft.world.getBlockState(pos);
        VoxelShape collider = state.getCollisionShape(minecraft.world, pos);

        return !collider.isEmpty();
    }

    /**
     * Function to check if the block at the given block position is lava
     * @param pos Block position to check
     * @return True if it's lava
     */
    public static boolean isLava(BlockPos pos) {
        BlockState state = minecraft.world.getBlockState(pos);
        return state.getMaterial() == Material.LAVA;
    }

    /**
     * Function to find an open spot forwards from the given hit position in line with the player's sight
     * @param hit Hit from a raycast originating from the player's eyes
     * @param distance Distance to check
     * @param config All mod settings
     * @return Open and safe spot to tp to
     */
    public static BlockPos findOpenSpotForwards(HitResult hit, double distance, ModConfig config) {
        return findOpenSpotInLineWithPlayer(hit, distance, 1, config);
    }

    /**
     * Function to find an open spot backwards from the given hit position in line with the player's sight
     * @param hit Hit from a raycast originating from the player's eyes
     * @param distance Distance to check
     * @param config All mod settings
     * @return Open and safe spot to tp to
     */
    public static BlockPos findOpenSpotBackwards(HitResult hit, double distance, ModConfig config) {
        return findOpenSpotInLineWithPlayer(hit, distance, -1, config);
    }

    /**
     * @param direction 1 is forwards and -1 is backwards
     */
    private static BlockPos findOpenSpotInLineWithPlayer(HitResult hit, double distance, int direction, ModConfig config) {
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        for (int i = Math.max(0, direction); i < distance * 8; i++) {
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

    /**
     * Function to find the first open spot above the given block position
     * @param pos Block position to start the check from
     * @param isLavaAllowed If allowed it'll consider lava as open/safe
     * @param isCrawlingAllowed If allowed it'll consider 1 block high spaces as open/safe
     * @return Open spot as block position
     */
    public static BlockPos findTopOpenSpot(BlockPos pos, boolean isLavaAllowed, boolean isCrawlingAllowed) {
        return findVerticalOpenSpot(pos, 1, isLavaAllowed, isCrawlingAllowed);
    }

    /**
     * Function to find the first open spot below the given block position
     * @param pos Block position to start the check from
     * @param isLavaAllowed If allowed it'll consider lava as open/safe
     * @param isCrawlingAllowed If allowed it'll consider 1 block high spaces as open/safe
     * @return Open spot as block position
     */
    public static BlockPos findBottomOpenSpot(BlockPos pos, boolean isLavaAllowed, boolean isCrawlingAllowed) {
        return findVerticalOpenSpot(pos, -1, isLavaAllowed, isCrawlingAllowed);
    }

    /**
     * @param direction 1 is upwards and -1 is downwards
     */
    private static BlockPos findVerticalOpenSpot(BlockPos pos, int direction, boolean isLavaAllowed, boolean isCrawlingAllowed) {
        for (int j = 1; j < minecraft.world.getHeight() + 1; j++) {
            boolean isBottomBlockFree = !BlockCheck.canCollide(pos.up(j * direction), isLavaAllowed);
            boolean isTopBlockFree = !BlockCheck.canCollide(pos.up((j + 1) * direction), isLavaAllowed);

            if (isBottomBlockFree && (isCrawlingAllowed || isTopBlockFree)) {
                return pos.up(j * direction);
            }
        }
        return null;
    }
}
