package croissantnova.sanitydim.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;

import java.util.ArrayList;
import java.util.List;

public class ConfigDefault
{
    public final DoubleValue m_posMul;
    public final DoubleValue m_negMul;

    public final DoubleValue m_passive;
    public final DoubleValue m_raining;
    public final IntValue m_hungerThreshold;
    public final DoubleValue m_hungry;
    public final DoubleValue m_enderManAnger;
    public final DoubleValue m_pet;
    public final DoubleValue m_monster;
    public final DoubleValue m_darkness;
    public final IntValue m_darknessThreshold;
    public final DoubleValue m_lightness;
    public final IntValue m_lightnessThreshold;
    public final DoubleValue m_blockStuck;
    public final DoubleValue m_dirtPath;
    public final DoubleValue m_jukeboxPleasant;
    public final DoubleValue m_jukeboxUnsettling;
    public final ConfigValue<List<? extends String>> m_passiveBlocks;

    public final DoubleValue m_sleeping;
    public final DoubleValue m_sleepingCd;
    public final DoubleValue m_hurtRatio;
    public final DoubleValue m_babyChickenSpawning;
    public final DoubleValue m_babyChickenSpawningCd;
    public final DoubleValue m_advancement;
    public final DoubleValue m_animalBreeding;
    public final DoubleValue m_animalBreedingCd;
    public final DoubleValue m_animalHurtRatio;
    public final DoubleValue m_petDeath;
    public final DoubleValue m_villagerTrade;
    public final DoubleValue m_villagerTradeCd;
    public final DoubleValue m_shearing;
    public final DoubleValue m_shearingCd;
    public final DoubleValue m_eating;
    public final DoubleValue m_eatingCd;
    public final DoubleValue m_fishing;
    public final DoubleValue m_fishingCd;
    public final DoubleValue m_farmlandTrample;
    public final DoubleValue m_pottingFlower;
    public final DoubleValue m_pottingFlowerCd;
    public final DoubleValue m_changedDimension;
    public final DoubleValue m_struckByLightning;
    public final ConfigValue<List<? extends String>> m_items;
    public final ConfigValue<List<? extends String>> m_itemCats;
    public final ConfigValue<List<? extends String>> m_brokenBlocks;
    public final ConfigValue<List<? extends String>> m_brokenBlockCats;

    public final DoubleValue m_sanePlayerCompany;
    public final DoubleValue m_insanePlayerCompany;

    public final BooleanValue m_saneSeeInnerEntities;

    public final BooleanValue m_renderIndicator;
    public final BooleanValue m_twitchIndicator;
    public final DoubleValue m_indicatorScale;
    public final EnumValue<SanityIndicatorLocation> m_indicatorLocation;

    public final BooleanValue m_renderHint;
    public final BooleanValue m_twitchHint;

    public final BooleanValue m_renderBloodTendrilsOverlay;
    public final BooleanValue m_flashBtOnShortBurst;
    public final BooleanValue m_renderBtPassive;

    public final BooleanValue m_renderPost;
    public final BooleanValue m_playSounds;
    public final DoubleValue m_insanityVolume;

    public ConfigDefault(ForgeConfigSpec.Builder builder)
    {
        builder.comment(
                "Sanity configuration",
                "NOTE: all sanity values are measured in percentages (i.e. 40.0 is equal to 40% of sanity bar)",
                "NOTE: each subsequent usage of an active source or item has its effectiveness multiplied by (timeSinceLastUsage / cooldown) (capped at 1.0)")
                .push("sanity");

        m_posMul = builder
                .comment("For balancing purposes: the effectiveness of all positive sanity sources will be multiplied by this number")
                .defineInRange("positive_multiplier", 1.0, Float.MIN_VALUE, Float.MAX_VALUE);
        m_negMul = builder
                .comment("For balancing purposes: the effectiveness of all negative sanity sources will be multiplied by this number")
                .defineInRange("negative_multiplier", 1.0, Float.MIN_VALUE, Float.MAX_VALUE);

        builder.comment("Configuration for passive sanity sources").push("passive");

        m_passive = builder
                .comment("This value will be added to sanity each second regardless of any other factors")
                .defineInRange("passive", .0, -100.0, 100.0);
        m_raining = builder
                .comment("Sanity gain per second during rainy weather or in water")
                .defineInRange("raining", -.2, -100.0, 100.0);
        m_hungerThreshold = builder
                .comment("Players' sanity will start getting affected with food levels at and below this threshold (in half-drumsticks)")
                .defineInRange("hunger_threshold", 8, 0, 20);
        m_hungry = builder
                .comment("Players with food levels at and below <hunger_threshold> gain this amount of sanity per second")
                .defineInRange("hungry", -.2, -100.0, 100.0);
        m_enderManAnger = builder
                .comment("Players will gain this amount of sanity per second for 5 seconds after looking at an enderman")
                .defineInRange("ender_man_anger", -5.0, -100.0, 100.0);
        m_pet = builder
                .comment("Players will gain this amount of sanity per second while being near their pets")
                .defineInRange("pet", .15, -100.0, 100.0);
        m_monster = builder
                .comment("Players will gain this amount of sanity per second while being near any monsters")
                .comment("This value is doubled if the monster is aggressive towards the player")
                .defineInRange("monster", -.1, -100.0, 100.0);
        m_darkness = builder
                .comment("Players will gain this amount of sanity per second while being in the dark")
                .defineInRange("darkness", -.15, -100.0, 100.0);
        m_darknessThreshold = builder
                .comment("Maximum light level considered to be darkness (inclusive)")
                .defineInRange("darkness_threshold", 4, 0, 15);
        m_lightness = builder
                .comment("Players will gain this amount of sanity per second while being in the light")
                .defineInRange("lightness", 0.0, -100.0, 100.0);
        m_lightnessThreshold = builder
                .comment("Minimum light level considered to be lightness (inclusive)")
                .defineInRange("lightness_threshold", 4, 0, 15);
        m_blockStuck = builder
                .comment("Players who are stuck in blocks (such as cobweb) and have their movement restricted gain this amount of sanity per second")
                .defineInRange("block_stuck", -.09, -100.0, 100.0);
        m_dirtPath = builder
                .comment("Players moving on a dirt path or a carpet receive this amount of sanity per second")
                .defineInRange("dirt_path", .09, -100.0, 100.0);
        m_jukeboxPleasant = builder
                .comment("Nearby jukebox playing a pleasant melody gives this amount of sanity per second")
                .defineInRange("jukebox_pleasant", .08, -100.0, 100.0);
        m_jukeboxUnsettling = builder
                .comment("Nearby jukebox playing an unsettling melody gives this amount of sanity per second (this takes priority over pleasant melodies)")
                .defineInRange("jukebox_unsettling", -.11, -100.0, 100.0);

        List<String> path = new ArrayList<>();
        path.add("blocks");

        m_passiveBlocks = builder.comment(
                "Define a list of blocks that affect sanity of players standing near them",
                "A block should be included as follows: block_registry_name[property1=value1,property2=value2];A;B;C",
                "Where A is how much sanity is gained per second, B is a radius in blocks,",
                "C is whether a block needs to be naturally generated (not placed by player) (true/false)",
                "Supports boolean block state properties (can be omitted together with brackets)",
                "Prefix with TAG_ and follow with a tag registry name to define all blocks with the tag",
                "NOTE: not everything may work correctly with any configuration, e.g. multiblocks like tall flowers and beds; needs testing")
                .defineListAllowEmpty(path, ConfigDefault::passiveBlocksDefault, ConfigManager::stringEntryIsValid);

        builder.pop();
        builder.comment("Configuration for active sanity sources").push("active");

        m_sleeping = builder
                .comment("Sleeping restores this amount of sanity")
                .defineInRange("sleeping", 50.0, -100.0, 100.0);
        m_sleepingCd = builder
                .comment("Sleeping cooldown (see notes above), real time in seconds")
                .defineInRange("sleeping_cd", 1200.0, 0.0, Float.MAX_VALUE);
        m_hurtRatio = builder
                .comment("Players will gain sanity based on the damage they take from any sources with the ratio of 1 to this number")
                .defineInRange("hurt_ratio", -1.0, -100.0, 100.0);
        m_babyChickenSpawning = builder
                .comment("Spawning a baby chicken by throwing an egg restores this amount of sanity")
                .defineInRange("baby_chicken_spawn", 5.0, -100.0, 100.0);
        m_babyChickenSpawningCd = builder
                .comment("Spawning a baby chicken cooldown (see notes above), real time in seconds")
                .defineInRange("baby_chicken_spawn_cd", 300.0, 0.0, Float.MAX_VALUE);
        m_advancement = builder
                .comment("Earning an advancement gives this amount of sanity")
                .defineInRange("advancement", 20.0, -100.0, 100.0);
        m_animalBreeding = builder
                .comment("Breeding two animals together gives this amount of sanity")
                .defineInRange("animal_breeding", 9.0, -100.0, 100.0);
        m_animalBreedingCd = builder
                .comment("Breeding animals cooldown (see notes above), real time in seconds")
                .defineInRange("animal_breeding_cd", 600.0, 0.0, Float.MAX_VALUE);
        m_animalHurtRatio = builder
                .comment(
                        "Players gain this amount of sanity for every point of damage dealt to peaceful animals (incl. neutral ones)",
                        "This value is doubled for baby animals")
                .defineInRange("animal_hurt_ratio", -.5, -100.0, 100.0);
        m_petDeath = builder
                .comment("Players gain this amount of sanity upon their pets' death")
                .defineInRange("pet_death", -60.0, -100.0, 100.0);
        m_villagerTrade = builder
                .comment("Players gain this amount of sanity upon successfully trading with a villager")
                .defineInRange("villager_trade", 20.0, -100.0, 100.0);
        m_villagerTradeCd = builder
                .comment("Villager trade cooldown (see notes above), real time in seconds")
                .defineInRange("villager_trade_cd", 600.0, 0.0, Float.MAX_VALUE);
        m_shearing = builder
                .comment("Shearing an animal gives this amount of sanity")
                .defineInRange("shearing", 5.0, -100.0, 100.0);
        m_shearingCd = builder
                .comment("Shearing animals cooldown (see notes above), real time in seconds")
                .defineInRange("shearing_cd", 300.0, 0.0, Float.MAX_VALUE);
        m_eating = builder
                .comment("Consuming food gives this amount of sanity for every half-drumstick the meal restores")
                .defineInRange("eating", .8, -100.0, 100.0);
        m_eatingCd = builder
                .comment("Eating cooldown (see notes above), real time in seconds")
                .defineInRange("eating_cd", 300.0, 0.0, Float.MAX_VALUE);
        m_fishing = builder
                .comment("Fishing an item gives this amount of sanity")
                .defineInRange("fishing", 1.3, -100.0, 100.0);
        m_fishingCd = builder
                .comment("Fishing cooldown (see notes above), real time in seconds")
                .defineInRange("fishing_cd", 0.0, 0.0, Float.MAX_VALUE);
        m_farmlandTrample = builder
                .comment("Trampling a farmland gives this amount of sanity")
                .defineInRange("farmland_trample", -2.0, -100.0, 100.0);
        m_pottingFlower = builder
                .comment("Potting a flower gives this amount of sanity")
                .defineInRange("potting_flower", 4.0, -100.0, 100.0);
        m_pottingFlowerCd = builder
                .comment("Potting a flower cooldown (see notes above), real time in seconds")
                .defineInRange("potting_flower_cd", 300.0, 0.0, Float.MAX_VALUE);
        m_changedDimension = builder
                .comment("Teleporting to another dimension gives this amount of sanity")
                .defineInRange("changed_dimension", -10.0, -100.0, 100.0);
        m_struckByLightning = builder
                .comment("Players gain this amount of sanity upon being struck by lightning")
                .defineInRange("struck_by_lightning", -30.0, -100.0, 100.0);

        path.clear();
        path.add("items");

        m_items = builder.comment(
                "Define a list of items that will affect sanity upon their usage",
                "An item should be included as follows: item_registry_name;A;B",
                "Where A is how much sanity is gained upon usage and B is a custom category",
                "Items with the same categories share the same cooldown",
                "The sanity gained will be multiplied by (timeSinceLastUsage / categoryCooldown) capping at 1.0")
                .defineListAllowEmpty(path, ConfigDefault::itemsDefault, ConfigManager::stringEntryIsValid);

        path.clear();
        path.add("item_categories");

        m_itemCats = builder.comment(
                "Define a list of custom categories for items specified in <items>",
                "A category should be included as follows: A;B",
                "Where A is a category id (integer) and B is a cooldown (in seconds) all items in this category share")
                .defineListAllowEmpty(path, ConfigDefault::itemCatsDefault, ConfigManager::stringEntryIsValid);

        path.clear();
        path.add("broken_blocks");

        m_brokenBlocks = builder.comment(
                "Define a list of blocks that will affect sanity of players breaking them",
                "A block should be included as follows: block_registry_name;A;B;C;D",
                "Where A is how much sanity is gained upon breakage, B is a custom category,",
                "C is whether a block has to be naturally generated (not placed by players) (true/false),",
                "D is whether a block has to be mined with a correct tool (aka resources should drop) (true/false)",
                "Prefix with TAG_ and follow with a tag registry name to define all blocks with the tag",
                "Blocks with the same categories share the same cooldown",
                "The sanity gained will be multiplied by (timeSinceLastUsage / categoryCooldown) capping at 1.0",
                "NOTE: not everything may work correctly with any configuration, e.g. multiblocks like tall flowers and beds need testing")
                .defineListAllowEmpty(path, ConfigDefault::brokenBlocksDefault, ConfigManager::stringEntryIsValid);

        path.clear();
        path.add("broken_block_categories");

        m_brokenBlockCats = builder.comment(
                "Define a list of custom categories for blocks specified in <broken_blocks>",
                "A category should be included as follows: A;B",
                "Where A is a category id (integer) and B is a cooldown (in seconds) all blocks in this category share")
                .defineListAllowEmpty(path, ConfigDefault::brokenBlockCatsDefault, ConfigManager::stringEntryIsValid);

        builder.pop();
        builder.comment("Multiplayer configuration").push("multiplayer");

        m_sanePlayerCompany = builder
                .comment("Being around players with sanity higher than 50% gives this amount of sanity per second")
                .defineInRange("sane_player_company", .05, -100.0, 100.0);
        m_insanePlayerCompany = builder
                .comment("Being around players with sanity lower than 50% gives this amount of sanity per second")
                .defineInRange("insane_player_company", -.12, -100.0, 100.0);

        builder.pop();

        builder.comment("Entities configuration").push("entity");

        m_saneSeeInnerEntities = builder
                .comment(
                        "Whether sane players should be able to see and battle inner entities",
                        "Mobs will still be there server-side and will count towards passive sanity",
                        "Players who are targeted by inner entities see them regardless")
                .define("sane_see_inner_entities", false);

        builder.pop();

        builder.comment("Client configuration").push("client");
        builder.comment("Sanity indicator configuration").push("indicator");

        m_renderIndicator = builder
                .comment("Whether to render sanity indicator")
                .define("render", true);
        m_twitchIndicator = builder
                .comment("Whether to twitch sanity indicator at low sanity levels")
                .define("twitch", true);
        m_indicatorScale = builder
                .comment("Sanity indicator scale")
                .defineInRange("scale", 1.0, 0.0, Float.MAX_VALUE);
        m_indicatorLocation = builder
                .comment("Sanity indicator location")
                .defineEnum("location", SanityIndicatorLocation.HOTBAR_LEFT);

        builder.pop();
        builder.comment("Inner monologue configuration").push("hints");

        m_renderHint = builder
                .comment("Whether to render inner monologue/random thoughts")
                .define("render", true);
        m_twitchHint = builder
                .comment("Whether to twitch inner monologue/random thoughts at low sanity levels")
                .define("twitch", true);

        builder.pop();
        builder.comment("Blood tendrils overlay configuration").push("blood_tendrils");

        m_renderBloodTendrilsOverlay = builder
                .comment("Whether to render blood tendrils overlay")
                .define("render", true);
        m_flashBtOnShortBurst = builder
                .comment("Whether to flash blood tendrils overlay upon losing sanity in a short burst")
                .define("short_burst_flash", true);
        m_renderBtPassive = builder
                .comment("Whether to render blood tendrils overlay when passively losing sanity")
                .define("render_passive", true);

        builder.pop();

        m_renderPost = builder
                .comment("Whether to render sanity postprocessing effects")
                .define("render_post", true);
        m_playSounds = builder
                .comment("Whether to enable sanity sound effects")
                .define("play_sounds", true);
        m_insanityVolume = builder
                .comment("Insanity ambience max volume")
                .defineInRange("insanity_volume", .6, .0, 1.0);

        builder.pop();
    }

    private static List<String> passiveBlocksDefault()
    {
        List<String> list = new ArrayList<>();

        list.add("minecraft:campfire[lit=true];0.1;4;false");

        return list;
    }

    private static List<String> itemsDefault()
    {
        List<String> list = new ArrayList<>();

        list.add("minecraft:pufferfish;-5;0");
        list.add("minecraft:poisonous_potato;-5;0");
        list.add("minecraft:spider_eye;-5;0");
        list.add("minecraft:rotten_flesh;-5;0");
        list.add("minecraft:chorus_fruit;-3;0");
        list.add("minecraft:ender_pearl;-1;0");
        list.add("minecraft:honey_bottle;6;1");
        list.add("minecraft:golden_carrot;7;1");
        list.add("minecraft:golden_apple;8;1");
        list.add("minecraft:enchanted_golden_apple;13;1");

        return list;
    }

    private static List<String> itemCatsDefault()
    {
        List<String> list = new ArrayList<>();

        list.add("0;0");
        list.add("1;800.0");

        return list;
    }

    private static List<String> brokenBlocksDefault()
    {
        List<String> list = new ArrayList<>();

        list.add("minecraft:infested_stone;-8;0;false;false");
        list.add("minecraft:infested_cobblestone;-8;0;false;false");
        list.add("minecraft:infested_stone_bricks;-8;0;false;false");
        list.add("minecraft:infested_cracked_stone_bricks;-8;0;false;false");
        list.add("minecraft:infested_mossy_stone_bricks;-8;0;false;false");
        list.add("minecraft:infested_chiseled_stone_bricks;-8;0;false;false");
        list.add("minecraft:infested_deepslate;-8;0;false;false");

        return list;
    }

    private static List<String> brokenBlockCatsDefault()
    {
        List<String> list = new ArrayList<>();

        list.add("0;0");

        return list;
    }
}