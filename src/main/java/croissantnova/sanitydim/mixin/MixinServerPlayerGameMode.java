package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode
{
    @Shadow @Final protected ServerPlayer player;

    @Inject(remap = false,
            method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BY,
                    by = 2,
                    target = "Lnet/minecraft/server/level/ServerPlayerGameMode;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
                    ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void destroyBlock(BlockPos blockPos,
                              CallbackInfoReturnable<Boolean> ci,
                              BlockState blockState,
                              int exp,
                              BlockEntity blockEntity,
                              Block block,
                              ItemStack itemstack,
                              ItemStack itemstack1,
                              boolean flag1,
                              boolean flag)
    {
        if (flag)
            SanityProcessor.handlePlayerMinedBlock(player, blockPos, blockState, block, flag1);
    }
}