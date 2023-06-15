package croissantnova.sanitydim.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class ConfigProxy
{
    private static List<ConfigPassiveBlock> defPassiveBlocks = Lists.newArrayList();

    public static float getPosMul(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.posMul.containsKey(dimension)
                ? DimensionConfig.posMul.get(dimension).floatValue() : getDef().posMul.get().floatValue();
        return value;
    }

    public static float getNegMul(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.negMul.containsKey(dimension)
                ? DimensionConfig.negMul.get(dimension).floatValue() : getDef().negMul.get().floatValue();
        return value;
    }

    public static float getPassive(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.passive.containsKey(dimension)
                ? DimensionConfig.passive.get(dimension).floatValue() : getDef().passive.get().floatValue();
        return -value / 2000f;
    }

    public static float getRaining(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.raining.containsKey(dimension)
                ? DimensionConfig.raining.get(dimension).floatValue() : getDef().raining.get().floatValue();
        return -value / 2000f;
    }

    public static int getHungerThreshold(ResourceLocation dimension)
    {
        int value = dimension != null && DimensionConfig.hungerThreshold.containsKey(dimension)
                ? DimensionConfig.hungerThreshold.get(dimension).intValue() : getDef().hungerThreshold.get().intValue();
        return value;
    }

    public static float getHungry(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.hungry.containsKey(dimension)
                ? DimensionConfig.hungry.get(dimension).floatValue() : getDef().hungry.get().floatValue();
        return -value / 2000f;
    }

    public static float getEnderManAnger(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.enderManAnger.containsKey(dimension)
                ? DimensionConfig.enderManAnger.get(dimension).floatValue() : getDef().enderManAnger.get().floatValue();
        return -value / 2000f;
    }

    public static float getPet(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.pet.containsKey(dimension)
                ? DimensionConfig.pet.get(dimension).floatValue() : getDef().pet.get().floatValue();
        return -value / 2000f;
    }

    public static float getMonster(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.monster.containsKey(dimension)
                ? DimensionConfig.monster.get(dimension).floatValue() : getDef().monster.get().floatValue();
        return -value / 2000f;
    }

    public static float getDarkness(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.darkness.containsKey(dimension)
                ? DimensionConfig.darkness.get(dimension).floatValue() : getDef().darkness.get().floatValue();
        return -value / 2000f;
    }

    public static int getDarknessThreshold(ResourceLocation dimension)
    {
        int value = dimension != null && DimensionConfig.darknessThreshold.containsKey(dimension)
                ? DimensionConfig.darknessThreshold.get(dimension).intValue() : getDef().darknessThreshold.get().intValue();
        return value;
    }

    public static List<ConfigPassiveBlock> getPassiveBlocks(ResourceLocation dimension)
    {
        List<ConfigPassiveBlock> value = dimension != null && DimensionConfig.passiveBlocksProcessed.containsKey(dimension)
                ? DimensionConfig.passiveBlocksProcessed.get(dimension) : defPassiveBlocks;
        return value;
    }

    public static float getSleeping(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.sleeping.containsKey(dimension)
                ? DimensionConfig.sleeping.get(dimension).floatValue() : getDef().sleeping.get().floatValue();
        return -value / 100f;
    }

    public static int getSleepingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.sleepingCd.containsKey(dimension)
                ? DimensionConfig.sleepingCd.get(dimension).floatValue() : getDef().sleepingCd.get().floatValue();
        return Math.round(value * 20);
    }

    public static float getHurtRatio(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.hurtRatio.containsKey(dimension)
                ? DimensionConfig.hurtRatio.get(dimension).floatValue() : getDef().hurtRatio.get().floatValue();
        return -value / 100f;
    }

    public static float getBabyChickenSpawning(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.babyChickenSpawning.containsKey(dimension)
                ? DimensionConfig.babyChickenSpawning.get(dimension).floatValue() : getDef().babyChickenSpawning.get().floatValue();
        return -value / 100f;
    }

    public static int getBabyChickenSpawningCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.babyChickenSpawningCd.containsKey(dimension)
                ? DimensionConfig.babyChickenSpawningCd.get(dimension).floatValue() : getDef().babyChickenSpawningCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getAdvancement(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.advancement.containsKey(dimension)
                ? DimensionConfig.advancement.get(dimension).floatValue() : getDef().advancement.get().floatValue();
        return -value / 100f;
    }

    public static float getAnimalBreeding(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.animalBreeding.containsKey(dimension)
                ? DimensionConfig.animalBreeding.get(dimension).floatValue() : getDef().animalBreeding.get().floatValue();
        return -value / 100f;
    }

    public static int getAnimalBreedingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.animalBreedingCd.containsKey(dimension)
                ? DimensionConfig.animalBreedingCd.get(dimension).floatValue() : getDef().animalBreedingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getAnimalHurtRatio(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.animalHurtRatio.containsKey(dimension)
                ? DimensionConfig.animalHurtRatio.get(dimension).floatValue() : getDef().animalHurtRatio.get().floatValue();
        return -value / 100f;
    }

    public static float getPetDeath(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.petDeath.containsKey(dimension)
                ? DimensionConfig.petDeath.get(dimension).floatValue() : getDef().petDeath.get().floatValue();
        return -value / 100f;
    }

    public static float getVillagerTrade(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.villagerTrade.containsKey(dimension)
                ? DimensionConfig.villagerTrade.get(dimension).floatValue() : getDef().villagerTrade.get().floatValue();
        return -value / 100f;
    }

    public static int getVillagerTradeCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.villagerTradeCd.containsKey(dimension)
                ? DimensionConfig.villagerTradeCd.get(dimension).floatValue() : getDef().villagerTradeCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getShearing(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.shearing.containsKey(dimension)
                ? DimensionConfig.shearing.get(dimension).floatValue() : getDef().shearing.get().floatValue();
        return -value / 100f;
    }

    public static int getShearingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.shearingCd.containsKey(dimension)
                ? DimensionConfig.shearingCd.get(dimension).floatValue() : getDef().shearingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getEating(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.eating.containsKey(dimension)
                ? DimensionConfig.eating.get(dimension).floatValue() : getDef().eating.get().floatValue();
        return -value / 100f;
    }

    public static int getEatingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.eatingCd.containsKey(dimension)
                ? DimensionConfig.eatingCd.get(dimension).floatValue() : getDef().eatingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static boolean getRenderIndicator(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.renderIndicator.containsKey(dimension)
                ? DimensionConfig.renderIndicator.get(dimension).booleanValue() : getDef().renderIndicator.get().booleanValue();
        return value;
    }

    public static boolean getTwitchIndicator(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.twitchIndicator.containsKey(dimension)
                ? DimensionConfig.twitchIndicator.get(dimension).booleanValue() : getDef().twitchIndicator.get().booleanValue();
        return value;
    }

    public static float getIndicatorScale(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.indicatorScale.containsKey(dimension)
                ? DimensionConfig.indicatorScale.get(dimension).floatValue() : getDef().indicatorScale.get().floatValue();
        return value;
    }

    public static boolean getRenderHint(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.renderHint.containsKey(dimension)
                ? DimensionConfig.renderHint.get(dimension).booleanValue() : getDef().renderHint.get().booleanValue();
        return value;
    }

    public static boolean getTwitchHint(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.twitchHint.containsKey(dimension)
                ? DimensionConfig.twitchHint.get(dimension).booleanValue() : getDef().twitchHint.get().booleanValue();
        return value;
    }

    public static boolean getRenderPost(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.renderPost.containsKey(dimension)
                ? DimensionConfig.renderPost.get(dimension).booleanValue() : getDef().renderPost.get().booleanValue();
        return value;
    }

    public static boolean getPlaySounds(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.playSounds.containsKey(dimension)
                ? DimensionConfig.playSounds.get(dimension).booleanValue() : getDef().playSounds.get().booleanValue();
        return value;
    }

    public static float getInsanityVolume(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.insanityVolume.containsKey(dimension)
                ? DimensionConfig.insanityVolume.get(dimension).floatValue() : getDef().insanityVolume.get().floatValue();
        return value;
    }

    public static ConfigDefault getDef()
    {
        return ConfigHandler.getDefault();
    }

    public static void onConfigLoading(final ModConfigEvent.Loading event)
    {
        defPassiveBlocks = ConfigHandler.processPassiveBlocks(getDef().passiveBlocks.get());
    }
}