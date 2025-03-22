package croissantnova.sanitydim.sources.passive.lso;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.compat.LSOCompatAPI;
import croissantnova.sanitydim.config.ConfigEntryOld2;
import croissantnova.sanitydim.sources.passive.IPassiveSanitySource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;

public class TemperatureSanitySource implements IPassiveSanitySource {

    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim) {
        if (!LSOCompatAPI.isModLoaded()) {
            return 0f;
        }

        TemperatureEnum temperatureStage = LSOCompatAPI.getTemperatureStage(player);
        return temperatureStage != null
                ? getTemperatureSanity(temperatureStage, dim)
                : 0f;
    }

    private float getTemperatureSanity(@NotNull TemperatureEnum temperatureStage, ResourceLocation dim) {
        return getConfigForTemperatureEnum(temperatureStage).getConfigValue(dim);
    }

    private ConfigEntryOld2<Float> getConfigForTemperatureEnum(@NotNull TemperatureEnum tempEnum) {
        return switch (tempEnum) {
            case HEAT_STROKE -> ConfigEntryOld2.passiveSanitySource.heatStrokeTemperature;
            case FROSTBITE -> ConfigEntryOld2.passiveSanitySource.frostbiteTemperature;
            case HOT -> ConfigEntryOld2.passiveSanitySource.hotTemperature;
            case COLD -> ConfigEntryOld2.passiveSanitySource.coldTemperature;
            case NORMAL -> ConfigEntryOld2.passiveSanitySource.normalTemperature;
        };
    }
}
