package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public abstract class MixinShearsItem
{
    @Inject(remap = false, method = "interactLivingEntity", at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraftforge/common/IForgeShearable;onSheared(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)Ljava/util/List;"))
    private void interactLivingEntity(ItemStack stack, net.minecraft.world.entity.player.Player playerIn, LivingEntity entity, net.minecraft.world.InteractionHand hand, CallbackInfoReturnable<InteractionResult> ci)
    {
        if (playerIn instanceof ServerPlayer sp)
            SanityProcessor.handlePlayerUsedShears(sp);
    }
}