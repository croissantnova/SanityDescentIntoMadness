package croissantnova.sanitydim.block;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import javax.annotation.Nullable;

public abstract class BlockStateHelper
{
    @Nullable
    public static BooleanProperty getBooleanProperty(BlockState bs, String name)
    {
        for (Property<?> p : bs.getProperties())
        {
            if (p instanceof BooleanProperty && p.getName().equalsIgnoreCase(name) && bs.hasProperty(p))
                return (BooleanProperty)p;
        }

        return null;
    }
}