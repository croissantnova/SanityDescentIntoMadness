package croissantnova.sanitydim.config.registry;

import croissantnova.sanitydim.config.custom.PassiveSanityEntity;
import croissantnova.sanitydim.config.custom.PassiveSanityEntityProcessor;
import croissantnova.sanitydim.config.custom.PassiveSanityStatusEffect;
import croissantnova.sanitydim.config.custom.PassiveSanityStatusEffectProcessor;
import croissantnova.sanitydim.config.value.ModConfigProcessableValue;
import croissantnova.sanitydim.config.value.ModConfigValue;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PassiveConfig {

    public final ModConfigProcessableValue<List<? extends String>, List<PassiveSanityEntity>> sanityEntities = ModConfigProcessableValue.createListAllowEmpty(
            "sanity.passive.entities",
            PassiveSanityEntityProcessor::processList,
            DefaultConfig.PASSIVE_SANITY_ENTITIES.getElements(),
            "Define a list of entities that affect sanity of players standing near them",
            "An entity should be included as follows: A;B;C",
            "A = entity registry name (e.g. minecraft:enderman)",
            "B = how much sanity is gained per second passively",
            "C = radius (in blocks as a double)"
    );

    public final ModConfigProcessableValue<List<? extends String>, List<PassiveSanityStatusEffect>> statusEffects = ModConfigProcessableValue.createListAllowEmpty(
            "sanity.passive.status_effects",
            PassiveSanityStatusEffectProcessor.INSTANCE::processList,
            DefaultConfig.STATUS_EFFECTS.getElements(),
            "Define a list of status effects that affect sanity of players who have them",
            "A status effect should be included as follows: A;B;C",
            "A = status effect registry name (e.g. minecraft:blindness)",
            "B = how much sanity is gained per second passively",
            "C = the minimum amplifier in order to get the sanity effect",
            "Note: The defined status effect with the highest amplifier that matches the player's current amplifier is chosen"
    );

    public final ModConfigValue<Double> wellHydrated = ModConfigValue.createPassiveDouble(
            "sanity.passive.well_hydrated",
            0.05,
            "Defines how much sanity is gained per second when the player is well hydrated.",
            "This value applies only when the player's hydration level is above the threshold."
    );

    public final ModConfigValue<Double> wellHydratedThreshold = ModConfigValue.createDouble(
            "sanity.passive.well_hydrated_threshold",
            20,
            0.0,
            20.0,
            "Defines what hydration level the player must have to be considered well-hydrated.",
            "Players with their hydration levels at or above this threshold gain bonus sanity per second."
    );

    public final ModConfigValue<Double> wellFed = ModConfigValue.createPassiveDouble(
            "sanity.passive.well_fed",
            0.05,
            "Defines how much sanity is gained per second when the player is well fed.",
            "This value applies only when the player's food level is above the threshold."
    );

    public final ModConfigValue<Double> wellFedThreshold = ModConfigValue.createDouble(
            "sanity.passive.well_fed_threshold",
            20,
            0.0,
            20.0,
            "Defines what food level the player must have to be considered well-fed.",
            "Players with their food levels at or above this threshold gain bonus sanity per second."
    );

    public final ModConfigValue<Double> lowHydration = ModConfigValue.createPassiveDouble(
            "sanity.passive.low_hydration",
            -0.2,
            "Defines how much sanity is gained per second when the player is low on hydration.",
            "This value applies only when the player's hydration level is at or below the threshold."
    );

    public final ModConfigValue<Double> sunlight = ModConfigValue.createPassiveDouble(
            "sanity.passive.sunlight",
            0.05,
            "Defines how much sanity is gained per second when the player is in the sunlight."
    );

    public final ModConfigValue<Double> wornArmor = ModConfigValue.createPassiveDouble(
            "sanity.passive.worn_armor",
            -0.1,
            "Defines the sanity effect when wearing damaged armor.",
            "The effect scales with the armor's damage percentage (e.g., 80% damage applies 80% of the value).",
            "Applied individually to each armor piece (e.g., four pieces at 50% damage will net a 200% modifier on the defined value)."
    );

    PassiveConfig(ForgeConfigSpec.Builder builder) {
        sanityEntities.build(builder);
        statusEffects.build(builder);
        wellHydrated.build(builder);
        wellHydratedThreshold.build(builder);
        wellFed.build(builder);
        wellFedThreshold.build(builder);
        lowHydration.build(builder);
        sunlight.build(builder);
        wornArmor.build(builder);
    }
}
