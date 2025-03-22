package croissantnova.sanitydim.event;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.api.SanityState;
import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.util.PlayerHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SanityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SleepInBedEvent {

    @SubscribeEvent
    public static void onSleep(PlayerSleepInBedEvent event) {
        if (event.isCanceled() || event.getResultStatus() != null) {
            return;
        }

        Player player = event.getEntity();
        if (PlayerHelper.isMortal(player)
                && PlayerSanityAPI.isAtStateOrAbove(player, SanityState.INSANE)) {
            event.setResult(Player.BedSleepingProblem.NOT_SAFE);
            player.sendSystemMessage(Component.literal("I don't feel safe here..."));
            event.setCanceled(true);
        }
    }
}
