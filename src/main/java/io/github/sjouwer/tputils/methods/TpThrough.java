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

public class TpThrough {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private final Style style = new Style();

    public TpThrough() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpThrough() {
        HitResult hit = castRay();

        BaseText message;
        if (hit != null) {
            BlockPos pos = BlockCheck.findOpenSpot(hit, config.tpThroughRange(), 1, config);
            if (pos != null) {
                Teleport.toBlockPos(pos, config);
                return;
            }
            message = new TranslatableText("text.tp_utils.message.tooMuchWall");
        }
        else {
            message = new TranslatableText("text.tp_utils.message.noObstacleFound");
        }

        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    private HitResult castRay() {
        Vec3d vector = minecraft.cameraEntity.getRotationVec(minecraft.getTickDelta());
        Vec3d rayStart = minecraft.cameraEntity.getCameraPosVec(minecraft.getTickDelta());
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpThroughRange()));
        HitResult hit = minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, minecraft.player));

        if (hit.getPos() != rayEnd) {
            return hit;
        }

        return null;
    }
}
