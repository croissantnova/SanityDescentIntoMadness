package croissantnova.sanitydim.event;

import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.api.SanityState;
import croissantnova.sanitydim.entity.NightmareEntity;
import croissantnova.sanitydim.sound.SoundPacketBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public final class NightmareHurtPlayerEvent {

    private final ServerPlayer player;
    private final NightmareEntity entity;

    public NightmareHurtPlayerEvent(ServerPlayer player, NightmareEntity entity) {
        this.player = player;
        this.entity = entity;

        if (PlayerSanityAPI.isAtStateOrBelow(player, SanityState.SANE)) {
            despawnNightmare();
        }
    }

    private void despawnNightmare() {
        SoundPacketBuilder.builder()
                .setSoundEvent(SoundEvents.ZOMBIE_VILLAGER_CONVERTED)
                .setSoundSource(SoundSource.HOSTILE)
                .setVec3(entity.position())
                .setVolume(1f)
                .setPitch(1f)
                .sendPacket(player);
        entity.remove(Entity.RemovalReason.DISCARDED);
    }
}
