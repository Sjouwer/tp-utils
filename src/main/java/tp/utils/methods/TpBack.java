package tp.utils.methods;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.BlockPos;
import tp.utils.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.text.Style.EMPTY;

public class TpBack {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public TpBack() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            config.setPreviousLocation(minecraft.player.getPos());
            BlockPos blockPos = new BlockPos(coordinates);
            minecraft.player.sendChatMessage(config.tpMethod() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
        } else {
            BaseText message = new LiteralText("Unable to find a previous location");
            message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
            minecraft.player.sendMessage(message, false);
        }
    }
}
