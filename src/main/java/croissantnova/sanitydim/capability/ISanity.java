package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.ICompoundTagSerializable;
import net.minecraft.resources.ResourceLocation;

public interface ISanity extends ICompoundTagSerializable
{
    float getSanity();

    void setSanity(float value);
}