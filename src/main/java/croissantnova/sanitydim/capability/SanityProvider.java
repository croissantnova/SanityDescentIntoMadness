package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class SanityProvider implements ICapabilitySerializable<CompoundTag>
{
    public static final ResourceLocation KEY = new ResourceLocation(SanityMod.MODID, "sanity");
    public static final Capability<ISanity> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private final Sanity m_cap = new Sanity();
    private final LazyOptional<ISanity> m_lazyOpt = LazyOptional.of(() -> m_cap);

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side)
    {
        return CAP.orEmpty(cap, m_lazyOpt);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        m_cap.serializeNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        m_cap.deserializeNBT(nbt);
    }
}