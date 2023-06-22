package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import javax.annotation.Nonnull;

public abstract class InnerEntity extends MonsterEntity
{
    protected InnerEntity(EntityType<? extends MonsterEntity> entityType, World level)
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
        if (this.level.isClientSide())
        {
            super.tick();
            return;
        }
        super.tick();
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource)
    {
        return SoundRegistry.INNER_ENTITY_HURT.get();
    }
}