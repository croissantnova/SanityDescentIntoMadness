package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.ICompoundTagSerializable;

public interface ISanity extends ICompoundTagSerializable
{
    float getSanity();

    void setSanity(float value);
}