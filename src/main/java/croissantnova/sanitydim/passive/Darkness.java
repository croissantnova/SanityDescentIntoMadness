package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;

public class Darkness implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        if (player.level.getMaxLocalRawBrightness(player.blockPosition()) <= ConfigProxy.getDarknessThreshold(dim))
            return ConfigProxy.getDarkness(dim);

        return 0;
    }
}