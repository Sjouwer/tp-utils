package io.github.sjouwer.tputils;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import io.github.sjouwer.tputils.util.Raycast;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.text.Style.EMPTY;

public class Teleports {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public Teleports() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpThrough() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpThroughRange());

        BaseText message;
        if (hit != null) {
            BlockPos pos = BlockCheck.findOpenSpot(hit, config.tpThroughRange(), 1, config);
            if (pos != null) {
                tpToBlockPos(pos, config);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.tooMuchWall");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.noObstacleFound");
        }

        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    public void tpOnTop() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpOnTopRange());
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = ((BlockHitResult)hit).getBlockPos();
            BlockPos tpPos = BlockCheck.findTopSpot(hitPos, config.isLavaAllowed(), config.isCrawlingAllowed());
            if (tpPos != null) {
                tpToBlockPos(tpPos, config);
                return;
            }
        }

        BaseText message = new TranslatableText("text.tp_utils.message.noBlockFound");
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    public void tpForward() {
        HitResult hit = Raycast.forwardFromPlayer(config.tpForwardRange());
        double distance = minecraft.cameraEntity.getEyePos().distanceTo(hit.getPos());
        BlockPos pos = BlockCheck.findOpenSpot(hit, distance, -1, config);

        BaseText message;
        if (pos != null) {
            BlockPos playerPos = new BlockPos(minecraft.player.getPos());
            if (!pos.equals(playerPos)) {
                tpToBlockPos(pos, config);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.cantMoveForward");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.obstructed");
        }

        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    public void tpGround() {
        HitResult hit = Raycast.downwardFromPlayer(config.isLavaAllowed());

        BaseText message;
        if (hit.getPos().getY() == minecraft.player.getPos().getY()) {
            message = new TranslatableText("text.tp_utils.message.alreadyGrounded");
        }
        else if (hit.getPos().getY() == 0) {
            message = new TranslatableText("text.tp_utils.message.noGroundFound");
        }
        else {
            tpToExactPos(hit.getPos(), config);
            return;
        }

        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            tpToExactPos(coordinates, config);
        }
        else {
            BaseText message = new TranslatableText("text.tp_utils.message.noPreviousLocation");
            message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
            minecraft.player.sendMessage(message, false);
        }
    }

    public void chunkTp(int x, int y, int z) {
        double xPos = x * 16 + 8.0;
        double yPos = y * 16 + 8.0;
        double zPos = z * 16 + 8.0;

        tpToExactPos(new Vec3d(xPos, yPos, zPos), config);
    }

    private static void tpToBlockPos(BlockPos pos, ModConfig config){
        config.setPreviousLocation(minecraft.player.getPos());
        minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    private static void tpToExactPos(Vec3d pos, ModConfig config){
        if (config.tpMethod().equals("/tp") || config.tpMethod().equals("/minecraft:tp")) {
            config.setPreviousLocation(minecraft.player.getPos());
            minecraft.player.sendChatMessage(config.tpMethod() + " "  + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        }
        else {
            BlockPos blockPos = new BlockPos(pos.getX(), Math.ceil(pos.getY()), pos.getZ());
            tpToBlockPos(blockPos, config);
        }
    }
}
