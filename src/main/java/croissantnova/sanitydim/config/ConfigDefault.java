package croissantnova.sanitydim.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class ConfigDefault
{
    public final DoubleValue posMul;
    public final DoubleValue negMul;

    public final DoubleValue passive;
    public final DoubleValue raining;
    public final IntValue hungerThreshold;
    public final DoubleValue hungry;
    public final DoubleValue enderManAnger;
    public final DoubleValue pet;
    public final DoubleValue monster;
    public final DoubleValue darkness;
    public final IntValue darknessThreshold;
    public final ConfigValue<List<? extends String>> passiveBlocks;

    public final DoubleValue sleeping;
    public final DoubleValue sleepingCd;
    public final DoubleValue hurtRatio;
    public final DoubleValue babyChickenSpawning;
    public final DoubleValue babyChickenSpawningCd;
    public final DoubleValue advancement;
    public final DoubleValue animalBreeding;
    public final DoubleValue animalBreedingCd;
    public final DoubleValue animalHurtRatio;
    public final DoubleValue petDeath;
    public final DoubleValue villagerTrade;
    public final DoubleValue villagerTradeCd;
    public final DoubleValue shearing;
    public final DoubleValue shearingCd;
    public final DoubleValue eating;
    public final DoubleValue eatingCd;

    public final DoubleValue sanePlayerCompany;
    public final DoubleValue insanePlayerCompany;

    public final BooleanValue renderIndicator;
    public final BooleanValue twitchIndicator;
    public final DoubleValue indicatorScale;
    public final BooleanValue renderHint;
    public final BooleanValue twitchHint;

    public final BooleanValue renderPost;
    public final BooleanValue playSounds;
    public final DoubleValue insanityVolume;

    public ConfigDefault(ForgeConfigSpec.Builder builder)
    {
        builder.comment("Sanity configuration",
                "NOTE: all sanity values are measured in percentages (i.e. 40.0 is equal to 40% of sanity bar)",
                "NOTE: each subsequent usage of an active source or item has its effectiveness multiplied by (timeSinceLastUsage / cooldown) (capped at 1.0)").push("sanity");

        posMul = builder
                .comment("For balancing purposes: the effectiveness of all positive sanity sources will be multiplied by this number")
                .defineInRange("positive_multiplier", 1.0, Float.MIN_VALUE, Float.MAX_VALUE);
        negMul = builder
                .comment("For balancing purposes: the effectiveness of all negative sanity sources will be multiplied by this number")
                .defineInRange("negative_multiplier", 1.0, Float.MIN_VALUE, Float.MAX_VALUE);

        builder.comment("Configuration for passive sanity sources").push("passive");

        passive = builder
                .comment("This value will be added to sanity each second regardless of any other factors")
                .defineInRange("passive", .0, -100.0, 100.0);
        raining = builder
                .comment("Sanity gain per second during rainy weather or in water")
                .defineInRange("raining", -.2, -100.0, 100.0);
        hungerThreshold = builder
                .comment("Players' sanity will start getting affected with food levels at and below this threshold (in half-drumsticks)")
                .defineInRange("hunger_threshold", 8, 0, 20);
        hungry = builder
                .comment("Players with food levels at and below <hunger_threshold> gain this amount of sanity per second")
                .defineInRange("hungry", -.2, -100.0, 100.0);
        enderManAnger = builder
                .comment("Players will gain this amount of sanity per second for 5 seconds after looking at an enderman")
                .defineInRange("ender_man_anger", -4.0, -100.0, 100.0);
        pet = builder
                .comment("Players will gain this amount of sanity per second while being near their pets")
                .defineInRange("pet", .15, -100.0, 100.0);
        monster = builder
                .comment("Players will gain this amount of sanity per second while being near any monsters")
                .comment("This value is doubled if the monster is aggressive towards the player")
                .defineInRange("monster", -.1, -100.0, 100.0);
        darkness = builder
                .comment("Players will gain this amount of sanity per second while being in the dark")
                .defineInRange("darkness", -.15, -100.0, 100.0);
        darknessThreshold = builder
                .comment("Maximum light level considered to be darkness (inclusive)")
                .defineInRange("darkness_threshold", 4, 0, 15);

        List<String> blocksPath = Lists.newArrayList();
        blocksPath.add("blocks");

        passiveBlocks = builder
                .comment("Specify a list of blocks that affect sanity of players standing near them")
                .comment("A block should be included as follows: block_registry_name[property1=value1,property2=value2];A;B")
                .comment("Where A is the sanity gain per second and B is the radius in blocks")
                .comment("Supports boolean block state properties (can be omitted together with brackets)")
                .defineListAllowEmpty(blocksPath, ConfigDefault::passiveBlocksDefault, ConfigHandler::stringEntryIsValid);

        builder.pop();
        builder.comment("Configuration for active sanity sources").push("active");

        sleeping = builder
                .comment("Sleeping restores this amount of sanity")
                .defineInRange("sleeping", 50.0, -100.0, 100.0);
        sleepingCd = builder
                .comment("Sleeping cooldown (see notes above), real time in seconds")
                .defineInRange("sleeping_cd", 1200.0, 0.0, Float.MAX_VALUE);
        hurtRatio = builder
                .comment("Players will gain sanity based on the damage they take from any sources with the ratio of 1 to this number")
                .defineInRange("hurt_ratio", -1.0, -100.0, 100.0);
        babyChickenSpawning = builder
                .comment("Spawning a baby chicken by throwing an egg restores this amount of sanity")
                .defineInRange("baby_chicken_spawn", 5.0, -100.0, 100.0);
        babyChickenSpawningCd = builder
                .comment("Spawning a baby chicken cooldown (see notes above), real time in seconds")
                .defineInRange("baby_chicken_spawn_cd", 300.0, 0.0, Float.MAX_VALUE);
        advancement = builder
                .comment("Earning an advancement gives this amount of sanity")
                .defineInRange("advancement", 20.0, -100.0, 100.0);
        animalBreeding = builder
                .comment("Breeding two animals together gives this amount of sanity")
                .defineInRange("animal_breeding", 15.0, -100.0, 100.0);
        animalBreedingCd = builder
                .comment("Breeding animals cooldown (see notes above), real time in seconds")
                .defineInRange("animal_breeding_cd", 600.0, 0.0, Float.MAX_VALUE);
        animalHurtRatio = builder
                .comment("Players gain this amount of sanity for every point of damage dealt to peaceful animals (incl. neutral ones)")
                .comment("This value is doubled for baby animals")
                .defineInRange("animal_hurt_ratio", -.5, -100.0, 100.0);
        petDeath = builder
                .comment("Players gain this amount of sanity upon their pets' death")
                .defineInRange("pet_death", -50.0, -100.0, 100.0);
        villagerTrade = builder
                .comment("Players gain this amount of sanity upon successfully trading with a villager")
                .defineInRange("villager_trade", 20.0, -100.0, 100.0);
        villagerTradeCd = builder
                .comment("Villager trade cooldown (see notes above), real time in seconds")
                .defineInRange("villager_trade_cd", 600.0, 0.0, Float.MAX_VALUE);
        shearing = builder
                .comment("Shearing an animal gives this amount of sanity")
                .defineInRange("shearing", 5.0, -100.0, 100.0);
        shearingCd = builder
                .comment("Shearing animals cooldown (see notes above), real time in seconds")
                .defineInRange("shearing_cd", 300.0, 0.0, Float.MAX_VALUE);
        eating = builder
                .comment("Consuming food gives this amount of sanity for every half-drumstick restored")
                .defineInRange("eating", .8, -100.0, 100.0);
        eatingCd = builder
                .comment("Eating cooldown (see notes above), real time in seconds")
                .defineInRange("eating_cd", 300.0, 0.0, Float.MAX_VALUE);

        builder.comment("Multiplayer configuration").push("multiplayer");

        sanePlayerCompany = builder
                .comment("Being around players with sanity higher than 50% gives this amount of sanity per second")
                .defineInRange("sane_player_company", .05, -100.0, 100.0);
        insanePlayerCompany = builder
                .comment("Being around players with sanity lower than 50% gives this amount of sanity per second")
                .defineInRange("insane_player_company", -.12, -100.0, 100.0);

        builder.pop();

        builder.pop();
        builder.comment("Client configuration").push("client");

        builder.comment("Sanity indicator configuration").push("indicator");

        renderIndicator = builder
                .comment("Whether to render sanity indicator")
                .define("render", true);
        twitchIndicator = builder
                .comment("Whether to twitch sanity indicator at low sanity levels")
                .define("twitch", true);
        indicatorScale = builder
                .comment("Sanity indicator's scale")
                .defineInRange("scale", 1.0, 0.0, Float.MAX_VALUE);

        builder.pop();

        builder.comment("Inner monologue configuration").push("hints");

        renderHint = builder
                .comment("Whether to render inner monologue/random thoughts")
                .define("render", true);
        twitchHint = builder
                .comment("Whether to twitch inner monologue/random thoughts at low sanity levels")
                .define("twitch", true);

        builder.pop();

        renderPost = builder
                .comment("Whether to render sanity postprocessing effects")
                .define("render_post", true);
        playSounds = builder
                .comment("Whether to enable sanity sound effects")
                .define("play_sounds", true);
        insanityVolume = builder
                .comment("Insanity ambience max volume")
                .defineInRange("insanity_volume", .6, .0, 1.0);

        builder.pop();
    }

    private static List<String> passiveBlocksDefault()
    {
        List<String> list = Lists.newArrayList();

        list.add("minecraft:campfire[lit=true];0.1;4");

        return list;
    }
}