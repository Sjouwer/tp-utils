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
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = minecraft.cameraEntity.getEyePos();
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpOnTopRange()));
        HitResult hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        BlockPos blockPos = new BlockPos(hit.getPos().add(vector.multiply(0.05)));

        if (hit.getPos() != rayEnd) {
            for (int j = 1; j < minecraft.world.getHeight() + 1; j++) {
                boolean isBottomBlockFree = !BlockCheck.canCollide(blockPos.add(0, j,0), config);
                boolean isTopBlockFree = !BlockCheck.canCollide(new BlockPos(blockPos.add(0,j + 1,0)), config);

                if (isBottomBlockFree && ( config.isCrawlingAllowed() || isTopBlockFree )) {
                    BlockPos pos = new BlockPos(blockPos.getX(), blockPos.getY() + j, blockPos.getZ());
                    Teleport.teleportPlayer(pos, config);
                    return;
                }
            }
        }

        BaseText message = new TranslatableText("text.tp_utils.message.noBlockFound");
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }
}
