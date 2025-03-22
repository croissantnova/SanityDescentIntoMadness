package croissantnova.sanitydim.config.value;

import croissantnova.sanitydim.config.ConfigManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IConfigValue<T> {

    default void commentate(ForgeConfigSpec.Builder builder, String @NotNull ... comments) {
        for (String comment : comments) {
            builder.comment(comment);
        }
    }

    void build(ForgeConfigSpec.Builder builder);

    void loadProxy(@NotNull Map<String, ConfigManager.ProxyValueEntry<?>> proxies);

    T get(ResourceLocation dim);
}
