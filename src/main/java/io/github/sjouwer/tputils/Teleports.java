package io.github.sjouwer.tputils;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import io.github.sjouwer.tputils.util.Chat;
import io.github.sjouwer.tputils.util.Raycast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Teleports {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final ModConfig config = TpUtils.getConfig();

    public void tpThrough() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpThroughRange());

        TranslatableTextContent message;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = BlockCheck.findOpenSpotForwards(hit, config.tpThroughRange());
            if (pos != null) {
                tpToBlockPos(pos);
                return;
            }
            message = new TranslatableTextContent("text.tp_utils.message.tooMuchWall");
        }
        else {
            message = new TranslatableTextContent("text.tp_utils.message.noObstacleFound");
        }

        Chat.sendError(message);
    }

    public void tpOnTop(HitResult hit) {
        if (hit == null) {
            hit = Raycast.forwardFromPlayer(config.tpOnTopRange());
        }

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
            BlockPos tpPos = BlockCheck.findTopOpenSpot(hitPos);
            if (tpPos != null) {
                tpToBlockPos(tpPos);
                return;
            }
        }

        Chat.sendError(new TranslatableTextContent("text.tp_utils.message.noBlockFound"));
    }

    public void tpForward() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpForwardRange());
        double distance = client.cameraEntity.getEyePos().distanceTo(hit.getPos());
        BlockPos pos = BlockCheck.findOpenSpotBackwards(hit, distance);

        TranslatableTextContent message;
        if (pos != null) {
            BlockPos playerPos = new BlockPos(client.player.getPos());
            if (!pos.equals(playerPos)) {
                tpToBlockPos(pos);
                return;
            }
            message = new TranslatableTextContent("text.tp_utils.message.cantMoveForward");
        }
        else {
            message = new TranslatableTextContent("text.tp_utils.message.obstructed");
        }

        Chat.sendError(message);
    }

    public void tpGround(HitResult hit) {
        if (hit == null) {
            hit = Raycast.downwardFromPlayer(config.isLavaAllowed());
        }

        TranslatableTextContent message;
        if (hit.getPos().getY() == client.player.getPos().getY()) {
            message = new TranslatableTextContent("text.tp_utils.message.alreadyGrounded");
        }
        else if (hit.getPos().getY() == client.world.getBottomY()) {
            message = new TranslatableTextContent("text.tp_utils.message.noGroundFound");
        }
        else {
            tpToExactPos(hit.getPos());
            return;
        }

        Chat.sendError(message);
    }

    public void tpUp() {
        HitResult hit = Raycast.upwardFromPlayer();
        if (hit.getPos().y < client.world.getHeight()){
            tpOnTop(hit);
            return;
        }

        Chat.sendError(new TranslatableTextContent("text.tp_utils.message.nothingAbove"));
    }

    public void tpDown() {
        HitResult hit = Raycast.downwardFromPlayer(false);

        TranslatableTextContent message;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
            BlockPos bottomPos = BlockCheck.findBottomOpenSpot(hitPos);
            if (bottomPos != null && bottomPos.getY() > client.world.getBottomY()) {
                hit = Raycast.downwardFromPos(bottomPos, false);
                tpGround(hit);
                return;
            }
            message = new TranslatableTextContent("text.tp_utils.message.noOpenSpaceBelow");
        }
        else {
            message = new TranslatableTextContent("text.tp_utils.message.nothingBelow");
        }

        Chat.sendError(message);
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            tpToExactPos(coordinates);
        }
        else {
            Chat.sendError(new TranslatableTextContent("text.tp_utils.message.noPreviousLocation"));
        }
    }

    public void chunkTp(int x, int y, int z) {
        double xPos = x * 16 + 8.0;
        double yPos = y * 16 + 8.0;
        double zPos = z * 16 + 8.0;

        tpToExactPos(new Vec3d(xPos, yPos, zPos));
    }

    private void tpToBlockPos(BlockPos pos){
        config.setPreviousLocation(client.player.getPos());
        client.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    private void tpToExactPos(Vec3d pos){
        if (config.tpMethod().equals("/tp") || config.tpMethod().equals("/minecraft:tp")) {
            config.setPreviousLocation(client.player.getPos());
            client.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        }
        else {
            BlockPos blockPos = new BlockPos(pos.getX(), Math.ceil(pos.getY()), pos.getZ());
            tpToBlockPos(blockPos);
        }
    }
}
