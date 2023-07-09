package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.capability.InnerEntityCapImplProvider;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.concurrent.atomic.AtomicReference;

public class RottingStalker extends InnerEntity implements IAnimatable
{
    private final AnimationFactory m_factory = GeckoLibUtil.createFactory(this);

    public static final AnimationBuilder ANIM_IDLE = new AnimationBuilder().addAnimation("misc.idle");
    public static final AnimationBuilder ANIM_WALK = new AnimationBuilder().addAnimation("move.walk");
    public static final AnimationBuilder ANIM_RUN = new AnimationBuilder().addAnimation("move.run");
    public static final AnimationBuilder ANIM_ATTACK_SWING = new AnimationBuilder().addAnimation("attack.swing");

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
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<>(this, "main", 0, event ->
        {
            if (attackAnim > 0.0f)
            {
                event.getController().setAnimation(ANIM_ATTACK_SWING);
                return PlayState.CONTINUE;
            }

            AtomicReference<AnimationBuilder> anim = new AtomicReference<>(ANIM_IDLE);

            if (event.isMoving())
            {
                getCapability(InnerEntityCapImplProvider.CAP).ifPresent(iec ->
                {
                    if (!iec.hasTarget() || isInWater())
                        anim.set(ANIM_WALK);
                    else
                        anim.set(ANIM_RUN);
                });
            }

            event.getController().setAnimation(anim.get());
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimationFactory getFactory()
    {
        return m_factory;
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