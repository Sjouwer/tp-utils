package io.github.sjouwer.tputils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public final class Raycast {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private Raycast() {
    }

    public static HitResult forwardFromPlayer(int range) {
        Entity player = minecraft.cameraEntity;
        Vec3d vector = player.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = player.getCameraPosVec(minecraft.getTickDelta());
        Vec3d rayEnd = rayStart.add(vector.multiply(range));
        return minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
    }

    public static HitResult downwardFromPlayer(boolean lavaAllowed) {
        BlockPos pos = new BlockPos(minecraft.cameraEntity.getEyePos());
        double x = pos.getX() + 0.5;
        double y = Math.min(pos.getY(), minecraft.world.getHeight() + 1);
        double z = pos.getZ() + 0.5;

        Vec3d rayStart = new Vec3d(x, y, z);
        Vec3d rayEnd = new Vec3d(x, minecraft.world.getBottomY(), z);

        HitResult hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        boolean hitLava = BlockCheck.isLava(new BlockPos(hit.getPos()));
        if (hitLava && !lavaAllowed) {
            hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, minecraft.player));
        }

        return hit;
    }
}
