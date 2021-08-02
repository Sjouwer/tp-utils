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

public class TpOnTop {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpOnTop() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpOnTop() {
        BlockPos hit = castRay();

        if (hit != null) {
            BlockPos pos = findOpenSpot(hit);
            if (pos != null) {
                Teleport.teleportPlayer(pos, config);
                return;
            }
        }

        BaseText message = new TranslatableText("text.tp_utils.message.noBlockFound");
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    private BlockPos castRay() {
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = minecraft.cameraEntity.getEyePos();
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpOnTopRange()));
        HitResult hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        if (hit.getPos() != rayEnd) {
            return new BlockPos(hit.getPos().add(vector.multiply(0.05)));
        }

        return null;
    }

    private BlockPos findOpenSpot(BlockPos pos) {
        for (int j = 1; j < minecraft.world.getHeight() + 1; j++) {
            boolean isBottomBlockFree = !BlockCheck.canCollide(pos.add(0, j,0), config);
            boolean isTopBlockFree = !BlockCheck.canCollide(new BlockPos(pos.add(0,j + 1,0)), config);

            if (isBottomBlockFree && ( config.isCrawlingAllowed() || isTopBlockFree )) {
                return new BlockPos(pos.getX(), pos.getY() + j, pos.getZ());
            }
        }
        return null;
    }
}
