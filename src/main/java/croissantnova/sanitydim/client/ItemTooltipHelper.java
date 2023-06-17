package croissantnova.sanitydim.client;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.List;

public abstract class ItemTooltipHelper
{
    public static void showTooltipOnShift(List<Component> components, String itemId)
    {
        if (Screen.hasShiftDown())
            components.add(Component.translatable("item." + SanityMod.MODID + "." + itemId + ".tooltip"));
        else
            components.add(Component.translatable("item." + SanityMod.MODID + ".tooltip.press_shift"));
    }
}