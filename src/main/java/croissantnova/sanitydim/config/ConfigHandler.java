package croissantnova.sanitydim.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import croissantnova.sanitydim.SanityMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigHandler
{
    public static final List<Pair<?, ForgeConfigSpec>> configList = new ArrayList<>();
    public static Pair<ConfigDefault, ForgeConfigSpec> def;

    public static void init()
    {
        configList.add(def = new ForgeConfigSpec.Builder().configure(ConfigDefault::new));
        DimensionConfig.init();
    }

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, def.getRight(), SanityMod.MODID + File.separator + "default.toml");
    }

    public static ConfigDefault getDefault()
    {
        return def.getLeft();
    }

    public static void onConfigLoading(final ModConfig.Loading event)
    {
        ConfigProxy.onConfigLoading();
    }

    public static void onConfigReloading(final ModConfig.Reloading event)
    {
        ConfigProxy.onConfigLoading();
    }

    public static boolean stringEntryIsValid(Object entry)
    {
        return entry instanceof String && !((String)entry).isEmpty();
    }

    public static List<ConfigPassiveBlock> processPassiveBlocks(List<? extends String> raw)
    {
        List<ConfigPassiveBlock> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.split(";", 3);
            if (params.length != 3)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 3");
                continue;
            }

            float sanity;
            try
            {
                sanity = Float.parseFloat(params[1]);
            }
            catch (NumberFormatException e)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[1] + " to float");
                continue;
            }
            sanity /= -2000.0f;

            float rad;
            try
            {
                rad = Float.parseFloat(params[2]);
            }
            catch (NumberFormatException e)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[2] + " to float");
                continue;
            }

            String name = params[0];
            Map<String, Boolean> props = Maps.newHashMap();
            int firstBracket = -1, secondBracket = -1;
            if ((firstBracket = params[0].indexOf('[')) != -1 && (secondBracket = params[0].indexOf(']')) != -1 && firstBracket < secondBracket)
            {
                name = params[0].substring(0, firstBracket);

                String propStr = params[0].substring(firstBracket + 1, secondBracket);
                String[] propStrSplit = propStr.split(",");
                for (String s : propStrSplit)
                {
                    String[] keyValue = s.split("=", 2);
                    if (keyValue.length == 2)
                    {
                        props.put(keyValue[0], Boolean.parseBoolean(keyValue[1]));
                    }
                }
            }

            ConfigPassiveBlock block = new ConfigPassiveBlock();
            block.m_name = new ResourceLocation(name);
            block.m_sanity = sanity;
            block.m_rad = rad;
            block.m_props = props;
            list.add(block);
        }

        return list;
    }

    public static List<ConfigItem> processItems(List<? extends String> raw)
    {
        List<ConfigItem> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.split(";", 3);
            if (params.length != 3)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 3");
                continue;
            }

            float sanity;
            try
            {
                sanity = Float.parseFloat(params[1]);
            }
            catch (NumberFormatException e)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[1] + " to float");
                continue;
            }
            sanity /= -100.0f;

            int cat;
            try
            {
                cat = Integer.parseInt(params[2]);
            }
            catch (NumberFormatException e)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[2] + " to integer");
                continue;
            }

            ConfigItem item = new ConfigItem();
            item.m_name = new ResourceLocation(params[0]);
            item.m_sanity = sanity;
            item.m_cat = cat;
            list.add(item);
        }

        return list;
    }

    public static List<ConfigItemCategory> processItemCats(List<? extends String> raw)
    {
        List<ConfigItemCategory> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.split(";", 2);
            if (params.length != 2)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 2");
                continue;
            }

            int id;
            try
            {
                id = Integer.parseInt(params[0]);
            }
            catch (NumberFormatException e)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[0] + " to integer");
                continue;
            }

            float cdf;
            try
            {
                cdf = Float.parseFloat(params[1]);
            }
            catch (NumberFormatException e)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[1] + " to float");
                continue;
            }
            int cd = Math.round(cdf * 20f);

            ConfigItemCategory cat = new ConfigItemCategory();
            cat.m_id = id;
            cat.m_cd = cd;
            list.add(cat);
        }

        return list;
    }

    public static Map<Integer, ConfigItemCategory> getMapFromCats(List<ConfigItemCategory> cats)
    {
        Map<Integer, ConfigItemCategory> map = Maps.newHashMap();

        for (ConfigItemCategory cat : cats)
        {
            map.put(cat.m_id, cat);
        }

        return map;
    }
}