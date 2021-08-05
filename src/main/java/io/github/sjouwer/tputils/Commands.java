package io.github.sjouwer.tputils;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import io.github.sjouwer.tputils.methods.*;
import com.mojang.brigadier.CommandDispatcher;

import static io.github.cottonmc.clientcommands.ArgumentBuilders.*;

public class Commands implements ClientCommandPlugin {
    private final ChunkTp chunkTp = new ChunkTp();
    private final TpThrough tpThrough = new TpThrough();
    private final TpOnTop tpOnTop = new TpOnTop();
    private final TpForward tpForward = new TpForward();
    private final TpBack tpBack = new TpBack();
    private final TpGround tpGround = new TpGround();

    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(literal("tpu")
                .then(literal("chunk")
                        .then(argument("x", IntegerArgumentType.integer())
                                .then(argument("z", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                                    chunkTp.chunkTp(IntegerArgumentType.getInteger(ctx, "x"),
                                                                    6,
                                                                    IntegerArgumentType.getInteger(ctx, "z"));
                                                    return 1;
                                                }
                                        )))));

        dispatcher.register(literal("tpu")
                .then(literal("chunk")
                        .then(argument("x", IntegerArgumentType.integer())
                                .then(argument("y", IntegerArgumentType.integer())
                                    .then(argument("z", IntegerArgumentType.integer())
                                            .executes(ctx -> {
                                                        chunkTp.chunkTp(IntegerArgumentType.getInteger(ctx, "x"),
                                                                        IntegerArgumentType.getInteger(ctx, "y"),
                                                                        IntegerArgumentType.getInteger(ctx, "z"));
                                                        return 1;
                                                    }
                                            ))))));

        dispatcher.register(literal("tpu")
                .then(literal("through")
                        .executes(ctx -> {
                                    tpThrough.tpThrough();
                                    return 1;
                                }
                        )));

        dispatcher.register(literal("tpu")
                .then(literal("top")
                        .executes(ctx -> {
                                    tpOnTop.tpOnTop();
                                    return 1;
                                }
                        )));

        dispatcher.register(literal("tpu")
                .then(literal("forward")
                        .executes(ctx -> {
                                    tpForward.tpForward();
                                    return 1;
                                }
                        )));

        dispatcher.register(literal("tpu")
                .then(literal("back")
                        .executes(ctx -> {
                                    tpBack.tpBack();
                                    return 1;
                                }
                        )));
        dispatcher.register(literal("tpu")
                .then(literal("ground")
                        .executes(ctx -> {
                                    tpGround.tpGround();
                                    return 1;
                                }
                        )));
    }
}