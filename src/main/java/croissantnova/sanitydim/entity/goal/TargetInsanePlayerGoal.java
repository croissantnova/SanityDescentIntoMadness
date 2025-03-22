package croissantnova.sanitydim.entity.goal;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TargetInsanePlayerGoal extends TargetGoal {
    private final float sanityThreshold;
    private boolean alertSameType;
    private Class<?>[] toIgnoreAlert = new Class<?>[0];
    private Player insanePlayer;

    public TargetInsanePlayerGoal(Mob pMob, boolean pMustSee, float sanityThreshold) {
        super(pMob, pMustSee);
        setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.sanityThreshold = sanityThreshold;
    }

    public TargetInsanePlayerGoal(Mob pMob, boolean pMustSee) {
        this(pMob, pMustSee, -1f);
    }

    @Override
    public boolean canUse() {
        return (insanePlayer = getMostInsanePlayer()) != null;
    }

    @Override
    public void start() {
        Player target = insanePlayer;
        if (target != null) {
            mob.setTarget(target);
            targetMob = mob.getTarget();
            if (alertSameType) {
                alertOthers();
            }
        }

        super.start();
    }

    public TargetInsanePlayerGoal setAlertOthers(Class<?>... pReinforcementTypes) {
        alertSameType = true;
        toIgnoreAlert = pReinforcementTypes;
        return this;
    }

    private Player getMostInsanePlayer() {
        return sanityThreshold < 0f ? SanityProcessor.getMostInsanePlayer(mob.level()) : SanityProcessor.getMostInsanePlayer(mob.level(), sanityThreshold);
    }

    protected void alertOthers() {
        double followDistance = this.getFollowDistance();
        AABB searchArea = AABB.unitCubeFromLowerCorner(mob.position()).inflate(followDistance, 10.0, followDistance);
        List<? extends Mob> mobsInRange = mob.level().getEntitiesOfClass(mob.getClass(), searchArea, EntitySelector.NO_SPECTATORS);

        for (Mob mobInRange : mobsInRange) {
            if (shouldAlertMob(mobInRange)) {
                this.alertOther(mobInRange, mob.getTarget());
            }
        }

    }

    private boolean shouldAlertMob(@NotNull Mob otherMob) {
        if (otherMob == mob || otherMob.getTarget() != null) {
            return false;
        }

        if (mob instanceof TamableAnimal tamable) {
            if (tamable.getOwner() != ((TamableAnimal) otherMob).getOwner()) {
                return false;
            }
        }

        Optional<LivingEntity> target = Optional.ofNullable(mob.getTarget());
        if (target.isPresent() && otherMob.isAlliedTo(target.get())) {
            return false;
        }

        return !shouldIgnoreAlertFor(otherMob);
    }

    private boolean shouldIgnoreAlertFor(@NotNull Mob otherMob) {
        for (Class<?> ignoreClass : toIgnoreAlert) {
            if (otherMob.getClass() == ignoreClass) {
                return true;
            }
        }
        return false;
    }


    protected void alertOther(@NotNull Mob pMob, LivingEntity pTarget) {
        pMob.setTarget(pTarget);
    }
}