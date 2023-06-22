package croissantnova.sanitydim.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SanityStorage implements Capability.IStorage<ISanity>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<ISanity> capability, ISanity instance, Direction side)
    {
        CompoundNBT nbt = new CompoundNBT();
        instance.serializeNBT(nbt);
        return nbt;
    }

    @Override
    public void readNBT(Capability<ISanity> capability, ISanity instance, Direction side, INBT nbt)
    {
        instance.deserializeNBT((CompoundNBT)nbt);
    }
}