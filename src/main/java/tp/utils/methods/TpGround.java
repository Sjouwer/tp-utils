package tp.utils.methods;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import tp.utils.config.ModConfig;


public class TpGround {
    private final ModConfig config;
    private final MinecraftClient minecraft = MinecraftClient.getInstance();

    private final Style style = new Style();

    public TpGround() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpGround() {
        Vec3d rayStart = minecraft.player.getPos().add(0, 1, 0);
        if (rayStart.getY() > 257) {
            rayStart = new Vec3d(rayStart.getX(), 257, rayStart.getZ());
        }
        Vec3d rayEnd = new Vec3d(rayStart.getX(), 0, rayStart.getZ());
        HitResult hit = minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, minecraft.player));

        Vec3d blockHit = hit.getPos().add(0, 0.05, 0);
        BlockPos blockPos = new BlockPos(blockHit);

        BaseText message;
        if (blockPos.getY() == 0) {
            message = new LiteralText("There is no ground below you!");
        }
        else if (blockPos.getY() == minecraft.player.getPos().getY()) {
            message = new LiteralText("You're already grounded!");
        }
        else {
            config.setPreviousLocation(minecraft.player.getPos());
            minecraft.player.sendChatMessage(config.tpMethod() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            return;
        }

        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }
}