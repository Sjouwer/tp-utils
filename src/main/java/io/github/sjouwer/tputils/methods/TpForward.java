package io.github.sjouwer.tputils.methods;

import io.github.sjouwer.tputils.config.ModConfig;
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

        Vec3d blockHit = hit.getPos().subtract(vector.multiply(0.05));
        BlockPos blockPos = new BlockPos(blockHit);

        boolean isLoaded = minecraft.world.getChunkManager().isChunkLoaded(blockPos.getX() / 16, blockPos.getZ() / 16);

        if (isLoaded) {
            config.setPreviousLocation(minecraft.player.getPos());
            minecraft.player.sendChatMessage(config.tpMethod() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            return;
        }

        BaseText message = new TranslatableText("text.tp_utils.message.chunkNotLoaded");
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }
}
