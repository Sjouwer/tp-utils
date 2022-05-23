package io.github.sjouwer.tputils;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import com.mojang.brigadier.CommandDispatcher;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.*;

public class Commands {
    private final Teleports teleports = new Teleports();

    public void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("tpu")
                .then(literal("chunk")
                        .then(argument("x", integer())
                                .then(argument("z", integer())
                                        .executes(ctx -> {
                                                    teleports.chunkTp(getInteger(ctx, "x"),
                                                                    6,
                                                                    getInteger(ctx, "z"));
                                                    return 1;
                                                }
                                        ))

                                .then(argument("y", integer())
                                        .then(argument("z", integer())
                                                .executes(ctx -> {
                                                            teleports.chunkTp(getInteger(ctx, "x"),
                                                                            getInteger(ctx, "y"),
                                                                            getInteger(ctx, "z"));
                                                            return 1;
                                                        }
                                                )))))

                .then(literal("through")
                        .executes(ctx -> {
                                    teleports.tpThrough();
                                    return 1;
                                }
                        ))

                .then(literal("top")
                        .executes(ctx -> {
                                    teleports.tpOnTop(null);
                                    return 1;
                                }
                        ))

                .then(literal("up")
                        .executes(ctx -> {
                                    teleports.tpUp();
                                    return 1;
                                }
                        ))

                .then(literal("down")
                        .executes(ctx -> {
                                    teleports.tpDown();
                                    return 1;
                                }
                        ))

                .then(literal("forward")
                        .executes(ctx -> {
                                    teleports.tpForward();
                                    return 1;
                                }
                        ))

                .then(literal("back")
                        .executes(ctx -> {
                                    teleports.tpBack();
                                    return 1;
                                }
                        ))

                .then(literal("ground")
                        .executes(ctx -> {
                                    teleports.tpGround(null);
                                    return 1;
                                }
                        )));
    }
}