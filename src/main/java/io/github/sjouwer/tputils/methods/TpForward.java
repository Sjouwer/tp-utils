package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
import io.github.sjouwer.tputils.util.BlockCheck;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.text.TranslatableText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.text.Style.EMPTY;

public class TpForward {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpForward() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpForward() {
        HitResult hit = minecraft.cameraEntity.raycast(config.tpForwardRange(), minecraft.getTickDelta(), false);
        Vec3d vector = minecraft.player.getRotationVec(minecraft.getTickDelta());

        BlockPos blockPos = new BlockPos(hit.getPos().subtract(vector.multiply(0.95)));
        double x = blockPos.getX() + 0.5;
        double y = blockPos.getY();
        double z = blockPos.getZ() + 0.5;

        boolean isLoaded = minecraft.world.getChunkManager().isChunkLoaded(blockPos.getX() / 16, blockPos.getZ() / 16);

        if (isLoaded) {
            config.setPreviousLocation(minecraft.player.getPos());

            boolean isBottomBlockFree = !BlockCheck.canCollide(blockPos.add(0, -1, 0), config);
            if (isBottomBlockFree) {
                minecraft.player.sendChatMessage(config.tpMethod() + " " + x + " " + (y - 1) + " " + z);
            }
            else {
                minecraft.player.sendChatMessage(config.tpMethod() + " " + x + " " + y + " " + z);
            }

            return;
        }

        BaseText message = new TranslatableText("text.tp_utils.message.chunkNotLoaded");
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }
}
