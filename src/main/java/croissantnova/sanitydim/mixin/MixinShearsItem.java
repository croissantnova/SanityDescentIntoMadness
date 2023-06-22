package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public abstract class MixinShearsItem
{
    @Inject(remap = false, method = "interactLivingEntity", at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraftforge/common/IForgeShearable;onSheared(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;I)Ljava/util/List;"))
    private void interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResultType> ci)
    {
        if (playerIn instanceof ServerPlayerEntity)
            SanityProcessor.handlePlayerUsedShears((ServerPlayerEntity)playerIn);
    }
}