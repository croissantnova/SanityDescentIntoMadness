package croissantnova.sanitydim.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import javax.annotation.Nullable;

public class InnerEntityCapImplStorage implements Capability.IStorage<IInnerEntityCap>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IInnerEntityCap> capability, IInnerEntityCap instance, Direction side)
    {
        return null;
    }

    @Override
    public void readNBT(Capability<IInnerEntityCap> capability, IInnerEntityCap instance, Direction side, INBT nbt)
    {

    }
}