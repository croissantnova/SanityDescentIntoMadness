package croissantnova.sanitydim.sources.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MovingOnCarpetSanitySource implements IPassiveSanitySource
{
    private final Map<ServerPlayer, Vec3> previousPlayerPositions = new HashMap<>();

    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        if (playerIsStandingOnCarpet(player))
        {
            if (playerMoved(player))
            {
                updatePreviousPlayerPosition(player);
                return ConfigProxy.getStandingOnCarpetSanity(dim);
            }
        }

        return 0;
    }

    private boolean playerIsStandingOnCarpet(@Nonnull ServerPlayer player) {
        BlockState blockPlayerIsStandingOn = player.getFeetBlockState();
        BlockPos blockPos = player.blockPosition();
        ServerLevel level = player.serverLevel();

        if (blockPlayerIsStandingOn.is(BlockTags.WOOL_CARPETS)) {
            return true;
        }

        if (blockPlayerIsStandingOn.isAir()) {
            return isCarpet(level, blockPos.below()) || isCarpet(level, blockPos.below(2));
        }
        return false;
    }

    private boolean playerMoved(ServerPlayer player) {
        Vec3 currentPos = player.position();
        Vec3 previousPos = previousPlayerPositions.getOrDefault(player, Vec3.ZERO);
        return !currentPos.equals(previousPos);
    }

    private void updatePreviousPlayerPosition(ServerPlayer player) {
        previousPlayerPositions.put(player, player.position());
    }

    private boolean isCarpet(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(BlockTags.WOOL_CARPETS);
    }
}