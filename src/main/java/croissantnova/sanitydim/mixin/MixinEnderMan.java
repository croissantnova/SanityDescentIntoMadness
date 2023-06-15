package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class MixinEnderMan
{
    @Inject(method = "isLookingAtMe(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("TAIL"))
    private void isLookingAtMe(Player player, CallbackInfoReturnable<Boolean> ci)
    {
        if (player instanceof ServerPlayer sp && ci.getReturnValue())
            SanityProcessor.handlePlayerEnderManAngered(sp);
    }
}