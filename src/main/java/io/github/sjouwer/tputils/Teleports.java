package io.github.sjouwer.tputils;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import io.github.sjouwer.tputils.util.InfoProvider;
import io.github.sjouwer.tputils.util.RaycastUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Teleports {
    private static final Minecraft client = Minecraft.getInstance();
    private static final ModConfig config = TpUtils.getConfig();

    private Teleports() {
    }

    public static void tpThrough() {
        HitResult hit = RaycastUtil.forwardFromPlayer(config.getTpThroughRange());

        if (hit.getType() != HitResult.Type.BLOCK) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.noObstacleFound"));
            return;
        }

        BlockPos pos = BlockCheck.findOpenSpotForwards(hit, config.getTpThroughRange());
        if (pos == null) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.tooMuchWall"));
            return;
        }

        tpToBlockPos(pos);
    }

    public static void tpOnTop(HitResult hit) {
        if (hit == null) {
            hit = RaycastUtil.forwardFromPlayer(config.getTpOnTopRange());
        }

        if (hit.getType() != HitResult.Type.BLOCK) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.noBlockFound"));
            return;
        }

        BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
        BlockPos tpPos = BlockCheck.findTopOpenSpot(hitPos);
        tpToBlockPos(tpPos);
    }

    public static void tpForward() {
        HitResult hit = RaycastUtil.forwardFromPlayer(config.getTpForwardRange());
        double distance = client.getCameraEntity().getEyePosition().distanceTo(hit.getLocation());
        BlockPos pos = BlockCheck.findOpenSpotBackwards(hit, distance);

        if (pos == null) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.obstructed"));
            return;
        }

        BlockPos playerPos = BlockPos.containing(client.player.position());
        if (pos.equals(playerPos)) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.cantMoveForward"));
            return;
        }

        tpToBlockPos(pos);
    }

    public static void tpGround(HitResult hit) {
        if (hit == null) {
            hit = RaycastUtil.downwardFromPlayer(config.isLavaAllowed());
        }

        if (hit.getLocation().y() == client.player.position().y()) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.alreadyGrounded"));
            return;
        }

        if (hit.getLocation().y() == client.level.getMinY()) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.noGroundFound"));
            return;
        }

        tpToExactPos(hit.getLocation());
    }

    public static void tpUp() {
        HitResult hit = RaycastUtil.upwardFromPlayer();
        if (hit.getLocation().y >= client.level.getHeight()) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.nothingAbove"));
            return;
        }

        tpOnTop(hit);
    }

    public static void tpDown() {
        HitResult hit = RaycastUtil.downwardFromPlayer(false);

        if (hit.getType() != HitResult.Type.BLOCK) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.nothingBelow"));
            return;
        }

        BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
        BlockPos bottomPos = BlockCheck.findBottomOpenSpot(hitPos);
        if (bottomPos == null || bottomPos.getY() <= client.level.getMinY()) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.noOpenSpaceBelow"));
            return;
        }

        hit = RaycastUtil.downwardFromPos(bottomPos, false);
        tpGround(hit);
    }

    public static void tpBack() {
        Vec3 coordinates = config.getPreviousLocation();
        if (coordinates == null) {
            InfoProvider.sendError(Component.translatable("text.tputils.message.noPreviousLocation"));
            return;
        }

        tpToExactPos(coordinates);
    }

    public static void chunkTp(int x, int y, int z) {
        tpToExactPos(new Vec3(x, y, z).scale(16).add(8.0));
    }

    private static void tpToBlockPos(BlockPos pos) {
        String tpMethod = config.getTpMethod(client.hasSingleplayerServer());
        config.setPreviousLocation(client.player.position());
        client.getConnection().sendCommand(tpMethod + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    private static void tpToExactPos(Vec3 pos) {
        String tpMethod = config.getTpMethod(client.hasSingleplayerServer());

        if (tpMethod.equals("tp") || tpMethod.equals("minecraft:tp")) {
            config.setPreviousLocation(client.player.position());
            client.getConnection().sendCommand(tpMethod + " " + pos.x() + " " + pos.y() + " " + pos.z());
        }
        else {
            BlockPos blockPos = BlockPos.containing(pos.x(), Math.ceil(pos.y()), pos.z());
            tpToBlockPos(blockPos);
        }
    }
}
