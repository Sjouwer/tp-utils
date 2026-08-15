package io.github.sjouwer.tputils.util;

import io.github.sjouwer.tputils.TpUtils;
import io.github.sjouwer.tputils.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockCheck {
    private static final Minecraft client = Minecraft.getInstance();
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
        BlockState state = client.level.getBlockState(pos);
        VoxelShape collider = state.getCollisionShape(client.level, pos);

        return !collider.isEmpty();
    }

    /**
     * Function to check if the block at the given block position is lava
     * @param pos Block position to check
     * @return True if it's lava
     */
    public static boolean isLava(BlockPos pos) {
        BlockState state = client.level.getBlockState(pos);
        return state.is(Blocks.LAVA);
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
        Vec3 vector = client.getCameraEntity().getViewVector(client.getDeltaTracker().getGameTimeDeltaPartialTick(true));
        for (int i = Math.max(0, direction); i < distance * 8; i++) {
            BlockPos pos = BlockPos.containing(hit.getLocation().add(vector.scale(direction * 0.125 * i)));
            boolean foundObstacle = canCollide(pos, config.isLavaAllowed());
            boolean isLoaded = client.level.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4);

            if (isLoaded && !foundObstacle && (!config.isBedrockLimitSet() || pos.getY() > client.level.getMinY())) {
                boolean isBottomBlockFree = !canCollide(pos.below(1), config.isLavaAllowed());
                boolean isTopBlockFree = !canCollide(pos.above(1), config.isLavaAllowed());

                if (isBottomBlockFree) {
                    return pos.below(1);
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
        for (int j = 1; j < client.level.getHeight() + 1; j++) {
            boolean isBottomBlockFree = !BlockCheck.canCollide(pos.above(j * direction), config.isLavaAllowed());
            boolean isTopBlockFree = !BlockCheck.canCollide(pos.above((j + 1) * direction), config.isLavaAllowed());

            if (isBottomBlockFree && (config.isCrawlingAllowed() || isTopBlockFree)) {
                return pos.above(j * direction);
            }
        }
        return null;
    }
}
