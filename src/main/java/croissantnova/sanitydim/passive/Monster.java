package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import javax.annotation.Nonnull;
import java.util.List;

public class Monster implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        AxisAlignedBB playerSurroundings = new AxisAlignedBB(
                player.position().add(new Vector3d(-8.0f, -8.0f, -8.0f)),
                player.position().add(new Vector3d(8.0f, 8.0f, 8.0f)));

        float result = 0;
        float monster = ConfigProxy.getMonster(dim);
        if (monster != 0.0f)
        {
            List<MonsterEntity> monstersAround = player.level.getEntitiesOfClass(
                    MonsterEntity.class,
                    playerSurroundings);
            for (MonsterEntity m : monstersAround)
            {
                if (!player.canSee(m))
                    continue;

                result = monster;

                if (m.getTarget() != null && m.getTarget().is(player))
                {
                    result *= 2;
                    break;
                }
            }
        }

        return result;
    }
}