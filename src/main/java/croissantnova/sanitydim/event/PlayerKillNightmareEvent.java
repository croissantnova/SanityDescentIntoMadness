package croissantnova.sanitydim.event;

import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigEntryOld2;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.entity.NightmareEntity;
import croissantnova.sanitydim.entity.RottingStalker;
import croissantnova.sanitydim.entity.SneakingTerror;
import croissantnova.sanitydim.util.PlayerHelper;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerKillNightmareEvent {

    private final ServerPlayer player;
    private final NightmareEntity nightmareEntity;

    public PlayerKillNightmareEvent(ServerPlayer player, NightmareEntity nightmareEntity) {
        this.player = player;
        this.nightmareEntity = nightmareEntity;
    }

    private void handle() {
        if (player == null || player.isCreative() || player.isSpectator()) {
            return;
        }

        player.getCapability(SanityProvider.CAP).ifPresent(iSanity -> {
            float sanityGain;
            if (nightmareEntity instanceof RottingStalker) {
                sanityGain = ConfigProxy.getRottingStalkerKillRatio(PlayerHelper.getDim(player));
            }
            else if (nightmareEntity instanceof SneakingTerror) {
                sanityGain = ConfigEntryOld2.activeSanitySource.killedSneakingTerror.getConfigValue(PlayerHelper.getDim(player));
            }
            else {
                throw new IllegalArgumentException("Killed nightmare entity is not yet registered.");
            }
            SanityProcessor.addSanity(iSanity, sanityGain, player);
        });
    }
}
