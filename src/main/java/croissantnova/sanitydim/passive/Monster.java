package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class Monster implements IPassiveSanitySource
{
    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim)
    {
        AABB playerSurroundings = new AABB(
                player.position().add(new Vec3(-8.0f, -8.0f, -8.0f)),
                player.position().add(new Vec3(8.0f, 8.0f, 8.0f)));

        float result = 0;
        float monster = ConfigProxy.getMonster(dim);
        if (monster != 0.0f)
        {
            List<net.minecraft.world.entity.monster.Monster> monstersAround = player.level.getEntities(
                    EntityTypeTest.forClass(net.minecraft.world.entity.monster.Monster.class),
                    playerSurroundings,
                    player::hasLineOfSight);
            if (!monstersAround.isEmpty())
                result = monster;
            for (net.minecraft.world.entity.monster.Monster m : monstersAround)
            {
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