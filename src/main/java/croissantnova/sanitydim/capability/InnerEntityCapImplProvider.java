package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerEntityCapImplProvider implements ICapabilityProvider
{
    public static final ResourceLocation KEY = new ResourceLocation(SanityMod.MODID, "inner_entity_cap");
    @CapabilityInject(IInnerEntityCap.class)
    public static Capability<IInnerEntityCap> CAP = null;

    private final InnerEntityCapImpl m_cap = new InnerEntityCapImpl();
    private final LazyOptional<IInnerEntityCap> m_lazyOpt = LazyOptional.of(() -> m_cap);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return CAP.orEmpty(cap, m_lazyOpt);
    }
}