package io.github.sjouwer.tputils;

import io.github.sjouwer.tputils.util.InfoProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.*;
import static com.mojang.brigadier.arguments.StringArgumentType.*;

public class Commands {
    private Commands() {
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(literal("tpu")
                .then(literal("chunk")
                    .then(argument("x", integer())
                        .then(argument("z", integer())
                            .executes(ctx -> {
                                Teleports.chunkTp(
                                    getInteger(ctx, "x"),
                                    6,
                                    getInteger(ctx, "z"));
                                return 1;
                            }))

                        .then(argument("y", integer())
                            .then(argument("z", integer())
                                .executes(ctx -> {
                                    Teleports.chunkTp(
                                        getInteger(ctx, "x"),
                                        getInteger(ctx, "y"),
                                        getInteger(ctx, "z"));
                                    return 1;
                                })))))

                .then(literal("through")
                    .executes(ctx -> {
                        Teleports.tpThrough();
                        return 1;
                    }))

                .then(literal("thru")
                    .executes(ctx -> {
                        Teleports.tpThrough();
                        return 1;
                    }))

                .then(literal("top")
                    .executes(ctx -> {
                        Teleports.tpOnTop(null);
                        return 1;
                    }))

                .then(literal("up")
                    .executes(ctx -> {
                        Teleports.tpUp();
                        return 1;
                    }))

                .then(literal("down")
                    .executes(ctx -> {
                        Teleports.tpDown();
                        return 1;
                    }))

                .then(literal("forward")
                    .executes(ctx -> {
                        Teleports.tpForward();
                        return 1;
                    }))

                .then(literal("back")
                    .executes(ctx -> {
                        Teleports.tpBack();
                        return 1;
                    }))

                .then(literal("ground")
                    .executes(ctx -> {
                        Teleports.tpGround(null);
                        return 1;
                    }))

                .then(literal("method")
                    .then(literal("singleplayer")
                        .then(argument("command", string())
                            .executes(ctx -> {
                                String command = getString(ctx, "command");
                                TpUtils.getConfig().setSingleplayerTpMethod(command);
                                InfoProvider.sendMessage(Component.translatable("text.tputils.message.newCommandSet", command));
                                return 1;
                            })))

                    .then(literal("server")
                        .then(argument("command", string())
                            .executes(ctx -> {
                                String command = getString(ctx, "command");
                                TpUtils.getConfig().setServerTpMethod(command);
                                InfoProvider.sendMessage(Component.translatable("text.tputils.message.newCommandSet", command));
                                return 1;
                            }))))));
    }
}