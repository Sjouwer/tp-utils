package io.github.sjouwer.tputils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class RaycastUtil {
    private static final Minecraft client = Minecraft.getInstance();

    private RaycastUtil() {
    }

    /**
     * Raycast from the player's eyes in the direction the player is looking
     * @param range Range of the raycast in blocks
     * @return Result of the Raycast
     */
    public static HitResult forwardFromPlayer(int range) {
        float tickDelta = client.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        Entity player = client.getCameraEntity();
        Vec3 vector = player.getViewVector(tickDelta);
        Vec3 rayStart = player.getEyePosition(tickDelta);
        Vec3 rayEnd = rayStart.add(vector.scale(range));
        return client.level.clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    }

    /**
     * Raycast downwards from the player's head
     * @param isLavaAllowed If lava is allowed the raycast won't consider it an obstacle
     * @return Result of the Raycast
     */
    public static HitResult downwardFromPlayer(boolean isLavaAllowed) {
        BlockPos pos = BlockPos.containing(client.getCameraEntity().getEyePosition());
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
        double y = Math.min(pos.getY(), client.level.getHeight() + 1);
        double z = pos.getZ() + 0.5;

        Vec3 rayStart = new Vec3(x, y, z);
        Vec3 rayEnd = new Vec3(x, client.level.getMinY(), z);

        HitResult hit = client.level.clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, client.player));

        boolean hitLava = BlockCheck.isLava(BlockPos.containing(hit.getLocation()));
        if (hitLava && !isLavaAllowed) {
            hit = client.level.clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, client.player));
        }

        return hit;
    }

    /**
     * Raycast upwards from the player's head
     * @return Result of the Raycast
     */
    public static HitResult upwardFromPlayer() {
        BlockPos pos = BlockPos.containing(client.getCameraEntity().getEyePosition());
        return upwardFromPos(pos);
    }

    /**
     * Raycast upwards from the given block position
     * @param pos Block position to start the raycast from
     * @return Result of the Raycast
     */
    public static HitResult upwardFromPos(BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = Math.max(pos.getY(), client.level.getMinY() - 1);
        double z = pos.getZ() + 0.5;

        Vec3 rayStart = new Vec3(x, y, z);
        Vec3 rayEnd = new Vec3(x, client.level.getHeight(), z);

        return client.level.clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, client.player));
    }
}
