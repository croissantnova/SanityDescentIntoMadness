package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

public interface IPassiveSanitySource
{
    float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim);
}