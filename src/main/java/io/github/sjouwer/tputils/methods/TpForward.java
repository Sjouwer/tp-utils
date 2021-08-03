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

public class TpForward {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpForward() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpForward() {
        BlockPos pos = findOpenSpot(castRay());

        BaseText message;
        if (pos != null) {
            BlockPos playerPos = new BlockPos(minecraft.player.getPos());
            if (!pos.equals(playerPos)) {
                Teleport.teleportPlayer(pos, config);
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

    private HitResult castRay() {
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = minecraft.cameraEntity.getEyePos();
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpForwardRange()));
        return minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));
    }

    private BlockPos findOpenSpot(HitResult hit) {
        double distance = minecraft.cameraEntity.getEyePos().distanceTo(hit.getPos());

        for (int i = 0; i < distance * 8; i++) {
            Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
            BlockPos pos = new BlockPos(hit.getPos().add(vector.multiply(-0.125 * i)));

            boolean foundObstacle = BlockCheck.canCollide(pos, config);
            boolean isLoaded = minecraft.world.getChunkManager().isChunkLoaded(pos.getX() / 16, pos.getZ() / 16);

            if (isLoaded && !foundObstacle && (!config.isBedrockLimitSet() || pos.getY() > minecraft.world.getBottomY())) {
                boolean isBottomBlockFree = !BlockCheck.canCollide(pos.add(0, -1, 0), config);
                boolean isTopBlockFree = !BlockCheck.canCollide(pos.add(0,1,0), config);

                if (isBottomBlockFree) {
                    return new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
                }
                else if (isTopBlockFree || config.isCrawlingAllowed()) {
                    return pos;
                }
            }
        }
        return null;
    }
}
