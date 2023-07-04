package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class InnerEntity extends Monster
{
    private final AtomicBoolean m_skipAttackInteraction = new AtomicBoolean(false);

    protected InnerEntity(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    public boolean skipAttackInteraction(Entity entity)
    {
        if (entity instanceof Player player && !ConfigProxy.getSaneSeeInnerEntities(player.level().dimension().location()) &&
                !(player.isCreative() || player.isSpectator()) && getTarget() != player)
        {
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                m_skipAttackInteraction.set(s.getSanity() < .6f);
            });

            return m_skipAttackInteraction.get();
        }

        return super.skipAttackInteraction(entity);
    }

    @Override
    public boolean shouldDropExperience()
    {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource)
    {
        return SoundRegistry.INNER_ENTITY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundRegistry.INNER_ENTITY_HURT.get();
    }
}