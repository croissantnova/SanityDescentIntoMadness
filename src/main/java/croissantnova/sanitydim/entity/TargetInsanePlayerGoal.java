package croissantnova.sanitydim.entity;

import java.util.EnumSet;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class TargetInsanePlayerGoal extends TargetGoal
{
    public TargetInsanePlayerGoal(MobEntity pMob, boolean pMustSee)
    {
        super(pMob, pMustSee);
        setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse()
    {
        return SanityProcessor.getMostInsanePlayer(mob.level) != null;
    }

    @Override
    public void start()
    {
        PlayerEntity target = SanityProcessor.getMostInsanePlayer(mob.level);
        if (target != null)
        {
            mob.setTarget(target);
            targetMob = mob.getTarget();
        }

        super.start();
    }
}