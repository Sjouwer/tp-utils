package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;

public class TpOnTop {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private final Style style = new Style();
    private double distance;
    private boolean doesWallExist;
    private HitResult hit;
    private Vec3d vector;
    private Vec3d blockHit;
    private BlockPos blockPos;

    public TpOnTop() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpOnTop() {
        doesWallExist = false;
        distance = 0;

        hit = minecraft.cameraEntity.rayTrace(config.tpOnTopRange(), minecraft.getTickDelta(), false);
        vector = minecraft.player.getRotationVec(minecraft.getTickDelta());

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = BlockCheck.canCollide(blockPos, !config.isLavaAllowed());

        while (!doesWallExist && distance < config.tpOnTopRange()){
            recastRay();
        }

        if (doesWallExist) {
            for (int j = 1; j < 257; j++) {
                boolean isBottomBlockFree = !BlockCheck.canCollide(blockPos.add(0, j,0), !config.isLavaAllowed());
                boolean isTopBlockFree = !BlockCheck.canCollide(new BlockPos(blockPos.add(0,j + 1,0)), !config.isLavaAllowed());

                if (isBottomBlockFree && ( config.isCrawlingAllowed() || isTopBlockFree )) {
                    config.setPreviousLocation(minecraft.player.getPos());config.setPreviousLocation(minecraft.player.getPos());
                    minecraft.player.sendChatMessage(config.tpMethod() + " "  + blockPos.getX() + " " + (blockPos.getY() + j) + " " + blockPos.getZ());
                    return;
                }
            }
        }

        BaseText message = new LiteralText("No block in sight (or too far)!");
        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    //If the ray cast hits a non solid block like grass, it'll redo the ray cast past the grass block.
    private void recastRay() {
        distance = minecraft.player.getPos().distanceTo(hit.getPos());
        Vec3d rayStart = hit.getPos().add(vector.multiply(0.05));
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpOnTopRange() - distance));
        hit = minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, minecraft.player));

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = BlockCheck.canCollide(blockPos, !config.isLavaAllowed());
    }
}
