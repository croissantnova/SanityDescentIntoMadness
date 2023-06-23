package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.passive.Jukebox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public abstract class MixinJukeboxBlockEntity extends BlockEntity implements Clearable
{
    @Shadow public abstract ItemStack getRecord();

    public MixinJukeboxBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState)
    {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "playRecord()V", at = @At("TAIL"))
    private void startPlaying(CallbackInfo ci)
    {
        if (this.getLevel() == null || this.getLevel().isClientSide())
            return;

        Jukebox.handleJukeboxStartedPlaying(getBlockPos(), getRecord());
    }

    @Inject(method = "clearContent()V", at = @At("TAIL"))
    private void stopPlaying(CallbackInfo ci)
    {
        if (this.getLevel() == null || this.getLevel().isClientSide())
            return;

        Jukebox.handleJukeboxStoppedPlaying(getBlockPos(), getRecord());
    }
}