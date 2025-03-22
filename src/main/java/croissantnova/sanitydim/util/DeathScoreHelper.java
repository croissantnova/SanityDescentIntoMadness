package croissantnova.sanitydim.util;

import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.capability.IPersistentSanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.config.registry.ConfigRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class DeathScoreHelper {

    private final IPersistentSanity ps;
    private final int scorePerDeath;
    private final float sanityPerDeath;

    public DeathScoreHelper(@NotNull Player player) {
        ps = (IPersistentSanity) player.getCapability(SanityProvider.CAP)
                .orElseThrow(() -> new IllegalStateException("ISanity capability is missing"));

        ResourceLocation dim = player.level().dimension().location();
        ConfigRegistry config = ConfigManager.getConfigValues();
        scorePerDeath = config.active_died_scorePerDeath.get(dim);
        sanityPerDeath = config.active_sanityPerDeath.get(dim).floatValue();
    }

    public DeathScoreHelper(IPersistentSanity ps, ResourceLocation dim) {
        this.ps = ps;

        ConfigRegistry config = ConfigManager.getConfigValues();
        scorePerDeath = config.active_died_scorePerDeath.get(dim);
        sanityPerDeath = config.active_sanityPerDeath.get(dim).floatValue();
    }

    public boolean isDeathStackingEnabled() {
        return scorePerDeath > 0;
    }

    public boolean isDeathSanityEnabled() {
        return sanityPerDeath != 0;
    }

    public float getScorePerDeath() throws DeathStackingDisabledException {
        if (!isDeathStackingEnabled()) {
            throw new DeathStackingDisabledException();
        }
        return scorePerDeath;
    }

    public float getSanityPerDeath() throws DeathSanityDisabledException {
        if (!isDeathSanityEnabled()) {
            throw new DeathSanityDisabledException();
        }
        return sanityPerDeath;
    }

    public float getDeathScore() {
        return ps.getDeathScore();
    }

    public void setDeathScore(float score) {
        ps.setDeathScore(score);
    }

    // Helper Methods

    public boolean isDamned() {
        return isDeathSanityEnabled() && isDeathStackingEnabled() && getNumberOfDeaths() >= getMaxDeaths();
    }

    public void incrementDeathScore() throws DeathSanityDisabledException, DeathStackingDisabledException {
        setDeathScore(Math.min(getDeathScore() + getScorePerDeath(), getMaxDeathScore()));
    }

    /**
     * Decrements the death score by a fixed amount and adjusts it to ensure it does not exceed the maximum death score.
     * If the resulting death score becomes divisible by the death count value, a specific condition is satisfied.
     *
     * @return true if the death score is divisible by the death count after decrementing;
     *         false if the death score is zero or the divisibility condition is not met.
     * @throws DeathSanityDisabledException if death sanity is not enabled.
     * @throws DeathStackingDisabledException if death stacking is not enabled.
     */
    public boolean decrementDeathScore() throws DeathSanityDisabledException, DeathStackingDisabledException {
        if (getDeathScore() > 0) {
            setDeathScore(Math.min(getDeathScore() - 1f, getMaxDeathScore()));
            return isScoreDivisibleByDeathCount();
        }
        return false;
    }

    public boolean decrementFullDeath() throws DeathSanityDisabledException, DeathStackingDisabledException {
        if (getDeathScore() > 0) {
            setDeathScore(Math.min(getDeathScore() - getScorePerDeath(), getMaxDeathScore()));
            return true;
        }
        return false;
    }

    public MutableComponent getDecrementText() {
        return getDeathScore() <= 0
                ? Component.literal("Your soul feels free...")
                : Component.literal("Your soul feels heavier...");
    }

    private boolean isScoreDivisibleByDeathCount() throws DeathStackingDisabledException {
        return getDeathScore() % getScorePerDeath() == 0;
    }

    public float getMaxDeathScore() throws DeathSanityDisabledException, DeathStackingDisabledException {
        return (PlayerSanityAPI.MAX_SANITY_INTERNAL / getSanityPerDeath()) * getScorePerDeath();
    }

    public int getNumberOfDeaths() throws DeathStackingDisabledException {
        return (int) Math.ceil(getDeathScore() / getScorePerDeath());
    }

    public int getMaxDeaths() throws DeathStackingDisabledException, DeathSanityDisabledException {
        return (int) Math.ceil(getMaxDeathScore() / getScorePerDeath());
    }

    public float calculateTotalSanityLossFromDeaths() throws DeathSanityDisabledException, DeathStackingDisabledException {
        return getNumberOfDeaths() * getSanityPerDeath();
    }
}