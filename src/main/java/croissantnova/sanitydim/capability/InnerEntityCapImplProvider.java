package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InnerEntityCapImplProvider implements ICapabilityProvider
{
    public static final ResourceLocation KEY = new ResourceLocation(SanityMod.MODID, "inner_entity_cap");
    public static final Capability<IInnerEntityCap> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private final InnerEntityCapImpl m_cap = new InnerEntityCapImpl();
    private final LazyOptional<IInnerEntityCap> m_lazyOpt = LazyOptional.of(() -> m_cap);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return CAP.orEmpty(cap, m_lazyOpt);
    }
}