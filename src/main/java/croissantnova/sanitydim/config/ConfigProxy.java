package croissantnova.sanitydim.config;

import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Map;

public abstract class ConfigProxy
{
    public static float getPosMul(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.positive_multiplier", dim);
    }

    public static float getNegMul(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.negative_multiplier", dim);
    }

    public static float getPassive(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.passive", dim);
    }

    public static float getRaining(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.raining", dim);
    }

    public static int getHungerThreshold(ResourceLocation dim)
    {
        return ConfigManager.proxyi("sanity.passive.hunger_threshold", dim);
    }

    public static float getHungry(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.hungry", dim);
    }

    public static float getEnderManAnger(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.ender_man_anger", dim);
    }

    public static float getPet(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.pet", dim);
    }

    public static float getMonster(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.monster", dim);
    }

    public static float getDarkness(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.darkness", dim);
    }

    public static int getDarknessThreshold(ResourceLocation dim)
    {
        return ConfigManager.proxyi("sanity.passive.darkness_threshold", dim);
    }

    public static float getLightness(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.lightness", dim);
    }

    public static int getLightnessThreshold(ResourceLocation dim)
    {
        return ConfigManager.proxyi("sanity.passive.lightness_threshold", dim);
    }

    public static float getBlockStuck(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.block_stuck", dim);
    }

    public static float getDirtPath(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.dirt_path", dim);
    }

    public static float getJukeboxPleasant(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.jukebox_pleasant", dim);
    }

    public static float getJukeboxUnsettling(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.passive.jukebox_unsettling", dim);
    }

    public static List<ConfigPassiveBlock> getPassiveBlocks(ResourceLocation dim)
    {
        return ConfigManager.proxy("sanity.passive.blocks", dim);
    }

    public static float getSleeping(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.sleeping", dim);
    }

    public static int getSleepingCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.sleeping_cd", dim);
    }

    public static float getHurtRatio(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.hurt_ratio", dim);
    }

    public static float getBabyChickenSpawning(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.baby_chicken_spawn", dim);
    }

    public static int getBabyChickenSpawningCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.baby_chicken_spawn_cd", dim);
    }

    public static float getAdvancement(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.advancement", dim);
    }

    public static float getAnimalBreeding(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.animal_breeding", dim);
    }

    public static int getAnimalBreedingCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.animal_breeding_cd", dim);
    }

    public static float getAnimalHurtRatio(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.animal_hurt_ratio", dim);
    }

    public static float getPetDeath(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.pet_death", dim);
    }

    public static float getVillagerTrade(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.villager_trade", dim);
    }

    public static int getVillagerTradeCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.villager_trade_cd", dim);
    }

    public static float getShearing(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.shearing", dim);
    }

    public static int getShearingCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.shearing_cd", dim);
    }

    public static float getEating(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.eating", dim);
    }

    public static int getEatingCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.eating_cd", dim);
    }

    public static float getFishing(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.fishing", dim);
    }

    public static int getFishingCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.fishing_cd", dim);
    }

    public static float getFarmlandTrample(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.farmland_trample", dim);
    }

    public static float getPottingFlower(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.potting_flower", dim);
    }

    public static int getPottingFlowerCooldown(ResourceLocation dim)
    {
        return ConfigManager.proxyd2i("sanity.active.potting_flower_cd", dim);
    }

    public static float getChangedDimension(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.changed_dimension", dim);
    }

    public static float getStruckByLightning(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.active.struck_by_lightning", dim);
    }

    public static List<ConfigItem> getItems(ResourceLocation dim)
    {
        return ConfigManager.proxy("sanity.active.items", dim);
    }

    public static List<ConfigItemCategory> getItemCats(ResourceLocation dim)
    {
        return ConfigManager.proxy("sanity.active.item_categories", dim);
    }

    public static Map<Integer, ConfigItemCategory> getIdToItemCat(ResourceLocation dim)
    {
        return ConfigManager.getIdToItemCat(dim);
    }

    public static List<ConfigBrokenBlock> getBrokenBlocks(ResourceLocation dim)
    {
        return ConfigManager.proxy("sanity.active.broken_blocks", dim);
    }

    public static List<ConfigBrokenBlockCategory> getBrokenBlockCats(ResourceLocation dim)
    {
        return ConfigManager.proxy("sanity.active.broken_block_categories", dim);
    }

    public static Map<Integer, ConfigBrokenBlockCategory> getIdToBrokenBlockCat(ResourceLocation dim)
    {
        return ConfigManager.getIdToBrokenBlockCat(dim);
    }

    public static float getSanePlayerCompany(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.multiplayer.sane_player_company", dim);
    }

    public static float getInsanePlayerCompany(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.multiplayer.insane_player_company", dim);
    }

    public static boolean getSaneSeeInnerEntities(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.entity.sane_see_inner_entities", dim);
    }

    public static boolean getRenderIndicator(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.indicator.render", dim);
    }

    public static boolean getTwitchIndicator(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.indicator.twitch", dim);
    }

    public static float getIndicatorScale(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.client.indicator.scale", dim);
    }

    public static SanityIndicatorLocation getIndicatorLocation(ResourceLocation dim)
    {
        return ConfigManager.proxy("sanity.client.indicator.location", dim);
    }

    public static boolean getRenderHint(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.hints.render", dim);
    }

    public static boolean getTwitchHint(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.hints.twitch", dim);
    }

    public static boolean getRenderBtOverlay(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.blood_tendrils.render", dim);
    }

    public static boolean getFlashBtOnShortBurst(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.blood_tendrils.short_burst_flash", dim);
    }

    public static boolean getRenderBtPassive(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.blood_tendrils.render_passive", dim);
    }

    public static boolean getRenderPost(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.render_post", dim);
    }

    public static boolean getPlaySounds(ResourceLocation dim)
    {
        return ConfigManager.proxyb("sanity.client.play_sounds", dim);
    }

    public static float getInsanityVolume(ResourceLocation dim)
    {
        return ConfigManager.proxyd2f("sanity.client.insanity_volume", dim);
    }
}