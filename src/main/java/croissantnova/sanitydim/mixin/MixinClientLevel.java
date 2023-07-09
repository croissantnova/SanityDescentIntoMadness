package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel
{
    @Shadow public abstract void playLocalSound(
            double pX,
            double pY,
            double pZ,
            SoundEvent pSound,
            SoundSource pCategory,
            float pVolume,
            float pPitch,
            boolean pDistanceDelay);

    @Inject(
            method = "playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"),
            cancellable = true)
    private void playSeededSound(
            @Nullable Player pPlayer,
            double pX,
            double pY,
            double pZ,
            SoundEvent pSound,
            SoundSource pCategory,
            float pVolume,
            float pPitch,
            CallbackInfo ci)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s.getSanity() >= Blackout.THRESHOLD)
            {
                if (pSound.equals(SoundEvents.CHICKEN_AMBIENT) ||
                        pSound.equals(SoundEvents.COW_AMBIENT) ||
                        pSound.equals(SoundEvents.PIG_AMBIENT) ||
                        pSound.equals(SoundEvents.SHEEP_AMBIENT)) {}
                else if (pSound.equals(SoundEvents.CHICKEN_HURT) || pSound.equals(SoundEvents.CHICKEN_DEATH) ||
                         pSound.equals(SoundEvents.COW_HURT) || pSound.equals(SoundEvents.COW_DEATH) ||
                         pSound.equals(SoundEvents.PIG_HURT) || pSound.equals(SoundEvents.PIG_DEATH) ||
                         pSound.equals(SoundEvents.SHEEP_HURT) || pSound.equals(SoundEvents.SHEEP_DEATH))
                {
                    this.playLocalSound(pX, pY, pZ, SoundRegistry.INNER_ENTITY_HURT.get(), pCategory, 1.0f, pPitch, false);
                }
                else return;

                ci.cancel();
            }
        });
    }
}