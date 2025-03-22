package croissantnova.sanitydim.client;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class ItemTooltipHelper
{
    public static void showTooltipOnShift(List<Component> components, String itemId)
    {
        if (Screen.hasShiftDown()) {
            String tooltipKey = "item." + SanityMod.MOD_ID + "." + itemId + ".tooltip";
            String rawTooltip = Component.translatable(tooltipKey).getString();

            for (String line : rawTooltip.split("\n")) {
                components.add(Component.literal(line));
            }
        }
        else {
            components.add(Component.translatable("item." + SanityMod.MOD_ID + ".tooltip.press_shift"));
        }
    }
}