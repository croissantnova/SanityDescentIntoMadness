package croissantnova.sanitydim.item;

import croissantnova.sanitydim.client.ItemTooltipHelper;
import croissantnova.sanitydim.item.material.FlowerArmorMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;

public class GarlandItem extends ArmorItem
{
    public GarlandItem()
    {
        super(new FlowerArmorMaterial(), ArmorItem.Type.HELMET, new Properties()
                .stacksTo(1)
                .setNoRepair());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced)
    {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        ItemTooltipHelper.showTooltipOnShift(pTooltipComponents, "garland");
    }
}