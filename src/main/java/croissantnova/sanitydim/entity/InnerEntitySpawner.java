package croissantnova.sanitydim.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class InnerEntitySpawner
{
    private static final RandomSource RAND = RandomSource.create();

    public static int spawnRad = 20;
    public static int detectionRad = 40;
    public static int spawnTimeout = 20 * 20;

    public static final float SPAWN_THRESHOLD = .75f;
    public static final Map<ServerPlayer, Integer> PLAYER_TO_SPAWN_TIMEOUT = new HashMap<ServerPlayer, Integer>();

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

    public static boolean trySpawnForPlayer(ServerPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return false;

        PLAYER_TO_SPAWN_TIMEOUT.putIfAbsent(player, 0);
        int t = PLAYER_TO_SPAWN_TIMEOUT.get(player);
        if (t > 0)
        {
            PLAYER_TO_SPAWN_TIMEOUT.put(player, t - 1);
            return false;
        }

        ISanity s = player.getCapability(SanityProvider.CAP).orElse(null);
        if (s == null)
            return false;
        if (s.getSanity() < SPAWN_THRESHOLD || getInnerEntitiesInRadius(player.level(), player.blockPosition(), detectionRad).size() >= 3)
            return false;

        RottingStalker entity = EntityRegistry.ROTTING_STALKER.get().create(player.level());
        if (entity == null)
            return false;

//        CreepingNightmare entity = EntityRegistry.CREEPING_NIGHTMARE.get().create(player.level);
        BlockPos trialPos = BlockPos.randomBetweenClosed(RAND, 1,
                player.blockPosition().getX() - spawnRad,
                player.blockPosition().getY(),
                player.blockPosition().getZ() - spawnRad,
                player.blockPosition().getX() + spawnRad,
                player.blockPosition().getY(),
                player.blockPosition().getZ() + spawnRad).iterator().next();
        int h = getHeightForSpawning(player.level(), trialPos, spawnRad);

        if (h == 0)
            return false;

        trialPos = new BlockPos(trialPos.getX(), h, trialPos.getZ());
        entity.setPos(new Vec3(trialPos.getX() + .5f, trialPos.getY() + .5f, trialPos.getZ() + .5f));
        if (entity.checkSpawnObstruction(player.level()) &&
                player.level().noCollision(entity) &&
                ((ServerLevel)player.level()).tryAddFreshEntityWithPassengers(entity))
        {
            PLAYER_TO_SPAWN_TIMEOUT.put(player, spawnTimeout);
            return true;
        }

        return false;
    }

    public static List<InnerEntity> getInnerEntitiesInRadius(Level level, BlockPos blockPos, int radius)
    {
        return level.getEntitiesOfClass(InnerEntity.class, new AABB(blockPos).inflate(radius));
    }
}