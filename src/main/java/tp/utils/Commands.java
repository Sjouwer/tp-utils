package tp.utils;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import tp.utils.methods.*;
import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.*;

public class Commands implements ClientCommandPlugin {
    private final ChunkTp chunkTp = new ChunkTp();
    private final TpThrough tpThrough = new TpThrough();
    private final TpOnTop tpOnTop = new TpOnTop();
    private final TpForward tpForward = new TpForward();
    private final TpBack tpBack = new TpBack();

    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("tpu")
                .then(ArgumentBuilders.literal("chunk")
                        .then(ArgumentBuilders.argument("x", IntegerArgumentType.integer())
                                .then(ArgumentBuilders.argument("z", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                                    chunkTp.chunkTp(IntegerArgumentType.getInteger(ctx, "x"), IntegerArgumentType.getInteger(ctx, "z"));
                                                    return 1;
                                                }
                                        )))));

        dispatcher.register(ArgumentBuilders.literal("tpu")
                .then(ArgumentBuilders.literal("through")
                        .executes(ctx -> {
                                    tpThrough.tpThrough();
                                    return 1;
                                }
                        )));

        dispatcher.register(ArgumentBuilders.literal("tpu")
                .then(ArgumentBuilders.literal("top")
                        .executes(ctx -> {
                                    tpOnTop.tpOnTop();
                                    return 1;
                                }
                        )));

        dispatcher.register(ArgumentBuilders.literal("tpu")
                .then(ArgumentBuilders.literal("forward")
                        .executes(ctx -> {
                                    tpForward.tpForward();
                                    return 1;
                                }
                        )));

        dispatcher.register(ArgumentBuilders.literal("tpu")
                .then(ArgumentBuilders.literal("back")
                        .executes(ctx -> {
                                    tpBack.tpBack();
                                    return 1;
                                }
                        )));
    }
}