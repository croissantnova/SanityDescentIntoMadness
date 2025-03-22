package croissantnova.sanitydim.sources.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.custom.PassiveSanityStatusEffect;
import croissantnova.sanitydim.sources.SanityCalculatorBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StatusEffectSanitySource implements IPassiveSanitySource {

    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim) {
        return new SanityCalculator(player, cap, dim).calculate();
    }

    private static class SanityCalculator extends SanityCalculatorBase {
        private final List<PassiveSanityStatusEffect> passiveSanityStatusEffects;
        private final Map<MobEffect, PassiveSanityStatusEffect> previousConfigEffects = new HashMap<>();

        public SanityCalculator(ServerPlayer player, ISanity sanityCap, ResourceLocation dim) {
            super(player, sanityCap, dim);
            passiveSanityStatusEffects = config.passiveConfig.statusEffects.get(dim);
        }

        public float calculate() {
            return passiveSanityStatusEffects.stream()
                    .map(this::calculate)
                    .reduce(0f, Float::sum);
        }

        private float calculate(PassiveSanityStatusEffect configEffect) {
            return configEffect.getEffectInstance(player)
                    .map(mobEffectInstance -> calculate(configEffect, mobEffectInstance))
                    .orElse(0f);
        }

        private float calculate(PassiveSanityStatusEffect configEffect, MobEffectInstance mobEffectInstance) {
            MobEffect playerEffect = mobEffectInstance.getEffect();
            int playerAmplifier = mobEffectInstance.getAmplifier();
            // ensures that config effects like -0.25/s when having Blindness II does not apply for Blindness I
            if (playerAmplifier < configEffect.amplifier()) {
                return 0f;
            }

            PassiveSanityStatusEffect previousConfigEffect = previousConfigEffects.getOrDefault(playerEffect, PassiveSanityStatusEffect.DEFAULT);
            if (!configEffect.overrides(previousConfigEffect)) {
                return 0f;
            }

            float previousSanity = previousConfigEffect.sanity();

            previousConfigEffects.put(playerEffect, configEffect);
            return configEffect.sanity() - previousSanity;
        }
    }
}