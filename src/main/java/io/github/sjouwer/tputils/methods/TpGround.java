package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.*;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;

public class TpGround {
    private final ModConfig config;
    private final MinecraftClient minecraft = MinecraftClient.getInstance();

    private final Style style = new Style();

    public TpGround() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpGround() {
        Vec3d hit = castRay();

        BaseText message;
        if (hit.getY() == minecraft.player.getPos().getY()) {
            message = new TranslatableText("text.tp_utils.message.alreadyGrounded");
        }
        else if (hit.getY() == 0) {
            message = new TranslatableText("text.tp_utils.message.noGroundFound");
        }
        else {
            Teleport.toExactPos(hit, config);
            return;
        }

        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    private Vec3d castRay() {
        BlockPos pos = new BlockPos(minecraft.cameraEntity.getCameraPosVec(minecraft.getTickDelta()));
        double x = pos.getX() + 0.5;
        double y = Math.min(pos.getY(), minecraft.world.getHeight() + 1);
        double z = pos.getZ() + 0.5;

        Vec3d rayStart = new Vec3d(x, y, z);
        Vec3d rayEnd = new Vec3d(x, 0, z);

        HitResult hit = minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, minecraft.player));
        Vec3d hitPos = hit.getPos();

        boolean hitLava = BlockCheck.isLava(new BlockPos(hit.getPos()));
        if (hitLava && !config.isLavaAllowed()) {
            hit = minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.ANY, minecraft.player));
            hitPos = new Vec3d(hit.getPos().getX(), Math.ceil(hit.getPos().getY()), hit.getPos().getZ());
        }

        return hitPos;
    }
}
