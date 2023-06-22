package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import javax.annotation.Nonnull;
import java.util.List;

public class Pet implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        AxisAlignedBB playerSurroundings = new AxisAlignedBB(
                player.position().add(new Vector3d(-8.0f, -8.0f, -8.0f)),
                player.position().add(new Vector3d(8.0f, 8.0f, 8.0f)));

        float pet = ConfigProxy.getPet(dim);
        if (pet != 0.0f)
        {
            List<TameableEntity> tamablesAround = player.level.getEntitiesOfClass(
                    TameableEntity.class,
                    playerSurroundings);
            for (TameableEntity ent : tamablesAround)
            {
                if (ent.isOwnedBy(player) && player.canSee(ent))
                    return pet;
            }
        }

        return 0;
    }
}