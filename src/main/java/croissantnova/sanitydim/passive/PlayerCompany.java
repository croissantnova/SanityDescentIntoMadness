package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerCompany implements IPassiveSanitySource
{
    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim)
    {
        // TODO
        return 0;
    }
}