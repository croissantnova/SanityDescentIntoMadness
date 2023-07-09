package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import croissantnova.sanitydim.entity.goal.AvoidInsanePlayerGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pig.class)
public abstract class MixinPig extends Animal
{
    protected MixinPig(EntityType<? extends Animal> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals()V", at = @At("HEAD"))
    private void registerGoals(CallbackInfo ci)
    {
        this.goalSelector.addGoal(-1, new AvoidInsanePlayerGoal(this, 6.0f, 1.6d, 1.7d));
    }

    @Inject(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true)
    private void mobInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> ci)
    {
        if (!pPlayer.getLevel().isClientSide())
        {
            pPlayer.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s.getSanity() >= Blackout.THRESHOLD)
                    ci.setReturnValue(InteractionResult.PASS);
            });
        }
    }
}