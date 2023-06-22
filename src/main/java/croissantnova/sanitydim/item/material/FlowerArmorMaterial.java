package croissantnova.sanitydim.item.material;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;

public class FlowerArmorMaterial implements IArmorMaterial
{
    @Override
    public int getDefenseForSlot(@Nonnull EquipmentSlotType slot)
    {
        return 0;
    }

    @Override
    public int getDurabilityForSlot(@Nonnull EquipmentSlotType slot)
    {
        return 150;
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