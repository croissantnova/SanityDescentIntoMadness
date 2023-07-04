package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.ICompoundTagSerializable;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface ISanityLevelChunk extends ICompoundTagSerializable
{
    List<BlockPos> getArtificiallyPlacedBlocks();
}