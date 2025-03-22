package croissantnova.sanitydim.sources.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class WornArmorSanitySource implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        float sanityChange = 0f;
        float configValue = ConfigManager.getConfigValues().passiveConfig.wornArmor.get(dim).floatValue();

        for (ItemStack armorItem : player.getArmorSlots()) {
            float maxDamage = armorItem.getMaxDamage();
            if (maxDamage <= 0) {
                continue;
            }

            float durabilityPercent = armorItem.getDamageValue() / maxDamage;
            sanityChange += configValue * durabilityPercent;
        }

        return sanityChange;
    }
}