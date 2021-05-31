package tp.utils.methods;

import me.shedaniel.autoconfig.AutoConfig;
import tp.utils.config.ModConfig;
import tp.utils.util.BlockCheck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static net.minecraft.text.Style.EMPTY;

public class TpThrough {
    private final ModConfig config;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private double distance;
    private boolean doesWallExist;
    private HitResult hit;
    private Vec3d vector;
    private Vec3d blockHit;
    private BlockPos blockPos;

    public TpThrough() {
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public void tpThrough() {
        doesWallExist = false;
        distance = 0;

        hit = minecraft.cameraEntity.raycast(config.tpThroughRange(), minecraft.getTickDelta(), false);
        vector = minecraft.player.getRotationVec(minecraft.getTickDelta());

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = BlockCheck.canCollide(blockPos, !config.isLavaAllowed());

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

        message.setStyle(EMPTY.withColor(Formatting.DARK_RED));
        minecraft.player.sendMessage(message, false);
    }

    //If the ray cast hits a non solid block like grass, it'll redo the ray cast past the grass block.
    private void recastRay() {
        distance = minecraft.player.getPos().distanceTo(hit.getPos());
        Vec3d rayStart = hit.getPos().add(vector.multiply(0.05));
        Vec3d rayEnd = rayStart.add(vector.multiply(config.tpThroughRange() - distance));
        hit = minecraft.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, minecraft.player));

        blockHit = hit.getPos().add(vector.multiply(0.05));
        blockPos = new BlockPos(blockHit);

        doesWallExist = BlockCheck.canCollide(blockPos, !config.isLavaAllowed());
    }

    //If the ray cast hits a wall it'll attempt and find a open spot behind the wall.
    private boolean wallCheck() {
        for (int i = 0; i < config.tpThroughRange() * 8; i++) {
            blockHit = blockHit.add(vector.multiply(0.125));
            blockPos = new BlockPos(blockHit);

            doesWallExist = BlockCheck.canCollide(blockPos, !config.isLavaAllowed());
            boolean isLoaded = minecraft.world.getChunkManager().isChunkLoaded(blockPos.getX() / 16, blockPos.getZ() / 16);

            if (isLoaded && !doesWallExist && (!config.isBedrockLimitSet() || blockPos.getY() > minecraft.world.getBottomY())) {
                boolean isBottomBlockFree = !BlockCheck.canCollide(blockPos.add(0, -1, 0), !config.isLavaAllowed());
                boolean isTopBlockFree = !BlockCheck.canCollide(blockPos.add(0,1,0), !config.isLavaAllowed());

                if (isBottomBlockFree) {
                    config.setPreviousLocation(minecraft.player.getPos());
                    minecraft.player.sendChatMessage(config.tpMethod() + " "  + blockPos.getX() + " " + (blockPos.getY() - 1) + " " + blockPos.getZ());
                    return true;
                }
                else if (isTopBlockFree || config.isCrawlingAllowed()) {
                    config.setPreviousLocation(minecraft.player.getPos());
                    minecraft.player.sendChatMessage(config.tpMethod() + " "  + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
                    return true;
                }
            }
        }
        return false;
    }
}
