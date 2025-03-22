package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.api.LevelSanityAPI;
import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.api.SanityState;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.sound.SoundRegistry;
import croissantnova.sanitydim.util.EntityHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NightmareEntity extends Monster {
    private final AtomicBoolean m_skipAttackInteraction = new AtomicBoolean(false);

    protected NightmareEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean skipAttackInteraction(@NotNull Entity entity) {
        if (entity instanceof Player player
                && !ConfigProxy.canSaneSeeNightmares(player.level().dimension().location())
                && !(player.isCreative() || player.isSpectator())
                && getTarget() != player) {
            m_skipAttackInteraction.set(PlayerSanityAPI.isAtStateOrBelow(player, SanityState.SANE));

            return m_skipAttackInteraction.get();
        }

        return super.skipAttackInteraction(entity);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundRegistry.INNER_ENTITY_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundRegistry.INNER_ENTITY_HURT.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        if (!LevelSanityAPI.isNightmareTime(this.level())) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        int despawnDistance = ConfigProxy.getInnerEntityDespawnMobsDistance(this.level().dimension().location());
        if (despawnDistance <= 0) return;

        List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(despawnDistance));
        entities.stream()
                .filter(EntityHelper::despawnsNearInnerEntities)
                .forEach(entity -> entity.remove(RemovalReason.DISCARDED));
    }
}