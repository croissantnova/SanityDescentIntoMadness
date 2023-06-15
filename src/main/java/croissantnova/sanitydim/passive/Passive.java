package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class Passive implements IPassiveSanitySource
{
    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim)
    {
        return ConfigProxy.getPassive(dim);
    }
}