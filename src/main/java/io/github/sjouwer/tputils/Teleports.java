package io.github.sjouwer.tputils;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import io.github.sjouwer.tputils.util.InfoProvider;
import io.github.sjouwer.tputils.util.RaycastUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Teleports {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final ModConfig config = TpUtils.getConfig();

    private Teleports() {
    }

    public static void tpThrough() {
        HitResult hit = RaycastUtil.forwardFromPlayer(config.getTpThroughRange());

        if (hit.getType() != HitResult.Type.BLOCK) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.noObstacleFound"));
            return;
        }

        BlockPos pos = BlockCheck.findOpenSpotForwards(hit, config.getTpThroughRange());
        if (pos == null) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.tooMuchWall"));
            return;
        }

        tpToBlockPos(pos);
    }

    public static void tpOnTop(HitResult hit) {
        if (hit == null) {
            hit = RaycastUtil.forwardFromPlayer(config.getTpOnTopRange());
        }

        if (hit.getType() != HitResult.Type.BLOCK) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.noBlockFound"));
            return;
        }

        BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
        BlockPos tpPos = BlockCheck.findTopOpenSpot(hitPos);
        tpToBlockPos(tpPos);
    }

    public static void tpForward() {
        HitResult hit = RaycastUtil.forwardFromPlayer(config.getTpForwardRange());
        double distance = client.getCameraEntity().getEyePos().distanceTo(hit.getPos());
        BlockPos pos = BlockCheck.findOpenSpotBackwards(hit, distance);

        if (pos == null) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.obstructed"));
            return;
        }

        BlockPos playerPos = BlockPos.ofFloored(client.player.getEntityPos());
        if (pos.equals(playerPos)) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.cantMoveForward"));
            return;
        }

        tpToBlockPos(pos);
    }

    public static void tpGround(HitResult hit) {
        if (hit == null) {
            hit = RaycastUtil.downwardFromPlayer(config.isLavaAllowed());
        }

        if (hit.getPos().getY() == client.player.getEntityPos().getY()) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.alreadyGrounded"));
            return;
        }

        if (hit.getPos().getY() == client.world.getBottomY()) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.noGroundFound"));
            return;
        }

        tpToExactPos(hit.getPos());
    }

    public static void tpUp() {
        HitResult hit = RaycastUtil.upwardFromPlayer();
        if (hit.getPos().y >= client.world.getHeight()) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.nothingAbove"));
            return;
        }

        tpOnTop(hit);
    }

    public static void tpDown() {
        HitResult hit = RaycastUtil.downwardFromPlayer(false);

        if (hit.getType() != HitResult.Type.BLOCK) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.nothingBelow"));
            return;
        }

        BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
        BlockPos bottomPos = BlockCheck.findBottomOpenSpot(hitPos);
        if (bottomPos == null || bottomPos.getY() <= client.world.getBottomY()) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.noOpenSpaceBelow"));
            return;
        }

        hit = RaycastUtil.downwardFromPos(bottomPos, false);
        tpGround(hit);
    }

    public static void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates == null) {
            InfoProvider.sendError(Text.translatable("text.tputils.message.noPreviousLocation"));
            return;
        }

        tpToExactPos(coordinates);
    }

    public static void chunkTp(int x, int y, int z) {
        tpToExactPos(new Vec3d(x, y, z).multiply(16).add(8.0));
    }

    private static void tpToBlockPos(BlockPos pos) {
        String tpMethod = config.getTpMethod(client.isInSingleplayer());
        config.setPreviousLocation(client.player.getEntityPos());
        client.getNetworkHandler().sendChatCommand(tpMethod + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    private static void tpToExactPos(Vec3d pos) {
        String tpMethod = config.getTpMethod(client.isInSingleplayer());

        if (tpMethod.equals("tp") || tpMethod.equals("minecraft:tp")) {
            config.setPreviousLocation(client.player.getEntityPos());
            client.getNetworkHandler().sendChatCommand(tpMethod + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        }
        else {
            BlockPos blockPos = BlockPos.ofFloored(pos.getX(), Math.ceil(pos.getY()), pos.getZ());
            tpToBlockPos(blockPos);
        }
    }
}
