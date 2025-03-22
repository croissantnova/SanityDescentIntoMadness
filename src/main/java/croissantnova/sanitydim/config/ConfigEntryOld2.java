package croissantnova.sanitydim.config;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated
public class ConfigEntryOld2<T> {
    public static List<ConfigEntryOld2<?>> configEntries = new ArrayList<>();

    public final String proxyKey;
    private final ConfigManager.ProxyValueEntry<?> proxyValueEntry;

    private ConfigEntryOld2(String proxyKey, ConfigManager.ProxyValueEntry<?> proxyValueEntry) {
        this.proxyKey = proxyKey;
        this.proxyValueEntry = proxyValueEntry;
        configEntries.add(this);
    }

    public void putInProxies(@NotNull Map<String, ConfigManager.ProxyValueEntry<?>> proxies) {
        proxies.put(proxyKey, proxyValueEntry);
    }

    public T getConfigValue(ResourceLocation dim) {
        return ConfigManager.proxy(proxyKey, dim);
    }


    private static <T> @NotNull ConfigEntryOld2<T> create(String proxyKey, Supplier<T> getter, Function<T, T> finalizer) {
        return new ConfigEntryOld2<>(proxyKey, new ConfigManager.ProxyValueEntry<>(getter, finalizer));
    }

    private static <T> @NotNull ConfigEntryOld2<Float> createFloat(String proxyKey, Supplier<T> getter, Function<T, T> finalizer) {
        return new ConfigEntryOld2<Float>(proxyKey, new ConfigManager.ProxyValueEntry<>(getter, finalizer)) {
            @Override
            public Float getConfigValue(ResourceLocation dim) {
                return ConfigManager.proxyd2f(this.proxyKey, dim);
            }
        };
    }
    private static <T> @NotNull ConfigEntryOld2<Double> createDouble(String proxyKey, Supplier<T> getter, Function<T, T> finalizer) {
        return new ConfigEntryOld2<Double>(proxyKey, new ConfigManager.ProxyValueEntry<>(getter, finalizer)) {
            @Override
            public Double getConfigValue(ResourceLocation dim) {
                return ConfigManager.proxy(this.proxyKey, dim);
            }
        };
    }

    public static final ActiveSanitySource activeSanitySource = new ActiveSanitySource();
    public static final PassiveSanitySource passiveSanitySource = new PassiveSanitySource();

    public static class ActiveSanitySource {
        private static @NotNull ConfigEntryOld2<Float> createFloatActive(String proxyKey, Supplier<Double> getter) {
            return createFloat(proxyKey, getter, ConfigManager::finalizeActive);
        }

        public final ConfigEntryOld2<Float> killedSneakingTerror = createFloatActive(
                "sanity.active.killed_sneaking_terror",
                () -> ConfigManager.getConfigValues().m_sneakingTerrorKillRatio.get()
        );
    }

    public static class PassiveSanitySource {
        private static @NotNull ConfigEntryOld2<Float> createFloatPassive(String proxyKey, Supplier<Double> getter) {
            return createFloat(proxyKey, getter, ConfigManager::finalizePassive);
        }
        private static @NotNull ConfigEntryOld2<Double> createDoublePassive(String proxyKey, Supplier<Double> getter) {
            return createDouble(proxyKey, getter, ConfigManager::finalizePassive);
        }

        public final ConfigEntryOld2<Float> bodyPartSlightlyWounded = createFloatPassive(
                "sanity.passive.body_part_slightly_wounded",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.bodyPartSlightlyWounded.get()
        );
        public final ConfigEntryOld2<Float> bodyPartWounded = createFloatPassive(
                "sanity.passive.body_part_wounded",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.bodyPartWounded.get()
        );
        public final ConfigEntryOld2<Float> bodyPartHeavilyWounded = createFloatPassive(
                "sanity.passive.body_part_heavily_wounded",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.bodyPartHeavilyWounded.get()
        );
        public final ConfigEntryOld2<Float> bodyPartDead = createFloatPassive(
                "sanity.passive.body_part_dead",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.bodyPartDead.get()
        );

        public final ConfigEntryOld2<Float> heatStrokeTemperature = createFloatPassive(
                "sanity.passive.heat_stroke_temperature",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.heatStrokeTemperature.get()
        );
        public final ConfigEntryOld2<Float> frostbiteTemperature = createFloatPassive(
                "sanity.passive.frostbite_temperature",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.frostbiteTemperature.get()
        );
        public final ConfigEntryOld2<Float> hotTemperature = createFloatPassive(
                "sanity.passive.hot_temperature",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.hotTemperature.get()
        );
        public final ConfigEntryOld2<Float> coldTemperature = createFloatPassive(
                "sanity.passive.cold_temperature",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.coldTemperature.get()
        );
        public final ConfigEntryOld2<Float> normalTemperature = createFloatPassive(
                "sanity.passive.normal_temperature",
                () -> ConfigManager.getConfigValues().passive_lsoCompat.normalTemperature.get()
        );
    }
}
