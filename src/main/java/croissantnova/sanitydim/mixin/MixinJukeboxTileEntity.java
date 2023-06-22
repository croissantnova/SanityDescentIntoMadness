package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.passive.Jukebox;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxTileEntity.class)
public abstract class MixinJukeboxTileEntity extends TileEntity implements IClearable
{
    @Shadow public abstract ItemStack getRecord();

    public MixinJukeboxTileEntity()
    {
        super(TileEntityType.JUKEBOX);
    }

    @Inject(method = "setRecord(Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void startPlaying(CallbackInfo ci)
    {
        if (this.getLevel() == null || this.getLevel().isClientSide())
            return;

        if (!getRecord().isEmpty())
            Jukebox.handleJukeboxStartedPlaying(getBlockPos(), getRecord());
        else
            Jukebox.handleJukeboxStoppedPlaying(getBlockPos());
    }
}