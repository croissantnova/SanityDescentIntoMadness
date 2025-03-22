package croissantnova.sanitydim.compat;

import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;

public class LSOCompatAPI {

    public static boolean isModLoaded() {
        return ModList.get().isLoaded("legendarysurvivaloverhaul");
    }

    public static @Nullable TemperatureEnum getTemperatureStage(@NotNull ServerPlayer player) {
        return player.getCapability(TemperatureProvider.TEMPERATURE_CAPABILITY)
                .map(temperatureCapability -> TemperatureEnum.get(temperatureCapability.getTemperatureLevel()))
                .orElse(null);
    }
}
