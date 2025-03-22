package croissantnova.sanitydim.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.config.ConfigManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;

public class SanityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sanity")
                .requires(stack -> stack.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("value", FloatArgumentType.floatArg(0f, 100f))
                                .executes(stack -> setSanity(stack.getSource(), Collections.singleton((ServerPlayer) stack.getSource().getEntityOrException()), FloatArgumentType.getFloat(stack, "value")))
                        )
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0f, 100f))
                                        .executes(stack -> setSanity(stack.getSource(), EntityArgument.getPlayers(stack, "targets"), FloatArgumentType.getFloat(stack, "value")))
                                )
                        )
                )
                .then(Commands.literal("get")
                        .executes(stack -> getSanity(stack.getSource(), (ServerPlayer) stack.getSource().getEntityOrException()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(stack -> getSanity(stack.getSource(), EntityArgument.getPlayer(stack, "target")))
                        )
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("value", FloatArgumentType.floatArg(-100f, 100f))
                                .executes(stack -> addSanity(stack.getSource(), Collections.singleton((ServerPlayer) stack.getSource().getEntityOrException()), FloatArgumentType.getFloat(stack, "value")))
                        )
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", FloatArgumentType.floatArg(-100f, 100f))
                                        .executes(stack -> addSanity(stack.getSource(), EntityArgument.getPlayers(stack, "targets"), FloatArgumentType.getFloat(stack, "value")))
                                )
                        )
                )
                .then(Commands.literal("config")
                        .then(Commands.literal("reload")
                                .executes(stack -> reloadConfig(stack.getSource()))
                        )
                )
        );
    }

    private static int setSanity(CommandSourceStack stack, Collection<? extends ServerPlayer> targets, float value) {
        for (ServerPlayer player : targets) {
            PlayerSanityAPI.setSanity(player, (100f - value) / 100f);
        }

        if (targets.size() == 1) {
            stack.sendSuccess(() -> Component.translatable("commands.sanity.set.success.single", targets.iterator().next().getDisplayName(), value), true);
        } else {
            stack.sendSuccess(() -> Component.translatable("commands.sanity.set.success.multiple", value, targets.size()), true);
        }

        return (int) value;
    }

    private static int getSanity(CommandSourceStack stack, ServerPlayer player) {
        float sanity = PlayerSanityAPI.getSanity(player);
        stack.sendSuccess(() -> Component.translatable("commands.sanity.get.success", player.getDisplayName(), sanity), true);
        return (int) (sanity * 100f);
    }

    private static int addSanity(CommandSourceStack stack, Collection<? extends ServerPlayer> targets, float value) {
        for (ServerPlayer player : targets) {
            PlayerSanityAPI.addSanity(player, -value / 100f);
        }

        if (targets.size() == 1) {
            stack.sendSuccess(() -> Component.translatable("commands.sanity.add.success.single", value, targets.iterator().next().getDisplayName()), true);
        } else {
            stack.sendSuccess(() -> Component.translatable("commands.sanity.add.success.multiple", targets.size(), value), true);
        }

        return (int) value;
    }

    private static int reloadConfig(CommandSourceStack stack) {
        ConfigManager.loadConfigs();
        stack.sendSuccess(() -> Component.translatable("commands.sanity.config.reload"), true);
        return 1;
    }
}