package croissantnova.sanitydim.item;

import croissantnova.sanitydim.client.ItemTooltipHelper;
import croissantnova.sanitydim.item.material.FlowerArmorMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class GarlandItem extends ArmorItem
{
    public GarlandItem()
    {
        super(new FlowerArmorMaterial(), EquipmentSlotType.HEAD, new Properties()
                .stacksTo(1)
                .setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag)
    {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        ItemTooltipHelper.showTooltipOnShift(pTooltip, "garland");
    }
}