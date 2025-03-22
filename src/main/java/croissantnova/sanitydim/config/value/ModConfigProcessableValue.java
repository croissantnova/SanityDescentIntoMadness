package croissantnova.sanitydim.config.value;

import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.util.ListHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @param <T> The type of the raw configuration value stored in the `configValue`.
 * @param <P> The processed type resulting from transforming the raw configuration value, optionally finalized; e.g., {@code List<PassiveSanityEntity>}.
 */
public abstract class ModConfigProcessableValue<T, P> implements IConfigValue<P> {
    public static final List<ModConfigProcessableValue<?, ?>> CONFIG_VALUES = new ArrayList<>();

    private final String proxyKey;
    private final Function<T, P> processor;
    private final Function<P, P> finalizer;

    protected ForgeConfigSpec.ConfigValue<T> configValue;
    private P processedValue;

    private ModConfigProcessableValue(String proxyKey, Function<T, P> processor, Function<P, P> finalizer) {
        CONFIG_VALUES.add(this);
        this.proxyKey = proxyKey;
        this.processor = processor;
        this.finalizer = finalizer;
    }

    /**
     * Needs to initialize {@link ModConfigProcessableValue#configValue} through a definition call
     */
    public abstract void build(ForgeConfigSpec.Builder builder);

    public void loadConfig() {
        processedValue = processor.apply(configValue.get());
    }

    public void loadProxy(@NotNull Map<String, ConfigManager.ProxyValueEntry<?>> proxies) {
        proxies.put(proxyKey, new ConfigManager.ProxyValueEntry<>(
                () -> processedValue,
                finalizer
        ));
    }

    public P get(ResourceLocation dimension) {
        return ConfigManager.proxy(proxyKey, dimension);
    }

    public static List<String> toPath(String name) {
        return ListHelper.getLastAsList(name.split("\\."));
    }


    public static <E extends String, P> ModConfigProcessableValue<List<? extends E>, P> createListAllowEmpty(
            String name,
            Function<List<? extends E>, P> processor,
            Function<P, P> finalizer,
            List<? extends E> defaultValue,
            Predicate<Object> elementValidator,
            String... comments) {
        return new ModConfigProcessableValue<>(name, processor, finalizer) {
            @Override
            public void build(ForgeConfigSpec.Builder builder) {
                commentate(builder, comments);
                configValue = builder.defineListAllowEmpty(toPath(name), defaultValue, elementValidator);
            }
        };
    }

    public static <E extends String, P> ModConfigProcessableValue<List<? extends E>, P> createListAllowEmpty(
            String name,
            Function<List<? extends E>, P> processor,
            List<? extends E> defaultValue,
            String... comments) {
        return createListAllowEmpty(name, processor, ConfigManager::noFinalize, defaultValue, ConfigManager::stringEntryIsValid, comments);
    }
}
