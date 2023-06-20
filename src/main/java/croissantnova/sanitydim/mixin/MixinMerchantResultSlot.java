package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantResultSlot.class)
public abstract class MixinMerchantResultSlot
{
    @Inject(method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/ResourceLocation;)V"))
    private void onTake(Player pPlayer, ItemStack pStack, CallbackInfo ci)
    {
        if (pPlayer instanceof ServerPlayer sp)
        {
            SanityProcessor.handlePlayerTradedWithVillager(sp);
        }
    }
}