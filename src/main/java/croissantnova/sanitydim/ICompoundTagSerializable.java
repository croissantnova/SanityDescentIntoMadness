package croissantnova.sanitydim;

import net.minecraft.nbt.CompoundNBT;

public interface ICompoundTagSerializable
{
    void serializeNBT(CompoundNBT tag);

    void deserializeNBT(CompoundNBT tag);
}