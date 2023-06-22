package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;

public class Hungry implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        if (player.getFoodData().getFoodLevel() <= ConfigProxy.getHungerThreshold(dim))
            return ConfigProxy.getHungry(dim);

        return 0;
    }
}
