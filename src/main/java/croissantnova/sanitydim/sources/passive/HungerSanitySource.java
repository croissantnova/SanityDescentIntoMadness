package croissantnova.sanitydim.sources.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.config.registry.ConfigRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

public class HungerSanitySource implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        ConfigRegistry config = ConfigManager.getConfigValues();
        int foodLevel = player.getFoodData().getFoodLevel();

        if (foodLevel <= ConfigProxy.getHungerThreshold(dim)) {
            return ConfigProxy.getHungry(dim);
        }

        if (foodLevel >= config.passiveConfig.wellFedThreshold.get(dim)) {
            return config.passiveConfig.wellFed.get(dim).floatValue();
        }

        return 0;
    }
}
