package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.SanityProcessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EggEntity.class)
public abstract class MixinEggEntity extends ProjectileItemEntity
{
    private MixinEggEntity(EntityType<? extends EggEntity> pEntityType, World pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Inject(method = "onHit(Lnet/minecraft/util/math/RayTraceResult;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void onHit(RayTraceResult hitResult, CallbackInfo ci)
    {
        if (getOwner() != null && getOwner() instanceof ServerPlayerEntity)
            SanityProcessor.handlePlayerSpawnedChicken((ServerPlayerEntity)getOwner());
    }
}