package tp.utils.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public final class BlockCheck {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private BlockCheck() {
    }

    public static boolean canCollide(BlockPos pos, boolean lavaCheck) {
        BlockState state = minecraft.world.getBlockState(pos);
        if (lavaCheck && isLava(pos)) {
            return true;
        }
        return state.getMaterial().isSolid();
    }

    public static boolean isLava(BlockPos pos) {
        BlockState state = minecraft.world.getBlockState(pos);
        return state.getMaterial() == Material.LAVA;
    }
}
