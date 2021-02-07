package tp.utils.methods;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import tp.utils.config.ModConfig;
import tp.utils.util.CollisionCheck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;

public class TpThrough {
    private ModConfig config;
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    private Style style = new Style();
    private double distance;
    private boolean doesWallExist;
    private HitResult hit;
    private Vec3d vector;
    private Vec3d blockHit;
    private BlockPos blockPos;

    public TpThrough()
    {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpThrough() {
        doesWallExist = false;
        distance = 0;

        hit = minecraft.cameraEntity.rayTrace(config.tpThroughRange(), minecraft.getTickDelta(), false);
        vector = minecraft.player.getRotationVec(minecraft.getTickDelta());

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = CollisionCheck.canCollide(blockPos);

        while (!doesWallExist && distance < config.tpThroughRange()){
            recastRay();
        }

        BaseText message;
        if(doesWallExist) {
            if (wallCheck()) {
                return;
            }
            message = new LiteralText("Too much wall. You shall not pass!");
        }
        else {
            message = new LiteralText("Nothing to pass through!");
        }

        style.setColor(Formatting.DARK_RED);
        message.setStyle(style);
        minecraft.player.sendMessage(message);
    }

    //If the ray cast hits a non solid block like grass, it'll redo the ray cast past the grass block.
    private void recastRay() {
        distance = minecraft.player.getPos().distanceTo(hit.getPos());
        Vec3d rayStart = hit.getPos().add(vector);
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpThroughRange() - distance));
        hit = minecraft.world.rayTrace(new RayTraceContext(rayStart, rayEnd, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, minecraft.player));

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = CollisionCheck.canCollide(blockPos);
    }

    //If the ray cast hits a wall it'll attempt and find a open spot behind the wall.
    private boolean wallCheck() {
        for (int i = 0; i < config.tpThroughRange() * 8; i++) {
            blockHit = blockHit.add(vector.multiply(0.125 * i));
            blockPos = new BlockPos(blockHit);

            doesWallExist = CollisionCheck.canCollide(blockPos);

            if (!doesWallExist && blockPos.getY() > 0 && minecraft.world.getChunkManager().isChunkLoaded(blockPos.getX() / 16, blockPos.getZ() / 16)) {
                if (!CollisionCheck.canCollide(blockPos) && !CollisionCheck.canCollide(blockPos.add(0, -1, 0))) {
                    config.setPreviousLocation(minecraft.player.getPos());
                    minecraft.player.sendChatMessage(config.tpMethod() + " "  + blockPos.getX() + " " + (blockPos.getY() - 1) + " " + blockPos.getZ());
                    return true;
                } else if (!CollisionCheck.canCollide(blockPos) && !CollisionCheck.canCollide(blockPos.add(0,1,0))) {
                    config.setPreviousLocation(minecraft.player.getPos());
                    minecraft.player.sendChatMessage(config.tpMethod() + " "  + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
                    return true;
                }
            }
        }
        return false;
    }
}
