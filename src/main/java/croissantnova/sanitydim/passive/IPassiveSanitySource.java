package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IPassiveSanitySource
{
    float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim);
}