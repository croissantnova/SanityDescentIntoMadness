package croissantnova.sanitydim.config;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigProxy
{
    private static List<ConfigPassiveBlock> defPassiveBlocks = new ArrayList<>();
    private static List<ConfigItem> defItems = new ArrayList<>();
    private static List<ConfigItemCategory> defItemCats = new ArrayList<>();
    private static Map<Integer, ConfigItemCategory> defIdToItemCat = Maps.newHashMap();

    public static float getPosMul(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.posMul.containsKey(dimension)
                ? DimensionConfig.posMul.get(dimension).floatValue() : getDef().m_posMul.get().floatValue();
        return value;
    }

    public static float getNegMul(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.negMul.containsKey(dimension)
                ? DimensionConfig.negMul.get(dimension).floatValue() : getDef().m_negMul.get().floatValue();
        return value;
    }

    public static float getPassive(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.passive.containsKey(dimension)
                ? DimensionConfig.passive.get(dimension).floatValue() : getDef().m_passive.get().floatValue();
        return -value / 2000f;
    }

    public static float getRaining(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.raining.containsKey(dimension)
                ? DimensionConfig.raining.get(dimension).floatValue() : getDef().m_raining.get().floatValue();
        return -value / 2000f;
    }

    public static int getHungerThreshold(ResourceLocation dimension)
    {
        int value = dimension != null && DimensionConfig.hungerThreshold.containsKey(dimension)
                ? DimensionConfig.hungerThreshold.get(dimension).intValue() : getDef().m_hungerThreshold.get().intValue();
        return value;
    }

    public static float getHungry(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.hungry.containsKey(dimension)
                ? DimensionConfig.hungry.get(dimension).floatValue() : getDef().m_hungry.get().floatValue();
        return -value / 2000f;
    }

    public static float getEnderManAnger(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.enderManAnger.containsKey(dimension)
                ? DimensionConfig.enderManAnger.get(dimension).floatValue() : getDef().m_enderManAnger.get().floatValue();
        return -value / 2000f;
    }

    public static float getPet(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.pet.containsKey(dimension)
                ? DimensionConfig.pet.get(dimension).floatValue() : getDef().m_pet.get().floatValue();
        return -value / 2000f;
    }

    public static float getMonster(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.monster.containsKey(dimension)
                ? DimensionConfig.monster.get(dimension).floatValue() : getDef().m_monster.get().floatValue();
        return -value / 2000f;
    }

    public static float getDarkness(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.darkness.containsKey(dimension)
                ? DimensionConfig.darkness.get(dimension).floatValue() : getDef().m_darkness.get().floatValue();
        return -value / 2000f;
    }

    public static int getDarknessThreshold(ResourceLocation dimension)
    {
        int value = dimension != null && DimensionConfig.darknessThreshold.containsKey(dimension)
                ? DimensionConfig.darknessThreshold.get(dimension).intValue() : getDef().m_darknessThreshold.get().intValue();
        return value;
    }

    public static float getJukeboxPleasant(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.jukeboxPleasant.containsKey(dimension)
                ? DimensionConfig.jukeboxPleasant.get(dimension).floatValue() : getDef().m_jukeboxPleasant.get().floatValue();
        return -value / 2000f;
    }

    public static float getJukeboxUnsettling(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.jukeboxUnsettling.containsKey(dimension)
                ? DimensionConfig.jukeboxUnsettling.get(dimension).floatValue() : getDef().m_jukeboxUnsettling.get().floatValue();
        return -value / 2000f;
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
                ? DimensionConfig.sleeping.get(dimension).floatValue() : getDef().m_sleeping.get().floatValue();
        return -value / 100f;
    }

    public static int getSleepingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.sleepingCd.containsKey(dimension)
                ? DimensionConfig.sleepingCd.get(dimension).floatValue() : getDef().m_sleepingCd.get().floatValue();
        return Math.round(value * 20);
    }

    public static float getHurtRatio(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.hurtRatio.containsKey(dimension)
                ? DimensionConfig.hurtRatio.get(dimension).floatValue() : getDef().m_hurtRatio.get().floatValue();
        return -value / 100f;
    }

    public static float getBabyChickenSpawning(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.babyChickenSpawning.containsKey(dimension)
                ? DimensionConfig.babyChickenSpawning.get(dimension).floatValue() : getDef().m_babyChickenSpawning.get().floatValue();
        return -value / 100f;
    }

    public static int getBabyChickenSpawningCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.babyChickenSpawningCd.containsKey(dimension)
                ? DimensionConfig.babyChickenSpawningCd.get(dimension).floatValue() : getDef().m_babyChickenSpawningCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getAdvancement(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.advancement.containsKey(dimension)
                ? DimensionConfig.advancement.get(dimension).floatValue() : getDef().m_advancement.get().floatValue();
        return -value / 100f;
    }

    public static float getAnimalBreeding(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.animalBreeding.containsKey(dimension)
                ? DimensionConfig.animalBreeding.get(dimension).floatValue() : getDef().m_animalBreeding.get().floatValue();
        return -value / 100f;
    }

    public static int getAnimalBreedingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.animalBreedingCd.containsKey(dimension)
                ? DimensionConfig.animalBreedingCd.get(dimension).floatValue() : getDef().m_animalBreedingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getAnimalHurtRatio(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.animalHurtRatio.containsKey(dimension)
                ? DimensionConfig.animalHurtRatio.get(dimension).floatValue() : getDef().m_animalHurtRatio.get().floatValue();
        return -value / 100f;
    }

    public static float getPetDeath(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.petDeath.containsKey(dimension)
                ? DimensionConfig.petDeath.get(dimension).floatValue() : getDef().m_petDeath.get().floatValue();
        return -value / 100f;
    }

    public static float getVillagerTrade(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.villagerTrade.containsKey(dimension)
                ? DimensionConfig.villagerTrade.get(dimension).floatValue() : getDef().m_villagerTrade.get().floatValue();
        return -value / 100f;
    }

    public static int getVillagerTradeCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.villagerTradeCd.containsKey(dimension)
                ? DimensionConfig.villagerTradeCd.get(dimension).floatValue() : getDef().m_villagerTradeCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getShearing(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.shearing.containsKey(dimension)
                ? DimensionConfig.shearing.get(dimension).floatValue() : getDef().m_shearing.get().floatValue();
        return -value / 100f;
    }

    public static int getShearingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.shearingCd.containsKey(dimension)
                ? DimensionConfig.shearingCd.get(dimension).floatValue() : getDef().m_shearingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getEating(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.eating.containsKey(dimension)
                ? DimensionConfig.eating.get(dimension).floatValue() : getDef().m_eating.get().floatValue();
        return -value / 100f;
    }

    public static int getEatingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.eatingCd.containsKey(dimension)
                ? DimensionConfig.eatingCd.get(dimension).floatValue() : getDef().m_eatingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static float getFishing(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.fishing.containsKey(dimension)
                ? DimensionConfig.fishing.get(dimension).floatValue() : getDef().m_fishing.get().floatValue();
        return -value / 100f;
    }

    public static int getFishingCooldown(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.fishingCd.containsKey(dimension)
                ? DimensionConfig.fishingCd.get(dimension).floatValue() : getDef().m_fishingCd.get().floatValue();
        return Math.round(value * 20f);
    }

    public static List<ConfigItem> getItems(ResourceLocation dimension)
    {
        List<ConfigItem> value = dimension != null && DimensionConfig.itemsProcessed.containsKey(dimension)
                ? DimensionConfig.itemsProcessed.get(dimension) : defItems;
        return value;
    }

    public static List<ConfigItemCategory> getItemCats(ResourceLocation dimension)
    {
        List<ConfigItemCategory> value = dimension != null && DimensionConfig.itemCatsProcessed.containsKey(dimension)
                ? DimensionConfig.itemCatsProcessed.get(dimension) : defItemCats;
        return value;
    }

    public static Map<Integer, ConfigItemCategory> getIdToItemCat(ResourceLocation dimension)
    {
        Map<Integer, ConfigItemCategory> value = dimension != null && DimensionConfig.idToItemCat.containsKey(dimension)
                ? DimensionConfig.idToItemCat.get(dimension) : defIdToItemCat;
        return value;
    }

    public static float getSanePlayerCompany(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.sanePlayerCompany.containsKey(dimension)
                ? DimensionConfig.sanePlayerCompany.get(dimension).floatValue() : getDef().m_sanePlayerCompany.get().floatValue();
        return -value / 2000f;
    }

    public static float getInsanePlayerCompany(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.insanePlayerCompany.containsKey(dimension)
                ? DimensionConfig.insanePlayerCompany.get(dimension).floatValue() : getDef().m_insanePlayerCompany.get().floatValue();
        return -value / 2000f;
    }

    public static boolean getRenderIndicator(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.renderIndicator.containsKey(dimension)
                ? DimensionConfig.renderIndicator.get(dimension).booleanValue() : getDef().m_renderIndicator.get().booleanValue();
        return value;
    }

    public static boolean getTwitchIndicator(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.twitchIndicator.containsKey(dimension)
                ? DimensionConfig.twitchIndicator.get(dimension).booleanValue() : getDef().m_twitchIndicator.get().booleanValue();
        return value;
    }

    public static float getIndicatorScale(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.indicatorScale.containsKey(dimension)
                ? DimensionConfig.indicatorScale.get(dimension).floatValue() : getDef().m_indicatorScale.get().floatValue();
        return value;
    }

    public static SanityIndicatorLocation getIndicatorLocation(ResourceLocation dimension)
    {
        SanityIndicatorLocation value = dimension != null && DimensionConfig.indicatorLocation.containsKey(dimension)
                ? DimensionConfig.indicatorLocation.get(dimension) : getDef().m_indicatorLocation.get();
        return value;
    }

    public static boolean getRenderHint(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.renderHint.containsKey(dimension)
                ? DimensionConfig.renderHint.get(dimension).booleanValue() : getDef().m_renderHint.get().booleanValue();
        return value;
    }

    public static boolean getTwitchHint(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.twitchHint.containsKey(dimension)
                ? DimensionConfig.twitchHint.get(dimension).booleanValue() : getDef().m_twitchHint.get().booleanValue();
        return value;
    }

    public static boolean getRenderPost(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.renderPost.containsKey(dimension)
                ? DimensionConfig.renderPost.get(dimension).booleanValue() : getDef().m_renderPost.get().booleanValue();
        return value;
    }

    public static boolean getPlaySounds(ResourceLocation dimension)
    {
        boolean value = dimension != null && DimensionConfig.playSounds.containsKey(dimension)
                ? DimensionConfig.playSounds.get(dimension).booleanValue() : getDef().m_playSounds.get().booleanValue();
        return value;
    }

    public static float getInsanityVolume(ResourceLocation dimension)
    {
        float value = dimension != null && DimensionConfig.insanityVolume.containsKey(dimension)
                ? DimensionConfig.insanityVolume.get(dimension).floatValue() : getDef().m_insanityVolume.get().floatValue();
        return value;
    }

    public static ConfigDefault getDef()
    {
        return ConfigHandler.getDefault();
    }

    public static void onConfigLoading()
    {
        defPassiveBlocks = ConfigHandler.processPassiveBlocks(getDef().m_passiveBlocks.get());
        defItems = ConfigHandler.processItems(getDef().m_items.get());
        defItemCats = ConfigHandler.processItemCats(getDef().m_itemCats.get());
        defIdToItemCat = ConfigHandler.getMapFromCats(defItemCats);
    }
}