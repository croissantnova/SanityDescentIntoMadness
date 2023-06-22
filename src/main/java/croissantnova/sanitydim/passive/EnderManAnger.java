package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;

public class EnderManAnger implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        if (cap instanceof IPersistentSanity && ((IPersistentSanity)cap).getEnderManAngerTimer() > 0)
        {
            IPersistentSanity ps = (IPersistentSanity)cap;
            ps.setEnderManAngerTimer(ps.getEnderManAngerTimer() - 1);
            return ConfigProxy.getEnderManAnger(dim);
        }

        return 0;
    }
}