package croissantnova.sanitydim.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public abstract class BlockPosHelper
{
    public static Vec3 getCenter(BlockPos blockPos)
    {
        return new Vec3(blockPos.getX() + .5f, blockPos.getY() + .5f, blockPos.getZ() + .5f);
    }
}