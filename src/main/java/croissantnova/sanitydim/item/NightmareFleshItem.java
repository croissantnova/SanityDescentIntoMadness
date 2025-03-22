package croissantnova.sanitydim.item;

import croissantnova.sanitydim.client.ItemTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class NightmareFleshItem extends Item {

    public NightmareFleshItem() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced)
    {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        ItemTooltipHelper.showTooltipOnShift(pTooltipComponents, "nightmare_flesh");
    }
}
