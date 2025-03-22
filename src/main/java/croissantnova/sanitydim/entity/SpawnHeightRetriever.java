package croissantnova.sanitydim.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

record SpawnHeightRetriever(@NotNull Level level, @NotNull BlockPos blockPos, int radius) {

    public static @NotNull Optional<Integer> getHeightForSpawning(@NotNull Level level, @NotNull BlockPos blockPos, int spawnRadius) {
        SpawnHeightRetriever retriever = new SpawnHeightRetriever(level, blockPos, spawnRadius);
        return retriever.tryGettingYOfSolidBlockWithAirAbove()
                .or(retriever::tryGettingYOfAirBlockWithSolidBelow);
    }

    private Optional<Integer> tryGettingYOfSolidBlockWithAirAbove() {
        BlockPos.MutableBlockPos mutable = blockPos.mutable();
        for (int i = 0; i < radius; i++) {
            if (isSolidBlockWithAirAbove(mutable)) {
                return Optional.of(mutable.getY());
            }
            mutable.move(Direction.UP);
        }
        return Optional.empty();
    }

    private Optional<Integer> tryGettingYOfAirBlockWithSolidBelow() {
        BlockPos.MutableBlockPos mutable = blockPos.mutable();
        for (int i = 0; i < radius; i++) {
            if (isAirBlockWithSolidBelow(mutable)) {
                return Optional.of(mutable.getY() - 1);
            }
            mutable.move(Direction.DOWN);
        }
        return Optional.empty();
    }

    private boolean isSolidBlockWithAirAbove(BlockPos.MutableBlockPos mutable) {
        return isSolidBlock(mutable) && isAirBlock(mutable.move(Direction.UP));
    }

    private boolean isAirBlockWithSolidBelow(BlockPos.MutableBlockPos mutable) {
        return isAirBlock(mutable) && isSolidBlock(mutable.move(Direction.DOWN));
    }

    private boolean isSolidBlock(BlockPos position) {
        return !level.getBlockState(position).isAir();
    }

    private boolean isAirBlock(BlockPos position) {
        return level.getBlockState(position).isAir();
    }
}
