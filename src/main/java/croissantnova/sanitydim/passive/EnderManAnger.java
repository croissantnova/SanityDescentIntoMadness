package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

public class EnderManAnger implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        if (cap instanceof IPersistentSanity ps && ps.getEnderManAngerTimer() > 0)
        {
            ps.setEnderManAngerTimer(ps.getEnderManAngerTimer() - 1);
            return ConfigProxy.getEnderManAnger(dim);
        }

        return 0;
    }
}