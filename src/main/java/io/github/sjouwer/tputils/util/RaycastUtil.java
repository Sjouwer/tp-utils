package io.github.sjouwer.tputils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public final class RaycastUtil {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private RaycastUtil() {
    }

    /**
     * Raycast from the player's eyes in the direction the player is looking
     * @param range Range of the raycast in blocks
     * @return Result of the Raycast
     */
    public static HitResult forwardFromPlayer(int range) {
        float tickDelta = client.getRenderTickCounter().getTickProgress(true);
        Entity player = client.cameraEntity;
        Vec3d vector = player.getRotationVec(tickDelta);
        Vec3d rayStart = player.getCameraPosVec(tickDelta);
        Vec3d rayEnd = rayStart.add(vector.multiply(range));
        return client.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
    }

    /**
     * Raycast downwards from the player's head
     * @param isLavaAllowed If lava is allowed the raycast won't consider it an obstacle
     * @return Result of the Raycast
     */
    public static HitResult downwardFromPlayer(boolean isLavaAllowed) {
        BlockPos pos = BlockPos.ofFloored(client.cameraEntity.getEyePos());
        return downwardFromPos(pos, isLavaAllowed);
    }

    /**
     * Raycast downwards from the given block position
     * @param pos Block position to start the raycast from
     * @param isLavaAllowed If lava is allowed the raycast won't consider it an obstacle
     * @return Result of the Raycast
     */
    public static HitResult downwardFromPos(BlockPos pos, boolean isLavaAllowed) {
        double x = pos.getX() + 0.5;
        double y = Math.min(pos.getY(), client.world.getHeight() + 1);
        double z = pos.getZ() + 0.5;

        Vec3d rayStart = new Vec3d(x, y, z);
        Vec3d rayEnd = new Vec3d(x, client.world.getBottomY(), z);

        HitResult hit = client.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player));

        boolean hitLava = BlockCheck.isLava(BlockPos.ofFloored(hit.getPos()));
        if (hitLava && !isLavaAllowed) {
            hit = client.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, client.player));
        }

        return hit;
    }

    /**
     * Raycast upwards from the player's head
     * @return Result of the Raycast
     */
    public static HitResult upwardFromPlayer() {
        BlockPos pos = BlockPos.ofFloored(client.cameraEntity.getEyePos());
        return upwardFromPos(pos);
    }

    /**
     * Raycast upwards from the given block position
     * @param pos Block position to start the raycast from
     * @return Result of the Raycast
     */
    public static HitResult upwardFromPos(BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = Math.max(pos.getY(), client.world.getBottomY() - 1);
        double z = pos.getZ() + 0.5;

        Vec3d rayStart = new Vec3d(x, y, z);
        Vec3d rayEnd = new Vec3d(x, client.world.getHeight(), z);

        return client.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player));
    }
}
