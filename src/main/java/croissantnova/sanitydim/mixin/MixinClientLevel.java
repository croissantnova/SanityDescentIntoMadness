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
    @Shadow protected abstract void playSound(
            double pX,
            double pY,
            double pZ,
            SoundEvent pSoundEvent,
            SoundSource pSource,
            float pVolume,
            float pPitch,
            boolean pDistanceDelay,
            long pSeed);

    @Inject(
            method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFJ)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/ClientLevel;playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V"),
            cancellable = true)
    private void playSeededSound(
            @Nullable Player pPlayer,
            double pX,
            double pY,
            double pZ,
            SoundEvent pSoundEvent,
            SoundSource pSource,
            float pVolume,
            float pPitch,
            long pSeed,
            CallbackInfo ci)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s.getSanity() >= Blackout.THRESHOLD)
            {
                if (pSoundEvent.equals(SoundEvents.CHICKEN_AMBIENT) ||
                        pSoundEvent.equals(SoundEvents.COW_AMBIENT) ||
                        pSoundEvent.equals(SoundEvents.PIG_AMBIENT) ||
                        pSoundEvent.equals(SoundEvents.SHEEP_AMBIENT)) {}
                else if (pSoundEvent.equals(SoundEvents.CHICKEN_HURT) || pSoundEvent.equals(SoundEvents.CHICKEN_DEATH) ||
                         pSoundEvent.equals(SoundEvents.COW_HURT) || pSoundEvent.equals(SoundEvents.COW_DEATH) ||
                         pSoundEvent.equals(SoundEvents.PIG_HURT) || pSoundEvent.equals(SoundEvents.PIG_DEATH) ||
                         pSoundEvent.equals(SoundEvents.SHEEP_HURT) || pSoundEvent.equals(SoundEvents.SHEEP_DEATH))
                {
                    this.playSound(pX, pY, pZ, SoundRegistry.INNER_ENTITY_HURT.get(), pSource, 1.0f, pPitch, false, pSeed);
                }
                else return;

                ci.cancel();
            }
        });
    }
}