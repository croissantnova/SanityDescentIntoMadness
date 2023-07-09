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
import org.jetbrains.annotations.Nullable;

public class SanityLevelChunkProvider implements ICapabilitySerializable<CompoundTag>
{
    public static final ResourceLocation KEY = new ResourceLocation(SanityMod.MODID, "sanity_level");
    public static final Capability<ISanityLevelChunk> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private final SanityLevelChunk m_cap = new SanityLevelChunk();
    private final LazyOptional<ISanityLevelChunk> m_lazyOpt = LazyOptional.of(() -> m_cap);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
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