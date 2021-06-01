package tp.utils.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public final class CollisionCheck {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private CollisionCheck() {
    }

    public static boolean canCollide(BlockPos pos) {
        BlockState state = minecraft.world.getBlockState(pos);
        return state.getMaterial().isSolid();
    }
}
