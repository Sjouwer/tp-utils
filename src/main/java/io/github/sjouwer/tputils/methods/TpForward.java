package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.*;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;

public class TpForward {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private final Style style = new Style();

    public TpForward() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpForward() {
        HitResult hit = castRay();
        double distance = minecraft.cameraEntity.getCameraPosVec(minecraft.getTickDelta()).distanceTo(hit.getPos());
        BlockPos pos = BlockCheck.findOpenSpot(hit, distance, -1, config);

        BaseText message;
        if (pos != null) {
            BlockPos playerPos = new BlockPos(minecraft.player.getPos());
            if (!pos.equals(playerPos)) {
                Teleport.toBlockPos(pos, config);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.cantMoveForward");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.obstructed");
        }

        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    private HitResult castRay() {
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = minecraft.cameraEntity.getCameraPosVec(minecraft.getTickDelta());
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpForwardRange()));
        return minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, minecraft.player));
    }
}
