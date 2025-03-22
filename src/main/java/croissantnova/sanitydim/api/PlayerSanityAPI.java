package croissantnova.sanitydim.api;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.util.PlayerHelper;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public final class PlayerSanityAPI {

    private PlayerSanityAPI() {}

    public static final int MIN_SANITY = -100;
    public static final int MAX_SANITY = 100;
    public static final int MIN_SANITY_INTERNAL = 0;
    public static final int MAX_SANITY_INTERNAL = 1;

    // Sanity Arithmetic

    public static void setSanity(@NotNull Player player, float sanity) {
        player.getCapability(SanityProvider.CAP).ifPresent(cap -> {
            cap.setSanity(sanity);
        });
    }

    public static float getSanity(@NotNull Player player) {
        AtomicReference<Float> sanity = new AtomicReference<>(0f);

        player.getCapability(SanityProvider.CAP).ifPresent(cap -> {
            sanity.set(cap.getSanity());
        });

        return sanity.get();
    }

    public static void addSanity(@NotNull Player player, float sanity) {
        setSanity(player, getSanity(player) + sanity);
    }

    // Nightmare Entity

    public static boolean bypassesNightmareInvisibilityToSane(@NotNull Player player) {
        return ConfigProxy.canSaneSeeNightmares(player.level().dimension().location());
    }

    public static boolean canSeeNightmares(@NotNull Player player) {
        return PlayerHelper.isImmortal(player)
                || bypassesNightmareInvisibilityToSane(player)
                || isAtStateOrAbove(player, SanityState.PARANOID);
    }

    public static boolean canBeTargetedByNightmares(@NotNull Player player) {
        return PlayerHelper.isMortal(player)
                && isAtStateOrAbove(player, SanityState.INSANE);
    }

    // Sanity State

    public static boolean isAtState(@NotNull Player player, @NotNull SanityState state) {
        float sanity = getSanity(player);
        return state.isAt(sanity);
    }

    public static boolean isAtStateOrAbove(@NotNull Player player, @NotNull SanityState state) {
        float sanity = getSanity(player);
        return state.isAtOrAbove(sanity);
    }

    public static boolean isAtStateOrBelow(@NotNull Player player, @NotNull SanityState state) {
        float sanity = getSanity(player);
        return state.isAtOrBelow(sanity);
    }

    public static boolean isBelowState(@NotNull Player player, @NotNull SanityState state) {
        float sanity = getSanity(player);
        return state.isBelow(sanity);
    }

    public static boolean isAboveState(@NotNull Player player, @NotNull SanityState state) {
        float sanity = getSanity(player);
        return state.isAbove(sanity);
    }

    // Other

    public static int getMaxSleepCounter(@NotNull Player player) {
        if (isAtStateOrAbove(player, SanityState.INSANE)) {
            return 600;
        }
        return 100;
    }
}
