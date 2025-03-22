package croissantnova.sanitydim.config.custom;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record PassiveSanityStatusEffect(ResourceLocation id, float sanity, int amplifier) {

    public static final PassiveSanityStatusEffect DEFAULT = new PassiveSanityStatusEffect(
            new ResourceLocation("minecraft:empty"),
            0f,
            -1
    );

    private static final Set<ResourceLocation> UNREGISTERED_EFFECTS = new HashSet<>();

    public Optional<MobEffect> getMobEffect() {
        Optional<MobEffect> mobEffect = Optional.ofNullable(ForgeRegistries.MOB_EFFECTS.getValue(id));
        if (mobEffect.isEmpty()) {
            handleUnregisteredEffect();
        }
        return mobEffect;
    }

    public boolean overrides(@NotNull PassiveSanityStatusEffect other) {
        return other.amplifier < this.amplifier;
    }

    public Optional<MobEffectInstance> getEffectInstance(LivingEntity entity) {
        return getMobEffect().map(entity::getEffect);
    }

    public void handleUnregisteredEffect() {
        if (!UNREGISTERED_EFFECTS.contains(id)) {
            UNREGISTERED_EFFECTS.add(id);
            SanityMod.LOGGER.error("Effect with ID {} is not registered, check your passive sanity config!", id);
        }
    }
}
