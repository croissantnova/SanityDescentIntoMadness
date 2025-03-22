package croissantnova.sanitydim.api;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the sanity state of a player or entity based on a sanity range.
 * <p>
 * Each state is defined within a specific range of sanity values:
 * - The lower bound is inclusive.
 * - The upper bound is exclusive.
 * <p>
 * Available states:<br>
 * 1. SANE: [0.0, 0.4)<br>
 * 2. PARANOID: [0.4, 0.7)<br>
 * 3. INSANE: [0.7, Float.MAX_VALUE)<br>
 * <p>
 * Methods in this class allow checking if a sanity value falls
 * within, below, or above a specific state. Additionally, you can determine
 * the state based on a sanity value.
 */
public enum SanityState {
    SANE(0f),
    PARANOID(0.4f),
    INSANE(0.7f);

    private final float lowerBound;

    /**
     * Constructs a SanityState with the specified lower bound.
     *
     * @param lowerBound The inclusive lower bound for this state.
     */
    SanityState(float lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Gets the inclusive lower bound of this state.
     *
     * @return The lower bound.
     */
    public float getLowerBound() {
        return lowerBound;
    }

    /**
     * Gets the exclusive upper bound of this state.
     *
     * @return The upper bound, or {@code Float.MAX_VALUE} if this is the last state.
     */
    public float getUpperBound() {
        return notLastOrdinal()
                ? getNextUpperBound()
                : Float.MAX_VALUE;
    }

    /**
     * Checks if this state is not the last state in the enumeration.
     *
     * @return {@code true} if this is not the last state, {@code false} otherwise.
     */
    private boolean notLastOrdinal() {
        return this.ordinal() < values().length - 1;
    }

    /**
     * Gets the lower bound of the next state in the enumeration.
     *
     * @return The lower bound of the next state.
     */
    private float getNextUpperBound() {
        return values()[this.ordinal() + 1].lowerBound;
    }

    /**
     * Checks if the given sanity value falls within this state.
     *
     * @param sanity The sanity value to check.
     * @return {@code true} if the value is within this state, {@code false} otherwise.
     */
    public boolean isAt(float sanity) {
        return sanity >= lowerBound && sanity < getUpperBound();
    }

    /**
     * Checks if the given sanity value is at or above this state's lower bound.
     *
     * @param sanity The sanity value to check.
     * @return {@code true} if the value is at or above this state, {@code false} otherwise.
     */
    public boolean isAtOrAbove(float sanity) {
        return sanity >= lowerBound;
    }

    /**
     * Checks if the given sanity value is at or below this state's upper bound.
     *
     * @param sanity The sanity value to check.
     * @return {@code true} if the value is at or below this state, {@code false} otherwise.
     */
    public boolean isAtOrBelow(float sanity) {
        return sanity < getUpperBound();
    }

    /**
     * Checks if the given sanity value is below this state's lower bound.
     *
     * @param sanity The sanity value to check.
     * @return {@code true} if the value is below this state, {@code false} otherwise.
     */
    public boolean isBelow(float sanity) {
        return sanity < lowerBound;
    }

    /**
     * Checks if the given sanity value is above this state's upper bound.
     *
     * @param sanity The sanity value to check.
     * @return {@code true} if the value is above this state, {@code false} otherwise.
     */
    public boolean isAbove(float sanity) {
        return sanity >= getUpperBound();
    }

    /**
     * Determines the SanityState corresponding to the given sanity value.
     *
     * @param sanity The sanity value to evaluate.
     * @return The corresponding SanityState.
     * @throws IllegalArgumentException If the sanity value is outside the defined ranges.
     */
    public static @NotNull SanityState fromSanityValue(float sanity) {
        for (SanityState state : values()) {
            if (state.isAt(sanity)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Sanity value is out of bounds: " + sanity);
    }
}

