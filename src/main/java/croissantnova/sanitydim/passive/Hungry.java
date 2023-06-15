package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class Hungry implements IPassiveSanitySource
{
    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim)
    {
        if (player.getFoodData().getFoodLevel() <= ConfigProxy.getHungerThreshold(dim))
            return ConfigProxy.getHungry(dim);

        return 0;
    }
}
