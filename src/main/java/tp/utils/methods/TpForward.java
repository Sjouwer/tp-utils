package tp.utils.methods;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import tp.utils.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TpForward {
    private ModConfig config;
    private MinecraftClient minecraft = MinecraftClient.getInstance();
    private Style style = new Style();

    public TpForward() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpForward() {
        HitResult hit = minecraft.cameraEntity.rayTrace(config.tpForwardRange(), minecraft.getTickDelta(), false);
        Vec3d vector = minecraft.player.getRotationVec(minecraft.getTickDelta());

        Vec3d blockHit = hit.getPos().subtract(vector.multiply(0.05));
        BlockPos blockPos = new BlockPos(blockHit);


        if (minecraft.world.getChunkManager().isChunkLoaded(blockPos.getX() / 16, blockPos.getZ() / 16))
        {
            config.setPreviousLocation(minecraft.player.getPos());
            minecraft.player.sendChatMessage(config.tpMethod() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            return;
        }

        BaseText message = new LiteralText("Chunk over there hasn't loaded yet (or render distance too low)!");
        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }
}
