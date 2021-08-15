package io.github.sjouwer.tputils;

import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import io.github.sjouwer.tputils.methods.*;
import com.mojang.brigadier.CommandDispatcher;

import static io.github.cottonmc.clientcommands.ArgumentBuilders.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.*;

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
                        .then(argument("x", integer())
                                .then(argument("z", integer())
                                        .executes(ctx -> {
                                                    chunkTp.chunkTp(getInteger(ctx, "x"),
                                                                    6,
                                                                    getInteger(ctx, "z"));
                                                    return 1;
                                                }
                                        )))

                        .then(argument("x", integer())
                                .then(argument("y", integer())
                                    .then(argument("z", integer())
                                            .executes(ctx -> {
                                                        chunkTp.chunkTp(getInteger(ctx, "x"),
                                                                        getInteger(ctx, "y"),
                                                                        getInteger(ctx, "z"));
                                                        return 1;
                                                    }
                                            )))))

                .then(literal("through")
                        .executes(ctx -> {
                                    tpThrough.tpThrough();
                                    return 1;
                                }
                        ))

                .then(literal("top")
                        .executes(ctx -> {
                                    tpOnTop.tpOnTop();
                                    return 1;
                                }
                        ))

                .then(literal("forward")
                        .executes(ctx -> {
                                    tpForward.tpForward();
                                    return 1;
                                }
                        ))

                .then(literal("back")
                        .executes(ctx -> {
                                    tpBack.tpBack();
                                    return 1;
                                }
                        ))

                .then(literal("ground")
                        .executes(ctx -> {
                                    tpGround.tpGround();
                                    return 1;
                                }
                        )));
    }
}