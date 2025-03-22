package croissantnova.sanitydim.event;

import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.util.PlayerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;

import java.util.List;

public final class PlayerHurtAnimalEvent {

    private final ServerPlayer player;
    private final Animal animal;
    private final float damageDealt;
    private float initialSanityDrainAmount;
    private float sanityDrainAmount;

    public PlayerHurtAnimalEvent(ServerPlayer player, Animal animal, float damageDealt) {
        this.player = player;
        this.animal = animal;
        this.damageDealt = damageDealt;
        handle();
    }

    private void handle()
    {
        if (player == null || player.isCreative() || player.isSpectator() || damageDealt <= 0f) {
            return;
        }

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            getInitialSanityDrainAmount();
            applyBabyAnimalIsWatchingModifier();
            applyAnimalOnFireModifier();
            applyHumaneModifier();
            if (sanityDrainAmount == 0f) {
                return;
            }

            SanityProcessor.addSanity(s, sanityDrainAmount, player);
        });
    }

    private void getInitialSanityDrainAmount() {
        ResourceLocation dim = player.level().dimension().location();
        sanityDrainAmount = damageDealt * ConfigProxy.getAnimalHurtRatio(dim);
        initialSanityDrainAmount = sanityDrainAmount;
    }

    private void applyAnimalOnFireModifier() {
        if (animal.isOnFire()) {
            sanityDrainAmount *= 2f;
        }
    }

    private void applyBabyAnimalIsWatchingModifier() {
        AABB searchBox = player.getBoundingBox().inflate(16d);
        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, searchBox);

        for (LivingEntity entity : entities) {
            if (entity instanceof Animal e && e.isBaby()) {
                sanityDrainAmount *= 2f;
            }
        }
    }

    private void applyMostlyHumaneModifier() {
        // don't punish the player as harshly for trying to kill an adult animal quickly
        if (!animal.isBaby() && damageDealt >= animal.getMaxHealth() * 0.5f) {
            sanityDrainAmount *= 0.5f;
        }
    }

    private void applyHumaneModifier() {
        if (sanityDrainAmount != initialSanityDrainAmount) {
            return;
        }
        if (PlayerHelper.isHoldingSword(player) && damageDealt >= animal.getMaxHealth()) {
            sanityDrainAmount = 0f;
        }
    }
}
