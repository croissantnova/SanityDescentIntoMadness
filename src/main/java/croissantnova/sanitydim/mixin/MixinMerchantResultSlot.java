package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.MerchantResultSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantResultSlot.class)
public abstract class MixinMerchantResultSlot
{
    @Inject(method = "onTake(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;awardStat(Lnet/minecraft/util/ResourceLocation;)V"))
    private void onTake(PlayerEntity pPlayer, ItemStack pStack, CallbackInfoReturnable<ItemStack> ci)
    {
        if (pPlayer instanceof ServerPlayerEntity)
        {
            SanityProcessor.handlePlayerTradedWithVillager((ServerPlayerEntity)pPlayer);
        }
    }
}