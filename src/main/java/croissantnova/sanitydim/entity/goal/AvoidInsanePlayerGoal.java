package croissantnova.sanitydim.entity.goal;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicBoolean;

public class AvoidInsanePlayerGoal extends AvoidEntityGoal<Player>
{
    private final TargetingConditions m_targetingConditions;

    public AvoidInsanePlayerGoal(PathfinderMob pMob, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier)
    {
        super(pMob, Player.class, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier);
        m_targetingConditions = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().range(pMaxDistance).selector(ent ->
        {
            if (!(ent instanceof Player player) || player.isCreative() || player.isSpectator())
                return false;

            AtomicBoolean flag = new AtomicBoolean(false);
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                flag.set(s.getSanity() >= Blackout.THRESHOLD);
            });
            return flag.get();
        });
    }

    @Override
    public boolean canUse()
    {
        this.toAvoid = this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate((double)this.maxDist, 3.0D, (double)this.maxDist), (p_148078_) -> {
            return true;
        }), m_targetingConditions, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
        if (this.toAvoid == null)
        {
            return false;
        }
        else
        {
            Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
            if (vec3 == null)
            {
                return false;
            }
            else if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob))
            {
                return false;
            }
            else
            {
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }
    }
}