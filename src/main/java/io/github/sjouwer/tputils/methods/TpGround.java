package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static net.minecraft.text.Style.EMPTY;

public class TpGround {
    private final ModConfig config;
    private final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpGround() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpGround() {
        HitResult hit = castRay();

        BaseText message;
        if (hit.getPos().getY() == minecraft.player.getPos().getY()) {
            message = new TranslatableText("text.tp_utils.message.alreadyGrounded");
        }
        else if (hit.getPos().getY() == 0) {
            message = new TranslatableText("text.tp_utils.message.noGroundFound");
        }
        else {
            Teleport.toExactPos(hit.getPos(), config);
            return;
        }

        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    private HitResult castRay() {
        BlockPos pos = new BlockPos(minecraft.cameraEntity.getCameraPosVec(minecraft.getTickDelta()));
        double x = pos.getX() + 0.5;
        double y = Math.min(pos.getY(), minecraft.world.getHeight() + 1);
        double z = pos.getZ() + 0.5;

        Vec3d rayStart = new Vec3d(x, y, z);
        Vec3d rayEnd = new Vec3d(x, 0, z);

        HitResult hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        boolean hitLava = BlockCheck.isLava(new BlockPos(hit.getPos()));
        if (hitLava && !config.isLavaAllowed()) {
            hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, minecraft.player));
        }

        return hit;
    }
}
