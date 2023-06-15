package croissantnova.sanitydim.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;

public abstract class BlockStateHelper
{
    @Nullable
    public static BooleanProperty getBooleanProperty(BlockState bs, String name)
    {
        for (Property<?> p : bs.getProperties())
        {
            if (p instanceof BooleanProperty bp && bp.getName().equalsIgnoreCase(name) && bs.hasProperty(p))
                return bp;
        }

        return null;
    }
}