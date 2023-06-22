package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class MixinEndermanEntity
{
    @Inject(method = "isLookingAtMe(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At("TAIL"))
    private void isLookingAtMe(PlayerEntity player, CallbackInfoReturnable<Boolean> ci)
    {
        if (player instanceof ServerPlayerEntity && ci.getReturnValue())
            SanityProcessor.handlePlayerEnderManAngered((ServerPlayerEntity)player);
    }
}