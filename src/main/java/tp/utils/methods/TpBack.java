package tp.utils.methods;

import net.minecraft.text.Style;
import tp.utils.config.ModConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

public class TpBack {
    private ModConfig config;
    private MinecraftClient minecraft = MinecraftClient.getInstance();
    private Style style = new Style();

    public TpBack() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpBack() {
        Vec3d coordinates = config.getPreviousLocation();
        if (coordinates != null) {
            config.setPreviousLocation(minecraft.player.getPos());
            int x = (int)coordinates.x;
            int y = (int)coordinates.y;
            int z = (int)coordinates.z;
            minecraft.player.sendChatMessage(config.tpMethod() + " " + x + " " + y + " " + z);
        } else {
            BaseText message = new LiteralText("Unable to find a previous location");
            style.setColor(Formatting.DARK_RED);
            message.setStyle(style);
            minecraft.player.sendMessage(message);
        }
    }
}
