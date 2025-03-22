package croissantnova.sanitydim.config;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.config.registry.ConfigRegistry;
import croissantnova.sanitydim.config.value.ModConfigProcessableValue;
import croissantnova.sanitydim.config.value.ModConfigValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigManager {
    private static List<ConfigPassiveBlock> defPassiveBlocks = new ArrayList<>();
    private static List<ConfigItem> defItems = new ArrayList<>();
    private static List<ConfigItemCategory> defItemCats = new ArrayList<>();
    private static Map<Integer, ConfigItemCategory> defIdToItemCat = new HashMap<>();
    private static List<ConfigBrokenBlock> defBrokenBlocks = new ArrayList<>();
    private static List<ConfigBrokenBlockCategory> defBrokenBlockCats = new ArrayList<>();
    private static Map<Integer, ConfigBrokenBlockCategory> defIdToBrokenBlockCat = new HashMap<>();

    protected static final Map<String, ProxyValueEntry<?>> proxies = new HashMap<>();

    public static final List<Pair<?, ForgeConfigSpec>> configList = new ArrayList<>();
    public static Pair<ConfigRegistry, ForgeConfigSpec> configValues;

    public static void initialize() {
        configList.add(configValues = new ForgeConfigSpec.Builder().configure(ConfigRegistry::new));
    }

    public static void loadProcessors() {
        ModConfigProcessableValue.CONFIG_VALUES.forEach(ModConfigProcessableValue::loadConfig);
        defPassiveBlocks = ConfigManager.processPassiveBlocks(getConfigValues().m_passiveBlocks.get());
        defItems = ConfigManager.processItems(getConfigValues().m_items.get());
        defItemCats = ConfigManager.processItemCats(getConfigValues().m_itemCats.get());
        defIdToItemCat = ConfigManager.getMapFromItemCats(defItemCats);
        defBrokenBlocks = ConfigManager.processBrokenBlocks(getConfigValues().m_brokenBlocks.get());
        defBrokenBlockCats = ConfigManager.processBrokenBlockCats(getConfigValues().m_brokenBlockCats.get());
        defIdToBrokenBlockCat = ConfigManager.getMapFromBrokenBlockCats(defBrokenBlockCats);
    }

    public static void loadProxies() {
        // sanity
        proxies.put("sanity.positive_multiplier", new ProxyValueEntry<>(() -> getConfigValues().m_posMul.get(), ConfigManager::noFinalize));
        proxies.put("sanity.negative_multiplier", new ProxyValueEntry<>(() -> getConfigValues().m_negMul.get(), ConfigManager::noFinalize));

        // sanity.passive
        proxies.put("sanity.passive.passive", new ProxyValueEntry<>(() -> getConfigValues().m_passive.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.raining", new ProxyValueEntry<>(() -> getConfigValues().m_raining.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.hunger_threshold", new ProxyValueEntry<>(() -> getConfigValues().m_hungerThreshold.get(), ConfigManager::noFinalize));
        proxies.put("sanity.passive.hungry", new ProxyValueEntry<>(() -> getConfigValues().m_hungry.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.ender_man_anger", new ProxyValueEntry<>(() -> getConfigValues().m_enderManAnger.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.pet", new ProxyValueEntry<>(() -> getConfigValues().m_pet.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.monster", new ProxyValueEntry<>(() -> getConfigValues().m_monster.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.darkness", new ProxyValueEntry<>(() -> getConfigValues().m_darkness.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.darkness_threshold", new ProxyValueEntry<>(() -> getConfigValues().m_darknessThreshold.get(), ConfigManager::noFinalize));
        proxies.put("sanity.passive.lightness", new ProxyValueEntry<>(() -> getConfigValues().m_lightness.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.lightness_threshold", new ProxyValueEntry<>(() -> getConfigValues().m_lightnessThreshold.get(), ConfigManager::noFinalize));
        proxies.put("sanity.passive.block_stuck", new ProxyValueEntry<>(() -> getConfigValues().m_blockStuck.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.dirt_path", new ProxyValueEntry<>(() -> getConfigValues().m_dirtPath.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.jukebox_pleasant", new ProxyValueEntry<>(() -> getConfigValues().m_jukeboxPleasant.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.jukebox_unsettling", new ProxyValueEntry<>(() -> getConfigValues().m_jukeboxUnsettling.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.passive.blocks", new ProxyValueEntry<>(() -> defPassiveBlocks, ConfigManager::noFinalize));

        //sanity.active
        proxies.put("sanity.active.sleeping", new ProxyValueEntry<>(() -> getConfigValues().m_sleeping.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.sleeping_cd", new ProxyValueEntry<>(() -> getConfigValues().m_sleepingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.hurt_ratio", new ProxyValueEntry<>(() -> getConfigValues().m_hurtRatio.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.baby_chicken_spawn", new ProxyValueEntry<>(() -> getConfigValues().m_babyChickenSpawning.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.baby_chicken_spawn_cd", new ProxyValueEntry<>(() -> getConfigValues().m_babyChickenSpawningCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.advancement", new ProxyValueEntry<>(() -> getConfigValues().m_advancement.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.animal_breeding", new ProxyValueEntry<>(() -> getConfigValues().m_animalBreeding.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.animal_breeding_cd", new ProxyValueEntry<>(() -> getConfigValues().m_animalBreedingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.animal_hurt_ratio", new ProxyValueEntry<>(() -> getConfigValues().m_animalHurtRatio.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.pet_death", new ProxyValueEntry<>(() -> getConfigValues().m_petDeath.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.villager_trade", new ProxyValueEntry<>(() -> getConfigValues().m_villagerTrade.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.villager_trade_cd", new ProxyValueEntry<>(() -> getConfigValues().m_villagerTradeCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.shearing", new ProxyValueEntry<>(() -> getConfigValues().m_shearing.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.shearing_cd", new ProxyValueEntry<>(() -> getConfigValues().m_shearingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.eating", new ProxyValueEntry<>(() -> getConfigValues().m_eating.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.eating_cd", new ProxyValueEntry<>(() -> getConfigValues().m_eatingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.fishing", new ProxyValueEntry<>(() -> getConfigValues().m_fishing.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.fishing_cd", new ProxyValueEntry<>(() -> getConfigValues().m_fishingCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.farmland_trample", new ProxyValueEntry<>(() -> getConfigValues().m_farmlandTrample.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.potting_flower", new ProxyValueEntry<>(() -> getConfigValues().m_pottingFlower.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.potting_flower_cd", new ProxyValueEntry<>(() -> getConfigValues().m_pottingFlowerCd.get(), ConfigManager::finalizeCooldown));
        proxies.put("sanity.active.changed_dimension", new ProxyValueEntry<>(() -> getConfigValues().m_changedDimension.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.struck_by_lightning", new ProxyValueEntry<>(() -> getConfigValues().m_struckByLightning.get(), ConfigManager::finalizeActive));
        proxies.put("sanity.active.items", new ProxyValueEntry<>(() -> defItems, ConfigManager::noFinalize));
        proxies.put("sanity.active.item_categories", new ProxyValueEntry<>(() -> defItemCats, ConfigManager::noFinalize));
        proxies.put("sanity.active.broken_blocks", new ProxyValueEntry<>(() -> defBrokenBlocks, ConfigManager::noFinalize));
        proxies.put("sanity.active.broken_block_categories", new ProxyValueEntry<>(() -> defBrokenBlockCats, ConfigManager::noFinalize));

        // sanity.multiplayer
        proxies.put("sanity.multiplayer.sane_player_company", new ProxyValueEntry<>(() -> getConfigValues().m_sanePlayerCompany.get(), ConfigManager::finalizePassive));
        proxies.put("sanity.multiplayer.insane_player_company", new ProxyValueEntry<>(() -> getConfigValues().m_insanePlayerCompany.get(), ConfigManager::finalizePassive));

        // sanity.entity
        proxies.put("sanity.entity.sane_see_inner_entities", new ProxyValueEntry<>(() -> getConfigValues().entities_saneSeeNightmares.get(), ConfigManager::noFinalize));
        proxies.put("sanity.entity.spawn_chance_seconds", new ProxyValueEntry<>(() -> getConfigValues().entities_spawnChanceSeconds.get(), ConfigManager::noFinalize));

        // reduces boilerplate and code duplication
        for (ConfigEntryOld configEntry : ConfigEntryOld.values()) {
            configEntry.putInProxies(proxies);
        }

        ConfigEntryOld2.configEntries.forEach(configEntry -> {
            configEntry.putInProxies(proxies);
        });

        ModConfigValue.CONFIG_VALUES.forEach(configValue -> configValue.loadProxy(proxies));

        ModConfigProcessableValue.CONFIG_VALUES.forEach(configValue -> configValue.loadProxy(proxies));

        // sanity.client
        // sanity.client.indicator
        proxies.put("sanity.client.indicator.render", new ProxyValueEntry<>(() -> getConfigValues().m_renderIndicator.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.indicator.twitch", new ProxyValueEntry<>(() -> getConfigValues().m_twitchIndicator.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.indicator.scale", new ProxyValueEntry<>(() -> getConfigValues().m_indicatorScale.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.indicator.location", new ProxyValueEntry<>(() -> getConfigValues().m_indicatorLocation.get(), ConfigManager::noFinalize));

        // sanity.client.hints
        proxies.put("sanity.client.hints.render", new ProxyValueEntry<>(() -> getConfigValues().m_renderHint.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.hints.twitch", new ProxyValueEntry<>(() -> getConfigValues().m_twitchHint.get(), ConfigManager::noFinalize));

        // sanity.client.blood_tendrils
        proxies.put("sanity.client.blood_tendrils.render", new ProxyValueEntry<>(() -> getConfigValues().m_renderBloodTendrilsOverlay.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.blood_tendrils.short_burst_flash", new ProxyValueEntry<>(() -> getConfigValues().m_flashBtOnShortBurst.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.blood_tendrils.render_passive", new ProxyValueEntry<>(() -> getConfigValues().m_renderBtPassive.get(), ConfigManager::noFinalize));

        proxies.put("sanity.client.render_post", new ProxyValueEntry<>(() -> getConfigValues().m_renderPost.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.play_sounds", new ProxyValueEntry<>(() -> getConfigValues().m_playSounds.get(), ConfigManager::noFinalize));
        proxies.put("sanity.client.insanity_volume", new ProxyValueEntry<>(() -> getConfigValues().m_insanityVolume.get(), ConfigManager::noFinalize));
    }

    public static void loadConfigs() {
        loadProcessors();
        loadProxies();
        DimensionConfig.init();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configValues.getRight(), SanityMod.MOD_ID + File.separator + "default.toml");
    }

    public static ConfigRegistry getConfigValues() {
        return configValues.getLeft();
    }

    public static void onConfigLoading(final ModConfigEvent.Loading event) {
        SanityMod.LOGGER.info("Loading configurations");
        loadConfigs();
    }

    public static void onConfigReloading(final ModConfigEvent.Reloading event) {
        SanityMod.LOGGER.info("Reloading configurations");
        loadConfigs();
    }

    public static boolean stringEntryIsValid(Object entry) {
        return entry instanceof String s && !s.isBlank();
    }

    public static Double finalizeActive(Double value) {
        return -value / 100.0;
    }

    public static Double finalizePassive(Double value) {
        return -value / 2000.0;
    }

    public static Float finalizePassive(Float value) {
        return -value / 2000f;
    }

    public static Double finalizeCooldown(Double value) {
        return (double) Math.round(value * 20.0);
    }

    public static <T> T noFinalize(T value) {
        return value;
    }

    public static float proxyd2f(String path, ResourceLocation dim) {
        return ((Double) proxy(path, dim)).floatValue();
    }

    public static int proxyi(String path, ResourceLocation dim) {
        return ((Integer) proxy(path, dim)).intValue();
    }

    public static int proxyd2i(String path, ResourceLocation dim) {
        return ((Double) proxy(path, dim)).intValue();
    }

    public static boolean proxyb(String path, ResourceLocation dim) {
        return ((Boolean) proxy(path, dim)).booleanValue();
    }

    public static <T> @Nullable T proxy(String path, ResourceLocation dim) {
        if (!proxies.containsKey(path) || !DimensionConfig.configToDimStored.containsKey(path))
            return null;

        ProxyValueEntry<T> entry = (ProxyValueEntry<T>) proxies.get(path);

        if (dim != null && DimensionConfig.configToDimStored.get(path).containsKey(dim))
            return entry.finalizeValue((T) DimensionConfig.configToDimStored.get(path).get(dim));
        else
            return entry.finalizedDefault();
    }

    public static Map<Integer, ConfigItemCategory> getIdToItemCat(ResourceLocation dim) {
        return dim != null && DimensionConfig.idToItemCat.containsKey(dim) ? DimensionConfig.idToItemCat.get(dim) : defIdToItemCat;
    }

    public static Map<Integer, ConfigBrokenBlockCategory> getIdToBrokenBlockCat(ResourceLocation dim) {
        return dim != null && DimensionConfig.idToBrokenBlockCat.containsKey(dim) ? DimensionConfig.idToBrokenBlockCat.get(dim) : defIdToBrokenBlockCat;
    }

    public static List<ConfigPassiveBlock> processPassiveBlocks(List<? extends String> raw) {
        List<ConfigPassiveBlock> list = new ArrayList<>();

        for (String entry : raw) {
            String[] params = entry.trim().split("\\s*;\\s*", 4);
            if (params.length != 4) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 4");
                continue;
            }

            float sanity;
            try {
                sanity = Float.parseFloat(params[1]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[1] + " to float");
                continue;
            }
            sanity /= -2000.0f;

            float rad;
            try {
                rad = Float.parseFloat(params[2]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[2] + " to float");
                continue;
            }

            String name = params[0];
            Map<String, Boolean> props = new HashMap<>();
            int firstBracket = -1, secondBracket = -1;
            if ((firstBracket = params[0].indexOf('[')) != -1 && (secondBracket = params[0].indexOf(']')) != -1 && firstBracket < secondBracket) {
                name = params[0].substring(0, firstBracket);

                String propStr = params[0].substring(firstBracket + 1, secondBracket);
                String[] propStrSplit = propStr.trim().split("\\s*,\\s*");
                for (String s : propStrSplit) {
                    String[] keyValue = s.split("\\s*=\\s*", 2);
                    if (keyValue.length == 2) {
                        props.put(keyValue[0], Boolean.parseBoolean(keyValue[1]));
                    }
                }
            }

            ConfigPassiveBlock block = new ConfigPassiveBlock();
            if (name.startsWith("TAG_") && name.length() > 4) {
                block.name = new ResourceLocation(name.substring(4));
                block.isTag = true;
            } else block.name = new ResourceLocation(name);
            block.sanity = sanity;
            block.radius = rad;
            block.blockProperties = props;
            block.naturallyGenerated = Boolean.parseBoolean(params[3]);
            list.add(block);
        }

        return list;
    }

    public static List<ConfigItem> processItems(List<? extends String> raw) {
        List<ConfigItem> list = new ArrayList<>();

        for (String entry : raw) {
            String[] params = entry.trim().split("\\s*;\\s*", 3);
            if (params.length != 3) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 3");
                continue;
            }

            float sanity;
            try {
                sanity = Float.parseFloat(params[1]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[1] + " to float");
                continue;
            }
            sanity /= -100.0f;

            int cat;
            try {
                cat = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
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

    public static List<ConfigItemCategory> processItemCats(List<? extends String> raw) {
        List<ConfigItemCategory> list = new ArrayList<>();

        for (String entry : raw) {
            String[] params = entry.trim().split("\\s*;\\s*", 2);
            if (params.length != 2) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 2");
                continue;
            }

            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[0] + " to integer");
                continue;
            }

            float cdf;
            try {
                cdf = Float.parseFloat(params[1]);
            } catch (NumberFormatException e) {
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

    public static Map<Integer, ConfigItemCategory> getMapFromItemCats(List<ConfigItemCategory> cats) {
        Map<Integer, ConfigItemCategory> map = new HashMap<>();

        for (ConfigItemCategory cat : cats) {
            map.put(cat.m_id, cat);
        }

        return map;
    }

    public static List<ConfigBrokenBlock> processBrokenBlocks(List<? extends String> raw) {
        List<ConfigBrokenBlock> list = new ArrayList<>();

        for (String entry : raw) {
            String[] params = entry.trim().split("\\s*;\\s*", 5);
            if (params.length != 5) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 5");
                continue;
            }

            float sanity;
            try {
                sanity = Float.parseFloat(params[1]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[1] + " to float");
                continue;
            }
            sanity /= -100.0f;

            int cat;
            try {
                cat = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[2] + " to integer");
                continue;
            }

            boolean naturallyGend = Boolean.parseBoolean(params[3]);
            boolean correctToolRequired = Boolean.parseBoolean(params[4]);

            ConfigBrokenBlock block = new ConfigBrokenBlock();
            if (params[0].startsWith("TAG_") && params[0].length() > 4) {
                block.m_name = new ResourceLocation(params[0].substring(4));
                block.m_isTag = true;
            } else block.m_name = new ResourceLocation(params[0]);
            block.m_sanity = sanity;
            block.m_cat = cat;
            block.m_naturallyGend = naturallyGend;
            block.m_toolRequired = correctToolRequired;
            list.add(block);
        }

        return list;
    }

    public static List<ConfigBrokenBlockCategory> processBrokenBlockCats(List<? extends String> raw) {
        List<ConfigBrokenBlockCategory> list = new ArrayList<>();

        for (String entry : raw) {
            String[] params = entry.trim().split("\\s*;\\s*", 2);
            if (params.length != 2) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> the number of parameters is not 2");
                continue;
            }

            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                SanityMod.LOGGER.error("config format error in " + entry + " -> can't convert " + params[0] + " to integer");
                continue;
            }

            float cdf;
            try {
                cdf = Float.parseFloat(params[1]);
            } catch (NumberFormatException e) {
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

    public static Map<Integer, ConfigBrokenBlockCategory> getMapFromBrokenBlockCats(List<ConfigBrokenBlockCategory> cats) {
        Map<Integer, ConfigBrokenBlockCategory> map = new HashMap<>();

        for (ConfigBrokenBlockCategory cat : cats) {
            map.put(cat.m_id, cat);
        }

        return map;
    }

    public static class ProxyValueEntry<T> {
        private final Supplier<T> m_supplierProvider;
        private final Function<T, T> m_finalizerProvider;

        public ProxyValueEntry(@Nonnull Supplier<T> supplierProvider, @Nonnull Function<T, T> finalizerProvider) {
            Objects.requireNonNull(supplierProvider);
            Objects.requireNonNull(finalizerProvider);
            m_supplierProvider = supplierProvider;
            m_finalizerProvider = finalizerProvider;
        }

        public T defaultValue() {
            return m_supplierProvider.get();
        }

        public T finalizedDefault() {
            return finalizeValue(defaultValue());
        }

        public T finalizeValue(@Nonnull T t) {
            return m_finalizerProvider.apply(t);
        }
    }
}