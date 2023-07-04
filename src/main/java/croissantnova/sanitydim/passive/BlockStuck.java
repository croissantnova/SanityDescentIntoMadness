package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class BlockStuck implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        if (cap instanceof IPersistentSanity ps && ps.getStuckMotionMultiplier() != Vec3.ZERO)
        {
            ps.setStuckMotionMultiplier(Vec3.ZERO);
            return ConfigProxy.getBlockStuck(dim);
        }

        return 0;
    }
}
