package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
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
            method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/ClientLevel;playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V"),
            cancellable = true)
    private void playSeededSound(
            @Nullable Player pPlayer,
            double pX,
            double pY,
            double pZ,
            Holder<SoundEvent> pSound,
            SoundSource pSource,
            float pVolume,
            float pPitch,
            long pSeed,
            CallbackInfo ci)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        SoundEvent soundEvent = pSound.value();

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s.getSanity() >= Blackout.THRESHOLD)
            {
                if (soundEvent.equals(SoundEvents.CHICKEN_AMBIENT) ||
                    soundEvent.equals(SoundEvents.COW_AMBIENT) ||
                    soundEvent.equals(SoundEvents.PIG_AMBIENT) ||
                    soundEvent.equals(SoundEvents.SHEEP_AMBIENT)) {}
                else if (soundEvent.equals(SoundEvents.CHICKEN_HURT) || soundEvent.equals(SoundEvents.CHICKEN_DEATH) ||
                         soundEvent.equals(SoundEvents.COW_HURT) || soundEvent.equals(SoundEvents.COW_DEATH) ||
                         soundEvent.equals(SoundEvents.PIG_HURT) || soundEvent.equals(SoundEvents.PIG_DEATH) ||
                         soundEvent.equals(SoundEvents.SHEEP_HURT) || soundEvent.equals(SoundEvents.SHEEP_DEATH))
                {
                    this.playSound(pX, pY, pZ, SoundRegistry.INNER_ENTITY_HURT.get(), pSource, 1.0f, pPitch, false, pSeed);
                }
                else return;

                ci.cancel();
            }
        });
    }
}