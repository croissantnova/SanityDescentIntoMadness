package croissantnova.sanitydim.event;

import croissantnova.sanitydim.ActiveSanitySources;
import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.util.DeathScoreHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SanityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SleepFinishedEvent {

    @SubscribeEvent
    public static void onSleepFinished(final SleepFinishedTimeEvent event)
    {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel level) {
            handlePlayersSlept(level);
        }
    }

    private static void handlePlayersSlept(ServerLevel level)
    {
        level.players().forEach(SleepFinishedEvent::handlePlayerSlept);
    }

    private static void handlePlayerSlept(ServerPlayer player) {
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        DeathScoreHelper dsh = new DeathScoreHelper(player);
        if (dsh.isDeathSanityEnabled() && dsh.isDeathStackingEnabled()) {

            boolean playerLostADeath = dsh.decrementFullDeath();
            if (playerLostADeath) {
                player.displayClientMessage(dsh.getDecrementText(), true);
            }
        }

        SanityProcessor.handleActiveSourceForPlayer(player, ActiveSanitySources.SLEEPING, ConfigProxy::getSleepingCooldown, ConfigProxy::getSleeping);
    }
}
