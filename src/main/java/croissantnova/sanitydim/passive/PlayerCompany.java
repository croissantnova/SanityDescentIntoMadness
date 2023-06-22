package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerCompany implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        // FIXME: untested

        AtomicReference<Float> result = new AtomicReference<>(0.0f);
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        Vector3d offset = new Vector3d(8.0d, 8.0d, 8.0d);
        float sane = ConfigProxy.getSanePlayerCompany(dim);
        float insane = ConfigProxy.getInsanePlayerCompany(dim);

        for (PlayerEntity p : player.level.getNearbyPlayers(new EntityPredicate(), player, new AxisAlignedBB(
                player.position().subtract(offset),
                player.position().add(offset)
        )))
        {
            p.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s.getSanity() >= .5f)
                {
                    result.set(insane);
                    flag.set(true);
                }
                else
                    result.set(sane);
            });

            if (flag.get())
                break;
        }

        return result.get();
    }
}