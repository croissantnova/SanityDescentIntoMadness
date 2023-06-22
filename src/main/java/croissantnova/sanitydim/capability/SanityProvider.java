package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;

public class SanityProvider implements ICapabilitySerializable<CompoundNBT>
{
    public static final ResourceLocation KEY = new ResourceLocation(SanityMod.MODID, "sanity");
    @CapabilityInject(ISanity.class)
    public static Capability<ISanity> CAP = null;

    private final Sanity m_cap = new Sanity();
    private final LazyOptional<ISanity> m_lazyOpt = LazyOptional.of(() -> m_cap);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
    {
        return CAP.orEmpty(cap, m_lazyOpt);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        m_cap.serializeNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        m_cap.deserializeNBT(nbt);
    }
}