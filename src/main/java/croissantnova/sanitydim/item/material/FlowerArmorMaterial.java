package croissantnova.sanitydim.item.material;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class FlowerArmorMaterial implements ArmorMaterial
{
    @Override
    public int getDefenseForType(ArmorItem.@NotNull Type type)
    {
        return 0;
    }

    @Override
    public int getDurabilityForType(ArmorItem.@NotNull Type type)
    {
        return 150;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 0;
    }

    @Override
    @NotNull
    public SoundEvent getEquipSound()
    {
        return SoundRegistry.FLOWERS_EQUIP.get();
    }

    @Override
    @NotNull
    public Ingredient getRepairIngredient()
    {
        return Ingredient.of(ItemTags.SMALL_FLOWERS);
    }

    @Override
    @NotNull
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