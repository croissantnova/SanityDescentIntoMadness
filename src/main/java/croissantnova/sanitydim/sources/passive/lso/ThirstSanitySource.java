package croissantnova.sanitydim.sources.passive.lso;

import croissantnova.sanitydim.sources.SanityCalculatorBase;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.compat.LSOCompatAPI;
import croissantnova.sanitydim.sources.passive.IPassiveSanitySource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;

public class ThirstSanitySource implements IPassiveSanitySource {

    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity sanityCap, @NotNull ResourceLocation dim) {
        return LSOCompatAPI.isModLoaded()
                ? new SanityCalculator(player, sanityCap, dim).calculate()
                : 0f;
    }

    private static class SanityCalculator extends SanityCalculatorBase {

        protected SanityCalculator(ServerPlayer player, ISanity sanityCap, ResourceLocation dim) {
            super(player, sanityCap, dim);
        }

        @Override
        protected float calculate() {
            return player.getCapability(ThirstProvider.THIRST_CAPABILITY)
                    .map(this::getSanity)
                    .orElse(0f);
        }

        private float getSanity(ThirstCapability thirstCapability) {
            int hydrationLevel = thirstCapability.getHydrationLevel();

            if (hydrationLevel <= 6) {
                float value = config.passiveConfig.lowHydration.get(dim).floatValue();
                // 100% at 6 hydration, 200% at 0 hydration
                float interpolation = (6 - hydrationLevel) / 6f + 1f;
                return value * interpolation;
            }

            if (hydrationLevel >= config.passiveConfig.wellHydratedThreshold.get(dim)) {
                return config.passiveConfig.wellHydrated.get(dim).floatValue();
            }

            return 0f;
        }
    }
}
