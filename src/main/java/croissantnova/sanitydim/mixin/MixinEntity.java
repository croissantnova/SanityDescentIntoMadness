package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity
{
    @Shadow protected Vec3 stuckSpeedMultiplier;

    @Inject(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/phys/Vec3;multiply(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
                     shift = At.Shift.BY,
                     by = 2))
    private void move(MoverType pType, Vec3 pPos, CallbackInfo ci)
    {
        if ((Object)this instanceof ServerPlayer sp)
        {
            sp.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s instanceof IPersistentSanity ps)
                    ps.setStuckMotionMultiplier(this.stuckSpeedMultiplier);
            });
        }
    }
}