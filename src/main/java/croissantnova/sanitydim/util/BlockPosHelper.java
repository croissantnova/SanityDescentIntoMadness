package croissantnova.sanitydim.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public abstract class BlockPosHelper
{
    public static Vector3d getCenter(BlockPos blockPos)
    {
        return new Vector3d(blockPos.getX() + .5f, blockPos.getY() + .5f, blockPos.getZ() + .5f);
    }
}