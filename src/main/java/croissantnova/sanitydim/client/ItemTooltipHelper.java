package croissantnova.sanitydim.client;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public abstract class ItemTooltipHelper
{
    public static void showTooltipOnShift(List<ITextComponent> components, String itemId)
    {
        if (Screen.hasShiftDown())
            components.add(new TranslationTextComponent("item." + SanityMod.MODID + "." + itemId + ".tooltip"));
        else
            components.add(new TranslationTextComponent("item." + SanityMod.MODID + ".tooltip.press_shift"));
    }
}