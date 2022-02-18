package io.github.sjouwer.tputils;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import io.github.sjouwer.tputils.util.Chat;
import io.github.sjouwer.tputils.util.Raycast;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Teleports {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public Teleports() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpThrough() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpThroughRange());

        BaseText message;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = BlockCheck.findOpenSpotForwards(hit, config.tpThroughRange(), config);
            if (pos != null) {
                tpToBlockPos(pos);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.tooMuchWall");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.noObstacleFound");
        }

        Chat.sendError(message);
    }

    public void tpOnTop(HitResult hit) {
        if (hit == null) {
            hit = Raycast.forwardFromPlayer(config.tpOnTopRange());
        }

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
            BlockPos tpPos = BlockCheck.findTopOpenSpot(hitPos, config.isLavaAllowed(), config.isCrawlingAllowed());
            if (tpPos != null) {
                tpToBlockPos(tpPos);
                return;
            }
        }

        Chat.sendError(new TranslatableText("text.tp_utils.message.noBlockFound"));
    }

    public void tpForward() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpForwardRange());
        double distance = minecraft.cameraEntity.getEyePos().distanceTo(hit.getPos());
        BlockPos pos = BlockCheck.findOpenSpotBackwards(hit, distance, config);

        BaseText message;
        if (pos != null) {
            BlockPos playerPos = new BlockPos(minecraft.player.getPos());
            if (!pos.equals(playerPos)) {
                tpToBlockPos(pos);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.cantMoveForward");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.obstructed");
        }

        Chat.sendError(message);
    }

    public void tpGround(HitResult hit) {
        if (hit == null) {
            hit = Raycast.downwardFromPlayer(config.isLavaAllowed());
        }

        BaseText message;
        if (hit.getPos().getY() == minecraft.player.getPos().getY()) {
            message = new TranslatableText("text.tp_utils.message.alreadyGrounded");
        }
        else if (hit.getPos().getY() == minecraft.world.getBottomY()) {
            message = new TranslatableText("text.tp_utils.message.noGroundFound");
        }
        else {
            tpToExactPos(hit.getPos());
            return;
        }

        Chat.sendError(message);
    }

    public void tpUp() {
        HitResult hit = Raycast.upwardFromPlayer();
        if (hit.getPos().y < minecraft.world.getHeight()){
            tpOnTop(hit);
            return;
        }

        Chat.sendError(new TranslatableText("text.tp_utils.message.nothingAbove"));
    }

    public void tpDown() {
        HitResult hit = Raycast.downwardFromPlayer(false);

        BaseText message;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
            BlockPos bottomPos = BlockCheck.findBottomOpenSpot(hitPos, config.isLavaAllowed(), config.isCrawlingAllowed());
            if (bottomPos != null && bottomPos.getY() > minecraft.world.getBottomY()) {
                hit = Raycast.downwardFromPos(bottomPos, false);
                tpGround(hit);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.noOpenSpaceBelow");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.nothingBelow");
        }

        Chat.sendError(message);
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            tpToExactPos(coordinates);
        }
        else {
            Chat.sendError(new TranslatableText("text.tp_utils.message.noPreviousLocation"));
        }
    }

    public void chunkTp(int x, int y, int z) {
        double xPos = x * 16 + 8.0;
        double yPos = y * 16 + 8.0;
        double zPos = z * 16 + 8.0;

        tpToExactPos(new Vec3d(xPos, yPos, zPos));
    }

    private void tpToBlockPos(BlockPos pos){
        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    private void tpToExactPos(Vec3d pos){
        if (config.tpMethod().equals("/tp") || config.tpMethod().equals("/minecraft:tp")) {
            config.setPreviousLocation(minecraft.player.getPos());
            minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        }
        else {
            BlockPos blockPos = new BlockPos(pos.getX(), Math.ceil(pos.getY()), pos.getZ());
            tpToBlockPos(blockPos);
        }
    }
}
