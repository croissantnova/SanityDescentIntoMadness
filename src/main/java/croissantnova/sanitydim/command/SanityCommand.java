package croissantnova.sanitydim.command;

import java.util.Collection;
import java.util.Collections;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.DimensionConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class SanityCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("sanity").requires(stack ->
        {
            return stack.hasPermission(2);
        }).then(Commands.literal("set").then(Commands.argument("value", FloatArgumentType.floatArg(0f, 100f)).executes(stack ->
        {
            return setSanity(stack.getSource(), Collections.singleton((ServerPlayerEntity)stack.getSource().getEntityOrException()), FloatArgumentType.getFloat(stack, "value"));
        })).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("value", FloatArgumentType.floatArg(0f, 100f)).executes(stack ->
        {
            return setSanity(stack.getSource(), EntityArgument.getPlayers(stack, "targets"), FloatArgumentType.getFloat(stack, "value"));
        })))).then(Commands.literal("add").then(Commands.argument("value", FloatArgumentType.floatArg(-100f, 100f)).executes(stack ->
        {
            return addSanity(stack.getSource(), Collections.singleton((ServerPlayerEntity) stack.getSource().getEntityOrException()), FloatArgumentType.getFloat(stack, "value"));
        })).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("value", FloatArgumentType.floatArg(-100f, 100f)).executes(stack ->
        {
            return addSanity(stack.getSource(), EntityArgument.getPlayers(stack, "targets"), FloatArgumentType.getFloat(stack, "value"));
        })))).then(Commands.literal("config").then(Commands.literal("reload").executes(stack ->
        {
            return reloadConfig(stack.getSource());
        }))));
    }

    private static int setSanity(CommandSource stack, Collection<? extends ServerPlayerEntity> targets, float value)
    {
        for (ServerPlayerEntity player : targets)
        {
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                s.setSanity((100f - value) / 100f);
            });
        }

        if (targets.size() == 1)
        {
            stack.sendSuccess(new TranslationTextComponent("commands.sanity.set.success.single", targets.iterator().next().getDisplayName(), value), true);
        }
        else
        {
            stack.sendSuccess(new TranslationTextComponent("commands.sanity.set.success.multiple", value, targets.size()), true);
        }

        return (int)value;
    }

    private static int addSanity(CommandSource stack, Collection<? extends ServerPlayerEntity> targets, float value)
    {
        for (ServerPlayerEntity player : targets)
        {
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                s.setSanity(s.getSanity() - value / 100f);
            });
        }

        if (targets.size() == 1)
        {
            stack.sendSuccess(new TranslationTextComponent("commands.sanity.add.success.single", value, targets.iterator().next().getDisplayName()), true);
        }
        else
        {
            stack.sendSuccess(new TranslationTextComponent("commands.sanity.add.success.multiple", targets.size(), value), true);
        }

        return (int)value;
    }

    private static int reloadConfig(CommandSource stack)
    {
        DimensionConfig.init();
        stack.sendSuccess(new TranslationTextComponent("commands.sanity.config.reload"), true);
        return 1;
    }
}