package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.passive.Jukebox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class MixinJukeboxBlockEntity extends BlockEntity implements Clearable, ContainerSingleItem
{
    public MixinJukeboxBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState)
    {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "startPlaying()V", at = @At("TAIL"))
    private void startPlaying(CallbackInfo ci)
    {
        if (this.getLevel() == null || this.getLevel().isClientSide())
            return;

        Jukebox.handleJukeboxStartedPlaying(getBlockPos(), getFirstItem());
    }

    @Inject(method = "stopPlaying()V", at = @At("TAIL"))
    private void stopPlaying(CallbackInfo ci)
    {
        if (this.getLevel() == null || this.getLevel().isClientSide())
            return;

        Jukebox.handleJukeboxStoppedPlaying(getBlockPos(), getFirstItem());
    }
}