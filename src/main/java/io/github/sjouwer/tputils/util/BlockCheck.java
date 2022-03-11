package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.TpUtils;
import io.github.sjouwer.tputils.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public final class BlockCheck {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final ModConfig config = TpUtils.getConfig();

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
        BlockState state = client.world.getBlockState(pos);
        VoxelShape collider = state.getCollisionShape(client.world, pos);

        return !collider.isEmpty();
    }

    /**
     * Function to check if the block at the given block position is lava
     * @param pos Block position to check
     * @return True if it's lava
     */
    public static boolean isLava(BlockPos pos) {
        BlockState state = client.world.getBlockState(pos);
        return state.getMaterial() == Material.LAVA;
    }

    /**
     * Function to find an open spot forwards from the given hit position in line with the player's sight
     * @param hit Hit from a raycast originating from the player's eyes
     * @param distance Distance to check
     * @return Open and safe spot to tp to
     */
    public static BlockPos findOpenSpotForwards(HitResult hit, double distance) {
        return findOpenSpotInLineWithPlayer(hit, distance, 1);
    }

    /**
     * Function to find an open spot backwards from the given hit position in line with the player's sight
     * @param hit Hit from a raycast originating from the player's eyes
     * @param distance Distance to check
     * @return Open and safe spot to tp to
     */
    public static BlockPos findOpenSpotBackwards(HitResult hit, double distance) {
        return findOpenSpotInLineWithPlayer(hit, distance, -1);
    }

    /**
     * @param direction 1 is forwards and -1 is backwards
     */
    private static BlockPos findOpenSpotInLineWithPlayer(HitResult hit, double distance, int direction) {
        Vec3d vector = client.cameraEntity.getRotationVec(client.getTickDelta());
        for (int i = Math.max(0, direction); i < distance * 8; i++) {
            BlockPos pos = new BlockPos(hit.getPos().add(vector.multiply(direction * 0.125 * i)));
            boolean foundObstacle = canCollide(pos, config.isLavaAllowed());
            boolean isLoaded = client.world.getChunkManager().isChunkLoaded(pos.getX() / 16, pos.getZ() / 16);

            if (isLoaded && !foundObstacle && (!config.isBedrockLimitSet() || pos.getY() > client.world.getBottomY())) {
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
     * @return Open spot as block position
     */
    public static BlockPos findTopOpenSpot(BlockPos pos) {
        return findVerticalOpenSpot(pos, 1);
    }

    /**
     * Function to find the first open spot below the given block position
     * @param pos Block position to start the check from
     * @return Open spot as block position
     */
    public static BlockPos findBottomOpenSpot(BlockPos pos) {
        return findVerticalOpenSpot(pos, -1);
    }

    /**
     * @param direction 1 is upwards and -1 is downwards
     */
    private static BlockPos findVerticalOpenSpot(BlockPos pos, int direction) {
        for (int j = 1; j < client.world.getHeight() + 1; j++) {
            boolean isBottomBlockFree = !BlockCheck.canCollide(pos.up(j * direction), config.isLavaAllowed());
            boolean isTopBlockFree = !BlockCheck.canCollide(pos.up((j + 1) * direction), config.isLavaAllowed());

            if (isBottomBlockFree && (config.isCrawlingAllowed() || isTopBlockFree)) {
                return pos.up(j * direction);
            }
        }
        return null;
    }
}
