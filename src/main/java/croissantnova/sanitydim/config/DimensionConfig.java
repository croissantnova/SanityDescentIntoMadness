package croissantnova.sanitydim.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.electronwill.nightconfig.core.file.FileConfig;
import croissantnova.sanitydim.SanityMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

public class DimensionConfig
{
    public static final Map<ResourceLocation, Double> posMul = new HashMap<>();
    public static final Map<ResourceLocation, Double> negMul = new HashMap<>();

    public static final Map<ResourceLocation, Double> passive = new HashMap<>();
    public static final Map<ResourceLocation, Double> raining = new HashMap<>();
    public static final Map<ResourceLocation, Integer> hungerThreshold = new HashMap<>();
    public static final Map<ResourceLocation, Double> hungry = new HashMap<>();
    public static final Map<ResourceLocation, Double> enderManAnger = new HashMap<>();
    public static final Map<ResourceLocation, Double> pet = new HashMap<>();
    public static final Map<ResourceLocation, Double> monster = new HashMap<>();
    public static final Map<ResourceLocation, Double> darkness = new HashMap<>();
    public static final Map<ResourceLocation, Integer> darknessThreshold = new HashMap<>();
    public static final Map<ResourceLocation, Double> jukeboxPleasant = new HashMap<>();
    public static final Map<ResourceLocation, Double> jukeboxUnsettling = new HashMap<>();
    public static final Map<ResourceLocation, List<? extends String>> passiveBlocks = new HashMap<>();
    public static final Map<ResourceLocation, List<ConfigPassiveBlock>> passiveBlocksProcessed = new HashMap<>();

    public static final Map<ResourceLocation, Double> sleeping = new HashMap<>();
    public static final Map<ResourceLocation, Double> sleepingCd = new HashMap<>();
    public static final Map<ResourceLocation, Double> hurtRatio = new HashMap<>();
    public static final Map<ResourceLocation, Double> babyChickenSpawning = new HashMap<>();
    public static final Map<ResourceLocation, Double> babyChickenSpawningCd = new HashMap<>();
    public static final Map<ResourceLocation, Double> advancement = new HashMap<>();
    public static final Map<ResourceLocation, Double> animalBreeding = new HashMap<>();
    public static final Map<ResourceLocation, Double> animalBreedingCd = new HashMap<>();
    public static final Map<ResourceLocation, Double> animalHurtRatio = new HashMap<>();
    public static final Map<ResourceLocation, Double> petDeath = new HashMap<>();
    public static final Map<ResourceLocation, Double> villagerTrade = new HashMap<>();
    public static final Map<ResourceLocation, Double> villagerTradeCd = new HashMap<>();
    public static final Map<ResourceLocation, Double> shearing = new HashMap<>();
    public static final Map<ResourceLocation, Double> shearingCd = new HashMap<>();
    public static final Map<ResourceLocation, Double> eating = new HashMap<>();
    public static final Map<ResourceLocation, Double> eatingCd = new HashMap<>();
    public static final Map<ResourceLocation, Double> fishing = new HashMap<>();
    public static final Map<ResourceLocation, Double> fishingCd = new HashMap<>();
    public static final Map<ResourceLocation, List<? extends String>> items = new HashMap<>();
    public static final Map<ResourceLocation, List<ConfigItem>> itemsProcessed = new HashMap<>();
    public static final Map<ResourceLocation, List<? extends String>> itemCats = new HashMap<>();
    public static final Map<ResourceLocation, List<ConfigItemCategory>> itemCatsProcessed = new HashMap<>();
    public static final Map<ResourceLocation, Map<Integer, ConfigItemCategory>> idToItemCat = new HashMap<>();

    public static final Map<ResourceLocation, Double> sanePlayerCompany = new HashMap<>();
    public static final Map<ResourceLocation, Double> insanePlayerCompany = new HashMap<>();

    public static final Map<ResourceLocation, Boolean> renderIndicator = new HashMap<>();
    public static final Map<ResourceLocation, Boolean> twitchIndicator = new HashMap<>();
    public static final Map<ResourceLocation, Double> indicatorScale = new HashMap<>();
    public static final Map<ResourceLocation, SanityIndicatorLocation> indicatorLocation = new HashMap<>();

    public static final Map<ResourceLocation, Boolean> renderHint = new HashMap<>();
    public static final Map<ResourceLocation, Boolean> twitchHint = new HashMap<>();

    public static final Map<ResourceLocation, Boolean> renderPost = new HashMap<>();
    public static final Map<ResourceLocation, Boolean> playSounds = new HashMap<>();
    public static final Map<ResourceLocation, Double> insanityVolume = new HashMap<>();

    protected static void unloadDimension(String dim, FileConfig config)
    {
        ResourceLocation name = new ResourceLocation(dim);

        if (config.contains("sanity.positive_multiplier"))
            posMul.put(name, config.get("sanity.positive_multiplier"));
        if (config.contains("sanity.negative_multiplier"))
            negMul.put(name, config.get("sanity.negative_multiplier"));

        if (config.contains("sanity.passive.passive"))
            passive.put(name, config.get("sanity.passive.passive"));
        if (config.contains("sanity.passive.raining"))
            raining.put(name, config.get("sanity.passive.raining"));
        if (config.contains("sanity.passive.hunger_threshold"))
            hungerThreshold.put(name, config.get("sanity.passive.hunger_threshold"));
        if (config.contains("sanity.passive.hungry"))
            hungry.put(name, config.get("sanity.passive.hungry"));
        if (config.contains("sanity.passive.ender_man_anger"))
            enderManAnger.put(name, config.get("sanity.passive.ender_man_anger"));
        if (config.contains("sanity.passive.pet"))
            pet.put(name, config.get("sanity.passive.pet"));
        if (config.contains("sanity.passive.monster"))
            monster.put(name, config.get("sanity.passive.monster"));
        if (config.contains("sanity.passive.darkness"))
            darkness.put(name, config.get("sanity.passive.darkness"));
        if (config.contains("sanity.passive.darkness_threshold"))
            darknessThreshold.put(name, config.get("sanity.passive.darkness_threshold"));
        if (config.contains("sanity.passive.jukebox_pleasant"))
            jukeboxPleasant.put(name, config.get("sanity.passive.jukebox_pleasant"));
        if (config.contains("sanity.passive.jukebox_unsettling"))
            jukeboxUnsettling.put(name, config.get("sanity.passive.jukebox_unsettling"));
        if (config.contains("sanity.passive.blocks"))
        {
            List<? extends String> pb = config.get("sanity.passive.blocks");
            passiveBlocks.put(name, pb);
            passiveBlocksProcessed.put(name, ConfigHandler.processPassiveBlocks(pb));
        }

        if (config.contains("sanity.active.sleeping"))
            sleeping.put(name, config.get("sanity.active.sleeping"));
        if (config.contains("sanity.active.sleeping_cd"))
            sleepingCd.put(name, config.get("sanity.active.sleeping_cd"));
        if (config.contains("sanity.active.hurt_ratio"))
            hurtRatio.put(name, config.get("sanity.active.hurt_ratio"));
        if (config.contains("sanity.active.baby_chicken_spawn"))
            babyChickenSpawning.put(name, config.get("sanity.active.baby_chicken_spawn"));
        if (config.contains("sanity.active.baby_chicken_spawn_cd"))
            babyChickenSpawningCd.put(name, config.get("sanity.active.baby_chicken_spawn_cd"));
        if (config.contains("sanity.active.advancement"))
            advancement.put(name, config.get("sanity.active.advancement"));
        if (config.contains("sanity.active.animal_breeding"))
            animalBreeding.put(name, config.get("sanity.active.animal_breeding"));
        if (config.contains("sanity.active.animal_breeding_cd"))
            animalBreedingCd.put(name, config.get("sanity.active.animal_breeding_cd"));
        if (config.contains("sanity.active.animal_hurt_ratio"))
            animalHurtRatio.put(name, config.get("sanity.active.animal_hurt_ratio"));
        if (config.contains("sanity.active.pet_death"))
            petDeath.put(name, config.get("sanity.active.pet_death"));
        if (config.contains("sanity.active.villager_trade"))
            villagerTrade.put(name, config.get("sanity.active.villager_trade"));
        if (config.contains("sanity.active.villager_trade_cd"))
            villagerTradeCd.put(name, config.get("sanity.active.villager_trade_cd"));
        if (config.contains("sanity.active.shearing"))
            shearing.put(name, config.get("sanity.active.shearing"));
        if (config.contains("sanity.active.shearing_cd"))
            shearingCd.put(name, config.get("sanity.active.shearing_cd"));
        if (config.contains("sanity.active.eating"))
            eating.put(name, config.get("sanity.active.eating"));
        if (config.contains("sanity.active.eating_cd"))
            eatingCd.put(name, config.get("sanity.active.eating_cd"));
        if (config.contains("sanity.active.fishing"))
            fishing.put(name, config.get("sanity.active.fishing"));
        if (config.contains("sanity.active.fishing_cd"))
            fishingCd.put(name, config.get("sanity.active.fishing_cd"));
        if (config.contains("sanity.active.items"))
        {
            List<? extends String> it3ms = config.get("sanity.active.items");
            items.put(name, it3ms);
            itemsProcessed.put(name, ConfigHandler.processItems(it3ms));
        }
        if (config.contains("sanity.active.item_categories"))
        {
            List<? extends String> it3mCats = config.get("sanity.active.item_categories");
            itemCats.put(name, it3mCats);
            itemCatsProcessed.put(name, ConfigHandler.processItemCats(it3mCats));
            idToItemCat.put(name, ConfigHandler.getMapFromCats(itemCatsProcessed.get(name)));
        }

        if (config.contains("sanity.multiplayer.sane_player_company"))
            sanePlayerCompany.put(name, config.get("sanity.multiplayer.insane_player_company"));
        if (config.contains("sanity.multiplayer.insane_player_company"))
            insanePlayerCompany.put(name, config.get("sanity.multiplayer.insane_player_company"));

        if (config.contains("sanity.client.indicator.render"))
            renderIndicator.put(name, config.get("sanity.client.indicator.render"));
        if (config.contains("sanity.client.indicator.twitch"))
            twitchIndicator.put(name, config.get("sanity.client.indicator.twitch"));
        if (config.contains("sanity.client.indicator.scale"))
            indicatorScale.put(name, config.get("sanity.client.indicator.scale"));
        if (config.contains("sanity.client.indicator.location"))
            indicatorLocation.put(name, config.get("sanity.client.indicator.location"));

        if (config.contains("sanity.client.hints.render"))
            renderHint.put(name, config.get("sanity.client.hints.render"));
        if (config.contains("sanity.client.hints.twitch"))
            twitchHint.put(name, config.get("sanity.client.hints.twitch"));

        if (config.contains("sanity.client.render_post"))
            renderPost.put(name, config.get("sanity.client.render_post"));
        if (config.contains("sanity.client.play_sounds"))
            playSounds.put(name, config.get("sanity.client.play_sounds"));
        if (config.contains("sanity.client.insanity_volume"))
            insanityVolume.put(name, config.get("sanity.client.insanity_volume"));
    }

    public static void init()
    {
        clear();
        Path dimPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), SanityMod.MODID + File.separator + "dimension");
        if (!Files.isDirectory(dimPath))
            return;
        try (Stream<Path> paths = Files.walk(dimPath, 1))
        {
            paths.forEach(path ->
            {
                if (path.toString().endsWith(".toml"))
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
    }

    public static void clear()
    {
        posMul.clear();
        negMul.clear();
        passive.clear();
        raining.clear();
        hungerThreshold.clear();
        hungry.clear();
        enderManAnger.clear();
        pet.clear();
        monster.clear();
        darkness.clear();
        darknessThreshold.clear();
        jukeboxPleasant.clear();
        jukeboxUnsettling.clear();
        passiveBlocks.clear();
        passiveBlocksProcessed.clear();
        sleeping.clear();
        sleepingCd.clear();
        hurtRatio.clear();
        babyChickenSpawning.clear();
        babyChickenSpawningCd.clear();
        advancement.clear();
        animalBreeding.clear();
        animalBreedingCd.clear();
        animalHurtRatio.clear();
        petDeath.clear();
        villagerTrade.clear();
        villagerTradeCd.clear();
        shearing.clear();
        shearingCd.clear();
        eating.clear();
        eatingCd.clear();
        fishing.clear();
        fishingCd.clear();
        items.clear();
        itemsProcessed.clear();
        itemCats.clear();
        itemCatsProcessed.clear();
        sanePlayerCompany.clear();
        insanePlayerCompany.clear();
        renderIndicator.clear();
        twitchIndicator.clear();
        indicatorScale.clear();
        indicatorLocation.clear();
        renderHint.clear();
        twitchHint.clear();
        renderPost.clear();
        playSounds.clear();
        insanityVolume.clear();
    }
}