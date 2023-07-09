package croissantnova.sanitydim.config;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigManager
{
    private static List<ConfigPassiveBlock> defPassiveBlocks = new ArrayList<>();
    private static List<ConfigItem> defItems = new ArrayList<>();
    private static List<ConfigItemCategory> defItemCats = new ArrayList<>();
    private static Map<Integer, ConfigItemCategory> defIdToItemCat = new HashMap<>();
    private static List<ConfigBrokenBlock> defBrokenBlocks = new ArrayList<>();
    private static List<ConfigBrokenBlockCategory> defBrokenBlockCats = new ArrayList<>();
    private static Map<Integer, ConfigBrokenBlockCategory> defIdToBrokenBlockCat = new HashMap<>();

    protected static final Map<String, ProxyValueEntry<?>> proxies = new HashMap<>();

    public static final List<Pair<?, ForgeConfigSpec>> configList = new ArrayList<>();
    public static Pair<ConfigDefault, ForgeConfigSpec> def;

    public static void init()
    {
        configList.add(def = new ForgeConfigSpec.Builder().configure(ConfigDefault::new));

        // sanity
        proxies.put("sanity.positive_multiplier", new ProxyValueEntry<>(() -> getDefault().m_posMul.get(), ConfigManager::noFinalize));
        proxies.put("sanity.negative_multiplier", new ProxyValueEntry<>(() -> getDefault().m_negMul.get(), ConfigManager::noFinalize));

        // sanity.passive
        proxies.put("sanity.passive.passive", new ProxyValueEntry<>(() -> getDefault().m_passive.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.raining", new ProxyValueEntry<>(() -> getDefault().m_raining.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.hunger_threshold", new ProxyValueEntry<>(() -> getDefault().m_hungerThreshold.get(), ConfigManager::noFinalize));
        proxies.put("sanity.passive.hungry", new ProxyValueEntry<>(() -> getDefault().m_hungry.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.ender_man_anger", new ProxyValueEntry<>(() -> getDefault().m_enderManAnger.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.pet", new ProxyValueEntry<>(() -> getDefault().m_pet.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.monster", new ProxyValueEntry<>(() -> getDefault().m_monster.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.darkness", new ProxyValueEntry<>(() -> getDefault().m_darkness.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.darkness_threshold", new ProxyValueEntry<>(() -> getDefault().m_darknessThreshold.get(), ConfigManager::noFinalize));
        proxies.put("sanity.passive.lightness", new ProxyValueEntry<>(() -> getDefault().m_lightness.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.lightness_threshold", new ProxyValueEntry<>(() -> getDefault().m_lightnessThreshold.get(), ConfigManager::noFinalize));
        proxies.put("sanity.passive.block_stuck", new ProxyValueEntry<>(() -> getDefault().m_blockStuck.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.dirt_path", new ProxyValueEntry<>(() -> getDefault().m_dirtPath.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.jukebox_pleasant", new ProxyValueEntry<>(() -> getDefault().m_jukeboxPleasant.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.jukebox_unsettling", new ProxyValueEntry<>(() -> getDefault().m_jukeboxUnsettling.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.blocks", new ProxyValueEntry<>(() -> defPassiveBlocks, ConfigManager::noFinalize));

        //sanity.active
        proxies.put("sanity.active.sleeping", new ProxyValueEntry<>(() -> getDefault().m_sleeping.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.sleeping_cd", new ProxyValueEntry<>(() -> getDefault().m_sleepingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.hurt_ratio", new ProxyValueEntry<>(() -> getDefault().m_hurtRatio.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.baby_chicken_spawn", new ProxyValueEntry<>(() -> getDefault().m_babyChickenSpawning.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.baby_chicken_spawn_cd", new ProxyValueEntry<>(() -> getDefault().m_babyChickenSpawningCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.advancement", new ProxyValueEntry<>(() -> getDefault().m_advancement.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.animal_breeding", new ProxyValueEntry<>(() -> getDefault().m_animalBreeding.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.animal_breeding_cd", new ProxyValueEntry<>(() -> getDefault().m_animalBreedingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.animal_hurt_ratio", new ProxyValueEntry<>(() -> getDefault().m_animalHurtRatio.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.pet_death", new ProxyValueEntry<>(() -> getDefault().m_petDeath.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.villager_trade", new ProxyValueEntry<>(() -> getDefault().m_villagerTrade.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.villager_trade_cd", new ProxyValueEntry<>(() -> getDefault().m_villagerTradeCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.shearing", new ProxyValueEntry<>(() -> getDefault().m_shearing.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.shearing_cd", new ProxyValueEntry<>(() -> getDefault().m_shearingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.eating", new ProxyValueEntry<>(() -> getDefault().m_eating.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.eating_cd", new ProxyValueEntry<>(() -> getDefault().m_eatingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.fishing", new ProxyValueEntry<>(() -> getDefault().m_fishing.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.fishing_cd", new ProxyValueEntry<>(() -> getDefault().m_fishingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.farmland_trample", new ProxyValueEntry<>(() -> getDefault().m_farmlandTrample.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.potting_flower", new ProxyValueEntry<>(() -> getDefault().m_pottingFlower.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.potting_flower_cd", new ProxyValueEntry<>(() -> getDefault().m_pottingFlowerCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.changed_dimension", new ProxyValueEntry<>(() -> getDefault().m_changedDimension.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.struck_by_lightning", new ProxyValueEntry<>(() -> getDefault().m_struckByLightning.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.items", new ProxyValueEntry<>(() -> defItems, ConfigManager::noFinalize));
        proxies.put("sanity.active.item_categories", new ProxyValueEntry<>(() -> defItemCats, ConfigManager::noFinalize));
        proxies.put("sanity.active.broken_blocks", new ProxyValueEntry<>(() -> defBrokenBlocks, ConfigManager::noFinalize));
        proxies.put("sanity.active.broken_block_categories", new ProxyValueEntry<>(() -> defBrokenBlockCats, ConfigManager::noFinalize));

        // sanity.multiplayer
        proxies.put("sanity.multiplayer.sane_player_company", new ProxyValueEntry<>(() -> getDefault().m_sanePlayerCompany.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.multiplayer.insane_player_company", new ProxyValueEntry<>(() -> getDefault().m_insanePlayerCompany.get(), ConfigManager::finalizePassive));

        // sanity.entity
        proxies.put("sanity.entity.sane_see_inner_entities", new ProxyValueEntry<>(() -> getDefault().m_saneSeeInnerEntities.get(), ConfigManager::noFinalize));

        // sanity.client
        // sanity.client.indicator
        proxies.put("sanity.client.indicator.render", new ProxyValueEntry<>(() -> getDefault().m_renderIndicator.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.indicator.twitch", new ProxyValueEntry<>(() -> getDefault().m_twitchIndicator.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.indicator.scale", new ProxyValueEntry<>(() -> getDefault().m_indicatorScale.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.indicator.location", new ProxyValueEntry<>(() -> getDefault().m_indicatorLocation.get(), ConfigManager::noFinalize));

        // sanity.client.hints
        proxies.put("sanity.client.hints.render", new ProxyValueEntry<>(() -> getDefault().m_renderHint.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.hints.twitch", new ProxyValueEntry<>(() -> getDefault().m_twitchHint.get(), ConfigManager::noFinalize));

        // sanity.client.blood_tendrils
        proxies.put("sanity.client.blood_tendrils.render", new ProxyValueEntry<>(() -> getDefault().m_renderBloodTendrilsOverlay.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.blood_tendrils.short_burst_flash", new ProxyValueEntry<>(() -> getDefault().m_flashBtOnShortBurst.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.blood_tendrils.render_passive", new ProxyValueEntry<>(() -> getDefault().m_renderBtPassive.get(), ConfigManager::noFinalize));

        proxies.put("sanity.client.render_post", new ProxyValueEntry<>(() -> getDefault().m_renderPost.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.play_sounds", new ProxyValueEntry<>(() -> getDefault().m_playSounds.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.insanity_volume", new ProxyValueEntry<>(() -> getDefault().m_insanityVolume.get(), ConfigManager::noFinalize));

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

    public static void onConfigLoading(final ModConfigEvent.Loading event)
    {
        defPassiveBlocks = ConfigManager.processPassiveBlocks(getDefault().m_passiveBlocks.get());
        defItems = ConfigManager.processItems(getDefault().m_items.get());
        defItemCats = ConfigManager.processItemCats(getDefault().m_itemCats.get());
        defIdToItemCat = ConfigManager.getMapFromItemCats(defItemCats);
        defBrokenBlocks = ConfigManager.processBrokenBlocks(getDefault().m_brokenBlocks.get());
        defBrokenBlockCats = ConfigManager.processBrokenBlockCats(getDefault().m_brokenBlockCats.get());
        defIdToBrokenBlockCat = ConfigManager.getMapFromBrokenBlockCats(defBrokenBlockCats);
    }

    public static void onConfigReloading(final ModConfigEvent.Reloading event)
    {
    }

    public static boolean stringEntryIsValid(Object entry)
    {
        return entry instanceof String s && !s.isEmpty() && !s.isBlank();
    }

    public static Double finalizeActive(Double value)
    {
        return -value / 100f;
    }

    public static Double finalizePassive(Double value)
    {
        return -value / 2000f;
    }

    public static Double finalizeCooldown(Double value)
    {
        return (double)Math.round(value * 20f);
    }

    public static <T> T noFinalize(T value)
    {
        return value;
    }

    public static float proxyd2f(String path, ResourceLocation dim)
    {
        return ((Double)proxy(path, dim)).floatValue();
    }

    public static int proxyi(String path, ResourceLocation dim)
    {
        return ((Integer)proxy(path, dim)).intValue();
    }

    public static int proxyd2i(String path, ResourceLocation dim)
    {
        return ((Double)proxy(path, dim)).intValue();
    }

    public static boolean proxyb(String path, ResourceLocation dim)
    {
        return ((Boolean)proxy(path, dim)).booleanValue();
    }

    public static <T> T proxy(String path, ResourceLocation dim)
    {
        if (!proxies.containsKey(path) || !DimensionConfig.configToDimStored.containsKey(path))
            return null;

        ProxyValueEntry<T> entry = (ProxyValueEntry<T>) proxies.get(path);

        if (dim != null && DimensionConfig.configToDimStored.get(path).containsKey(dim))
            return entry.finalizeValue((T) DimensionConfig.configToDimStored.get(path).get(dim));
        else
            return entry.finalizedDefault();
    }

    public static Map<Integer, ConfigItemCategory> getIdToItemCat(ResourceLocation dim)
    {
        return dim != null && DimensionConfig.idToItemCat.containsKey(dim) ? DimensionConfig.idToItemCat.get(dim) : defIdToItemCat;
    }

    public static Map<Integer, ConfigBrokenBlockCategory> getIdToBrokenBlockCat(ResourceLocation dim)
    {
        return dim != null && DimensionConfig.idToBrokenBlockCat.containsKey(dim) ? DimensionConfig.idToBrokenBlockCat.get(dim) : defIdToBrokenBlockCat;
    }

    public static List<ConfigPassiveBlock> processPassiveBlocks(List<? extends String> raw)
    {
        List<ConfigPassiveBlock> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.trim().split("\\s*;\\s*", 4);
            if (params.length != 4)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 4");
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
            Map<String, Boolean> props = new HashMap<>();
            int firstBracket = -1, secondBracket = -1;
            if ((firstBracket = params[0].indexOf('[')) != -1 && (secondBracket = params[0].indexOf(']')) != -1 && firstBracket < secondBracket)
            {
                name = params[0].substring(0, firstBracket);

                String propStr = params[0].substring(firstBracket + 1, secondBracket);
                String[] propStrSplit = propStr.trim().split("\\s*,\\s*");
                for (String s : propStrSplit)
                {
                    String[] keyValue = s.split("\\s*=\\s*", 2);
                    if (keyValue.length == 2)
                    {
                        props.put(keyValue[0], Boolean.parseBoolean(keyValue[1]));
                    }
                }
            }

            ConfigPassiveBlock block = new ConfigPassiveBlock();
            if (name.startsWith("TAG_") && name.length() > 4)
            {
                block.m_name = new ResourceLocation(name.substring(4));
                block.m_isTag = true;
            }
            else block.m_name = new ResourceLocation(name);
            block.m_sanity = sanity;
            block.m_rad = rad;
            block.m_props = props;
            block.m_naturallyGend = Boolean.parseBoolean(params[3]);
            list.add(block);
        }

        return list;
    }

    public static List<ConfigItem> processItems(List<? extends String> raw)
    {
        List<ConfigItem> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.trim().split("\\s*;\\s*", 3);
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
            String[] params = entry.trim().split("\\s*;\\s*", 2);
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

    public static Map<Integer, ConfigItemCategory> getMapFromItemCats(List<ConfigItemCategory> cats)
    {
        Map<Integer, ConfigItemCategory> map = new HashMap<>();

        for (ConfigItemCategory cat : cats)
        {
            map.put(cat.m_id, cat);
        }

        return map;
    }

    public static List<ConfigBrokenBlock> processBrokenBlocks(List<? extends String> raw)
    {
        List<ConfigBrokenBlock> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.trim().split("\\s*;\\s*", 5);
            if (params.length != 5)
            {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 5");
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

            boolean naturallyGend = Boolean.parseBoolean(params[3]);
            boolean correctToolRequired = Boolean.parseBoolean(params[4]);

            ConfigBrokenBlock block = new ConfigBrokenBlock();
            if (params[0].startsWith("TAG_") && params[0].length() > 4)
            {
                block.m_name = new ResourceLocation(params[0].substring(4));
                block.m_isTag = true;
            }
            else block.m_name = new ResourceLocation(params[0]);
            block.m_sanity = sanity;
            block.m_cat = cat;
            block.m_naturallyGend = naturallyGend;
            block.m_toolRequired = correctToolRequired;
            list.add(block);
        }

        return list;
    }

    public static List<ConfigBrokenBlockCategory> processBrokenBlockCats(List<? extends String> raw)
    {
        List<ConfigBrokenBlockCategory> list = new ArrayList<>();

        for (String entry : raw)
        {
            String[] params = entry.trim().split("\\s*;\\s*", 2);
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

            ConfigBrokenBlockCategory cat = new ConfigBrokenBlockCategory();
            cat.m_id = id;
            cat.m_cd = cd;
            list.add(cat);
        }

        return list;
    }

    public static Map<Integer, ConfigBrokenBlockCategory> getMapFromBrokenBlockCats(List<ConfigBrokenBlockCategory> cats)
    {
        Map<Integer, ConfigBrokenBlockCategory> map = new HashMap<>();

        for (ConfigBrokenBlockCategory cat : cats)
        {
            map.put(cat.m_id, cat);
        }

        return map;
    }

    public static class ProxyValueEntry<T>
    {
        private final Supplier<T> m_supplierProvider;
        private final Function<T, T> m_finalizerProvider;

        public ProxyValueEntry(@Nonnull Supplier<T> supplierProvider, @Nonnull Function<T, T> finalizerProvider)
        {
            Objects.requireNonNull(supplierProvider);
            Objects.requireNonNull(finalizerProvider);
            m_supplierProvider = supplierProvider;
            m_finalizerProvider = finalizerProvider;
        }

        public T defaultValue()
        {
            return m_supplierProvider.get();
        }

        public T finalizedDefault()
        {
            return finalizeValue(defaultValue());
        }

        public T finalizeValue(@Nonnull T t)
        {
            return m_finalizerProvider.apply(t);
        }
    }
}