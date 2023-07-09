package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

public class Passive implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        return ConfigProxy.getPassive(dim);
    }
}