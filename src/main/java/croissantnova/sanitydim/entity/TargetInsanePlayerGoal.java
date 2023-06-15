package croissantnova.sanitydim.entity;

import java.util.EnumSet;

import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.InnerEntityCapImplProvider;
import croissantnova.sanitydim.net.PacketHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class TargetInsanePlayerGoal extends TargetGoal
{
    public TargetInsanePlayerGoal(Mob pMob, boolean pMustSee)
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
        Player target = SanityProcessor.getMostInsanePlayer(mob.level);
        if (target != null)
        {
            mob.setTarget(target);
            targetMob = mob.getTarget();
        }

        super.start();
    }
}