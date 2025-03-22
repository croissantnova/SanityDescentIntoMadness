package croissantnova.sanitydim.config.value;

import croissantnova.sanitydim.config.ConfigManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static croissantnova.sanitydim.api.PlayerSanityAPI.MAX_SANITY;
import static croissantnova.sanitydim.api.PlayerSanityAPI.MIN_SANITY;

public abstract class ModConfigValue<T> implements IConfigValue<T> {
    public static final List<ModConfigValue<?>> CONFIG_VALUES = new ArrayList<>();

    private final String proxyKey;
    private final Function<T, T> finalizer;
    protected ForgeConfigSpec.ConfigValue<T> configValue;

    private ModConfigValue(String proxyKey, Function<T, T> finalizer) {
        CONFIG_VALUES.add(this);
        this.proxyKey = proxyKey;
        this.finalizer = finalizer;
    }

    /**
     * Needs to initialize {@link ModConfigValue#configValue} through a definition call
     */
    public abstract void build(ForgeConfigSpec.Builder builder);

    public void loadProxy(@NotNull Map<String, ConfigManager.ProxyValueEntry<?>> proxies) {
        proxies.put(proxyKey, new ConfigManager.ProxyValueEntry<>(
                configValue,
                finalizer
        ));
    }

    public T get(ResourceLocation dim) {
        return ConfigManager.proxy(proxyKey, dim);
    }

    protected String toPath(String name) {
        String[] parts = name.split("\\.");
        return parts[parts.length - 1];
    }


    public static ModConfigValue<Integer> createInteger(String name, int defaultValue, int minValue, int maxValue, String... comments) {
        return new ModConfigValue<>(name, ConfigManager::noFinalize) {
            @Override
            public void build(ForgeConfigSpec.Builder builder) {
                commentate(builder, comments);
                configValue = builder.defineInRange(toPath(name), defaultValue, minValue, maxValue);
            }
        };
    }

    public static ModConfigValue<Double> createDouble(String name, double defaultValue, double minValue, double maxValue, String... comments) {
        return new ModConfigValue<>(name, ConfigManager::noFinalize) {
            @Override
            public void build(ForgeConfigSpec.Builder builder) {
                commentate(builder, comments);
                configValue = builder.defineInRange(toPath(name), defaultValue, minValue, maxValue);
            }
        };
    }


    public static ModConfigValue<Double> createActiveDouble(String name, double defaultValue, double minValue, double maxValue, String... comments) {
        return new ModConfigValue<>(name, ConfigManager::finalizeActive) {
            @Override
            public void build(ForgeConfigSpec.Builder builder) {
                commentate(builder, comments);
                configValue = builder.defineInRange(toPath(name), defaultValue, minValue, maxValue);
            }
        };
    }

    public static ModConfigValue<Double> createActiveDouble(String name, double defaultValue, String... comments) {
        return createActiveDouble(name, defaultValue, MIN_SANITY, MAX_SANITY, comments);
    }


    public static ModConfigValue<Double> createPassiveDouble(String name, double defaultValue, double minValue, double maxValue, String... comments) {
        return new ModConfigValue<>(name, ConfigManager::finalizePassive) {
            @Override
            public void build(ForgeConfigSpec.Builder builder) {
                commentate(builder, comments);
                configValue = builder.defineInRange(toPath(name), defaultValue, minValue, maxValue);
            }
        };
    }

    /**
     * Creates a passive double configuration value with predefined sanity
     * minimum and maximum bounds (-100, 100).
     *
     * @param name         the unique name identifying this configuration value
     * @param defaultValue the default value to be used if none is specified
     * @param comments     optional comments to describe this configuration value
     * @return a {@link ModConfigValue} instance representing the configuration value
     */
    public static ModConfigValue<Double> createPassiveDouble(String name, double defaultValue, String... comments) {
        return createPassiveDouble(name, defaultValue, MIN_SANITY, MAX_SANITY, comments);
    }
}
