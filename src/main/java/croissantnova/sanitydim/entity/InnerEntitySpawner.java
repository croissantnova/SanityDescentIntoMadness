package croissantnova.sanitydim.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class InnerEntitySpawner
{
    private static final Random RAND = new Random();

    public static int spawnRad = 20;
    public static int detectionRad = 40;
    public static int spawnTimeout = 20 * 20;

    public static final float SPAWN_THRESHOLD = .75f;
    public static final Map<ServerPlayerEntity, Integer> PLAYER_TO_SPAWN_TIMEOUT = new HashMap<>();

    private static int getHeightForSpawning(World level, BlockPos blockPos, int radius)
    {
        BlockPos.Mutable mutable = blockPos.mutable();
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

    public static boolean trySpawnForPlayer(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator() || player.level == null)
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
        if (s.getSanity() < SPAWN_THRESHOLD || getInnerEntitiesInRadius(player.getLevel(), player.blockPosition(), detectionRad).size() >= 3)
            return false;

        RottingStalker entity = EntityRegistry.ROTTING_STALKER.get().create(player.level);
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
        int h = getHeightForSpawning(player.level, trialPos, spawnRad);

        if (h == 0)
            return false;

        trialPos = new BlockPos(trialPos.getX(), h, trialPos.getZ());
        entity.setPos(trialPos.getX() + .5f, trialPos.getY() + .5f, trialPos.getZ() + .5f);
        if (entity.checkSpawnObstruction(player.level) &&
                player.level.noCollision(entity) &&
                ((ServerWorld)player.level).tryAddFreshEntityWithPassengers(entity))
        {
            PLAYER_TO_SPAWN_TIMEOUT.put(player, spawnTimeout);
            return true;
        }

        return false;
    }

    public static List<InnerEntity> getInnerEntitiesInRadius(ServerWorld level, BlockPos blockPos, int radius)
    {
        return level.getEntitiesOfClass(InnerEntity.class, new AxisAlignedBB(blockPos).inflate(radius));
    }
}