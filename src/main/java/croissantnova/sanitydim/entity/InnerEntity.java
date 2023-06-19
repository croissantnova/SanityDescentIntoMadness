package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class InnerEntity extends Monster
{
    protected InnerEntity(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    public boolean shouldDropExperience()
    {
        return false;
    }

    @Override
    public void tick()
    {
        if (level().isClientSide())
        {
            super.tick();
            return;
        }
        super.tick();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource)
    {
        return SoundRegistry.INNER_ENTITY_HURT.get();
    }
}