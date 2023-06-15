package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.InnerEntityCapImplProvider;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.concurrent.atomic.AtomicReference;

public class RottingStalker extends InnerEntity implements GeoEntity
{
    private final AnimatableInstanceCache m_animCache = GeckoLibUtil.createInstanceCache(this);

    protected RottingStalker(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0d, true));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, .4d));
        this.targetSelector.addGoal(0, new TargetInsanePlayerGoal(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        super.registerGoals();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
    {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, 0, state ->
        {
            if (attackAnim > 0.0f)
            {
                return state.setAndContinue(DefaultAnimations.ATTACK_SWING);
            }

            AtomicReference<RawAnimation> anim = new AtomicReference<>(DefaultAnimations.IDLE);

            if (state.isMoving())
            {
                getCapability(InnerEntityCapImplProvider.CAP).ifPresent(iec ->
                {
                    if (!iec.hasTarget() || isInWater())
                        anim.set(DefaultAnimations.WALK);
                    else
                        anim.set(DefaultAnimations.RUN);
                });
            }

            return state.setAndContinue(anim.get());
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return m_animCache;
    }

    public static AttributeSupplier buildAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0d)
                .add(Attributes.FOLLOW_RANGE, 256.0d)
                .add(Attributes.ATTACK_DAMAGE, 9.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.45d)
                .build();
    }
}