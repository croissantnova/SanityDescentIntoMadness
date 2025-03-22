package croissantnova.sanitydim.event;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SanityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CloneEvent {

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        oldPlayer.getCapability(SanityProvider.CAP).ifPresent(oldSanity -> {
            newPlayer.getCapability(SanityProvider.CAP).ifPresent(newSanity -> {
                transferCapability(oldSanity, newSanity);
            });
        });

        oldPlayer.invalidateCaps();
    }

    private static void transferCapability(ISanity oldSanity, ISanity newSanity) {
        IPersistentSanity oldPs = (IPersistentSanity) oldSanity;
        IPersistentSanity newPs = (IPersistentSanity) newSanity;

        newPs.setDeathScore(oldPs.getDeathScore());
    }
}
