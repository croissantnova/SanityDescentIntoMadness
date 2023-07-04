package croissantnova.sanitydim.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import croissantnova.sanitydim.SanityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class DimensionConfig
{
    public static final Map<String, Map<ResourceLocation, Object>> configToDimStored = new HashMap<>();
    public static final Map<ResourceLocation, Map<Integer, ConfigItemCategory>> idToItemCat = new HashMap<>();
    public static final Map<ResourceLocation, Map<Integer, ConfigBrokenBlockCategory>> idToBrokenBlockCat = new HashMap<>();

    private static void unloadDimension(String dim, Config config)
    {
        ResourceLocation name = new ResourceLocation(dim);
        for (Map.Entry<String, Map<ResourceLocation, Object>> entry : configToDimStored.entrySet())
        {
            String key = entry.getKey();
            Map<ResourceLocation, Object> value = entry.getValue();
            if (config.contains(key))
            {
                switch (key)
                {
                    case "sanity.passive.blocks" -> value.put(name, ConfigManager.processPassiveBlocks(config.get(key)));
                    case "sanity.active.items" -> value.put(name, ConfigManager.processItems(config.get(key)));
                    case "sanity.active.item_categories" ->
                    {
                        List<ConfigItemCategory> list = ConfigManager.processItemCats(config.get(key));
                        value.put(name, list);
                        idToItemCat.put(name, ConfigManager.getMapFromItemCats(list));
                    }
                    case "sanity.active.broken_blocks" -> value.put(name, ConfigManager.processBrokenBlocks(config.get(key)));
                    case "sanity.active.broken_block_categories" ->
                    {
                        List<ConfigBrokenBlockCategory> list = ConfigManager.processBrokenBlockCats(config.get(key));
                        value.put(name, list);
                        idToBrokenBlockCat.put(name, ConfigManager.getMapFromBrokenBlockCats(list));
                    }
                    default -> value.put(name, config.get(key));
                }
            }
        }
    }

    private static void createExampleConfig(Path dimPath)
    {
        Path example = Paths.get(dimPath.toString(), "example.toml");
        String lines =
                """
                # Dimensions to apply this config to, e.g. minecraft:overworld, minecraft:the_nether, minecraft:the_end
                applied_dimensions = [ "minecraft:example_dimension1", "someothermodid:example_dimension2" ]

                # Anything that is not present in this config will default to whatever is set in default.toml
                [sanity]
                \t[sanity.passive]
                \t\t# Now that's what I call hardcore
                \t\tmonster = -100.0
                """;
        try
        {

            Files.writeString(example, lines, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            SanityMod.LOGGER.warn("unable to create example dimension config, refer to github to see one");
        }
    }

    public static void init()
    {
        clear();

        for (Map.Entry<String, ConfigManager.ProxyValueEntry<?>> entry : ConfigManager.proxies.entrySet())
        {
            configToDimStored.put(entry.getKey(), new HashMap<>());
        }

        Path dimPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), SanityMod.MODID + File.separator + "dimension");
        try
        {
            Files.createDirectories(dimPath);
        }
        catch (IOException e)
        {
            SanityMod.LOGGER.warn("unable to create dimension config directory: " + dimPath);
            return;
        }

        try (Stream<Path> paths = Files.walk(dimPath, 1))
        {
            paths.forEach(path ->
            {
                if (path.toString().endsWith(".toml") && !path.toString().equals("example.toml"))
                {
                    FileConfig config = FileConfig.of(path);
                    config.load();
                    List<String> dims = config.get("applied_dimensions");
                    if (dims != null && dims.size() > 0)
                    {
                        SanityMod.LOGGER.info("Applying dimension config " + path.getFileName());
                        for (String dim : dims)
                        {
                            unloadDimension(dim, config);
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        createExampleConfig(dimPath);
    }

    public static void clear()
    {
        configToDimStored.clear();
        idToItemCat.clear();
        idToBrokenBlockCat.clear();
    }
}