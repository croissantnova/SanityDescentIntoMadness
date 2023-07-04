package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.entity.goal.TargetInsanePlayerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglin.class)
public abstract class MixinZombifiedPiglin extends Zombie implements NeutralMob
{
    public MixinZombifiedPiglin(EntityType<? extends Zombie> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Inject(method = "addBehaviourGoals()V",
            at = @At("TAIL"))
    private void addBehaviourGoals(CallbackInfo ci)
    {
        this.targetSelector.addGoal(1, new TargetInsanePlayerGoal(this, true, .7f).setAlertOthers());
    }
}