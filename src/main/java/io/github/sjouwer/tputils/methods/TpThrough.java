package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.text.TranslatableText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static net.minecraft.text.Style.EMPTY;

public class TpThrough {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpThrough() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpThrough() {
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = minecraft.cameraEntity.getEyePos();
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpThroughRange()));
        HitResult hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        BaseText message;
        if (hit.getPos() != rayEnd) {
            BlockPos pos = findOpenSpot(hit, vector);
            if (pos != null) {
                Teleport.teleportPlayer(pos, config);
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

    //If the ray cast hits an obstacle it'll attempt and find an open spot behind the obstacle.
    private BlockPos findOpenSpot(HitResult hit, Vec3d vector) {
        for (int i = 1; i < config.tpThroughRange() * 8; i++) {
            Vec3d blockHit = hit.getPos().add(vector.multiply(0.125 * i));
            BlockPos blockPos = new BlockPos(blockHit);

            boolean foundObstacle = BlockCheck.canCollide(blockPos, config);
            boolean isLoaded = minecraft.world.getChunkManager().isChunkLoaded(blockPos.getX() / 16, blockPos.getZ() / 16);

            if (isLoaded && !foundObstacle && (!config.isBedrockLimitSet() || blockPos.getY() > minecraft.world.getBottomY())) {
                boolean isBottomBlockFree = !BlockCheck.canCollide(blockPos.add(0, -1, 0), config);
                boolean isTopBlockFree = !BlockCheck.canCollide(blockPos.add(0,1,0), config);

                if (isBottomBlockFree) {
                    return new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ());
                }
                else if (isTopBlockFree || config.isCrawlingAllowed()) {
                    return blockPos;
                }
            }
        }
        return null;
    }
}
