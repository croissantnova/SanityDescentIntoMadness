package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.entity.goal.TargetInsanePlayerGoal;
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

public class SneakingTerror extends InnerEntity implements IAnimatable
{
    private final AnimationFactory m_factory = GeckoLibUtil.createFactory(this);

    public static final AnimationBuilder ANIM_IDLE = new AnimationBuilder().addAnimation("misc.idle");
    public static final AnimationBuilder ANIM_WALK = new AnimationBuilder().addAnimation("move.walk");
//    public static final AnimationBuilder ANIM_RUN = new AnimationBuilder().addAnimation("move.run");
    public static final AnimationBuilder ANIM_ATTACK_SWING = new AnimationBuilder().addAnimation("attack.swing");

    protected SneakingTerror(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0d, true));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0d));
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
            else if (event.isMoving())
            {
                event.getController().setAnimation(ANIM_WALK);
                return PlayState.CONTINUE;
            }

            event.getController().setAnimation(ANIM_IDLE);
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
                .add(Attributes.MAX_HEALTH, 700.0d)
                .add(Attributes.FOLLOW_RANGE, 128.0d)
                .add(Attributes.ATTACK_DAMAGE, 16.0d)
                .add(Attributes.MOVEMENT_SPEED, .21d)
                .add(Attributes.KNOCKBACK_RESISTANCE, .8d)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0d)
                .build();
    }
}