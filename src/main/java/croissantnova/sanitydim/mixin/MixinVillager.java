package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class MixinVillager
{
    @Shadow protected abstract void setUnhappy();

    @Inject(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true)
    private void mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> ci)
    {
        if (!player.getLevel().isClientSide())
        {
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s.getSanity() >= .6f)
                {
                    this.setUnhappy();
                    ci.setReturnValue(InteractionResult.PASS);
                }
            });
        }
    }
}