package croissantnova.sanitydim.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class SanityLevelChunk implements ISanityLevelChunk
{
    private final List<BlockPos> m_blocksPlacedByPlayer = new ArrayList<>();

    @Override
    public List<BlockPos> getArtificiallyPlacedBlocks()
    {
        return m_blocksPlacedByPlayer;
    }

    @Override
    public void serializeNBT(CompoundTag tag)
    {
        int size = m_blocksPlacedByPlayer.size();
        long[] arr = new long[size];
        for (int i = 0; i < size; ++i)
        {
            arr[i] = m_blocksPlacedByPlayer.get(i).asLong();
        }
        tag.putLongArray("sanity.blocks_placed_by_player", arr);
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        long[] arr = tag.getLongArray("sanity.blocks_placed_by_player");
        m_blocksPlacedByPlayer.clear();
        for (int i = 0; i < arr.length / 3; ++i)
        {
            m_blocksPlacedByPlayer.add(BlockPos.of(arr[i]));
        }
    }
}