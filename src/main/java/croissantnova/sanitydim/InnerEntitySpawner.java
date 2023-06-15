package croissantnova.sanitydim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class InnerEntitySpawner
{
    private static final RandomSource random = RandomSource.create();
    public static final Map<ServerPlayer, Integer> playerToSpawnTimeout = new HashMap<ServerPlayer, Integer>();
    public static int spawnRad = 20;
    public static int detectionRad = 40;
    public static int spawnTimeout = 20 * 20;

    private static int getHeightForSpawning(Level level, BlockPos blockPos, int radius)
    {
        BlockPos.MutableBlockPos mutable = blockPos.mutable();
        for (int i = 0; i < radius; i++)
        {
            if (!level.getBlockState(mutable).isAir() && level.getBlockState(mutable.move(Direction.UP)).isAir())
            {
                return mutable.getY();
            }
        }
        for (int i = 0; i < radius; i++)
        {
            if (level.getBlockState(mutable).isAir() && !level.getBlockState(mutable.move(Direction.DOWN)).isAir())
            {
                return mutable.getY() - 1;
            }
        }
        return 0;
    }

    public static boolean trySpawnForPlayer(ServerPlayer player, float sanityThreshold)
    {
        if (player == null || player.isCreative() || player.isSpectator() || player.level == null)
            return false;

        playerToSpawnTimeout.putIfAbsent(player, 0);
        int t = playerToSpawnTimeout.get(player);
        if (t > 0)
        {
            playerToSpawnTimeout.put(player, t - 1);
            return false;
        }

        ISanity s = player.getCapability(SanityProvider.CAP).orElse(null);
        if (s == null)
            return false;
        if (s.getSanity() < sanityThreshold || getInnerEntitiesInRadius(player.level, player.blockPosition(), detectionRad).size() >= 3)
            return false;

        RottingStalker entity = EntityRegistry.ROTTING_STALKER.get().create(player.level);
//        CreepingNightmare entity = EntityRegistry.CREEPING_NIGHTMARE.get().create(player.level);
        BlockPos trialPos = BlockPos.randomBetweenClosed(random, 1,
                player.blockPosition().getX() - spawnRad,
                player.blockPosition().getY(),
                player.blockPosition().getZ() - spawnRad,
                player.blockPosition().getX() + spawnRad,
                player.blockPosition().getY(),
                player.blockPosition().getZ() + spawnRad).iterator().next();
        int h = getHeightForSpawning(player.level, trialPos, spawnRad);

        if (h == 0)
            return false;

        trialPos = new BlockPos(trialPos.getX(), h, trialPos.getZ());
        entity.setPos(new Vec3(trialPos.getX() + .5f, trialPos.getY() + .5f, trialPos.getZ() + .5f));
        if (entity.checkSpawnObstruction(player.level) &&
                player.level.noCollision(entity) &&
                ((ServerLevel)player.level).tryAddFreshEntityWithPassengers(entity))
        {
            playerToSpawnTimeout.put(player, spawnTimeout);
            return true;
        }

        return false;
    }

    public static List<InnerEntity> getInnerEntitiesInRadius(Level level, BlockPos blockPos, int radius)
    {
        return level.getEntitiesOfClass(InnerEntity.class, new AABB(blockPos).inflate(radius));
    }
}