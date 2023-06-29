package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Pet implements IPassiveSanitySource
{
    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim)
    {
        AABB playerSurroundings = new AABB(
                player.position().add(new Vec3(-8.0f, -8.0f, -8.0f)),
                player.position().add(new Vec3(8.0f, 8.0f, 8.0f)));

        float pet = ConfigProxy.getPet(dim);
        if (pet != 0.0f)
        {
            List<TamableAnimal> tamablesAround = new ArrayList<>();
            player.level().getEntities(
                    EntityTypeTest.forClass(TamableAnimal.class),
                    playerSurroundings,
                    ta -> ta.isOwnedBy(player) && player.hasLineOfSight(ta),
                    tamablesAround,
                    1);
            if (!tamablesAround.isEmpty())
                return pet;
        }

        return 0;
    }
}