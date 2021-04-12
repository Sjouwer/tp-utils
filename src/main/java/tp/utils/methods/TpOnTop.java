package tp.utils.methods;

import tp.utils.config.ModConfig;
import tp.utils.util.CollisionCheck;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static net.minecraft.text.Style.EMPTY;

public class TpOnTop {
    private ModConfig config;
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    private double distance;
    private boolean doesWallExist;
    private HitResult hit;
    private Vec3d vector;
    private Vec3d blockHit;
    private BlockPos blockPos;

    public TpOnTop()
    {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpOnTop() {
        doesWallExist = false;
        distance = 0;

        hit = minecraft.cameraEntity.raycast(config.tpOnTopRange(), minecraft.getTickDelta(), false);
        vector = minecraft.player.getRotationVec(minecraft.getTickDelta());

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = CollisionCheck.canCollide(blockPos);

        while (!doesWallExist && distance < config.tpOnTopRange()){
            recastRay();
        }

        if (doesWallExist)
        {
            for (int j = 1; j < 257; j++) {
                boolean isBottomBlockFree = !CollisionCheck.canCollide(blockPos.add(0, j,0));
                boolean isTopBlockFree = !CollisionCheck.canCollide(new BlockPos(blockPos.add(0,j + 1,0)));

                if (isBottomBlockFree && ( config.isCrawlingAllowed() || isTopBlockFree )) {
                    config.setPreviousLocation(minecraft.player.getPos());
                    minecraft.player.sendChatMessage(config.tpMethod() + " "  + blockPos.getX() + " " + (blockPos.getY() + j) + " " + blockPos.getZ());
                    return;
                }
            }
        }

        BaseText message = new LiteralText("No block in sight (or too far)!");
        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    //If the ray cast hits a non solid block like grass, it'll redo the ray cast past the grass block.
    private void recastRay() {
        distance = minecraft.player.getPos().distanceTo(hit.getPos());
        Vec3d rayStart = hit.getPos().add(vector.multiply(0.05));
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpOnTopRange() - distance));
        hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = CollisionCheck.canCollide(blockPos);
    }
}
