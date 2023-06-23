package croissantnova.sanitydim.item.material;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nonnull;

public class FlowerArmorMaterial implements ArmorMaterial
{
    @Override
    public int getDurabilityForSlot(@Nonnull EquipmentSlot pSlot)
    {
        return 150;
    }

    @Override
    public int getDefenseForSlot(@Nonnull EquipmentSlot pSlot)
    {
        return 0;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 0;
    }

    @Override
    @Nonnull
    public SoundEvent getEquipSound()
    {
        return SoundRegistry.FLOWERS_EQUIP.get();
    }

    @Override
    @Nonnull
    public Ingredient getRepairIngredient()
    {
        return Ingredient.of(ItemTags.SMALL_FLOWERS);
    }

    @Override
    @Nonnull
    public String getName()
    {
        return SanityMod.MODID + ":flower";
    }

    @Override
    public float getToughness()
    {
        return 0;
    }

    @Override
    public float getKnockbackResistance()
    {
        return 0;
    }
}