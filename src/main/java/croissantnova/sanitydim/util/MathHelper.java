package croissantnova.sanitydim.util;

import net.minecraft.world.phys.Vec3;

public class MathHelper
{
    public static float clamp(float value, float min, float max)
    {
        return value < min ? min : (Math.min(value, max));
    }
    public static double clamp(double value, double min, double max)
    {
        return value < min ? min : (Math.min(value, max));
    }

    public static float clampNorm(float value)
    {
        return clamp(value, 0.0f, 1.0f);
    }

    public static double clampNorm(double value)
    {
        return clamp(value, 0.0d, 1.0d);
    }

    public static Vec3 toRadians(Vec3 angle)
    {
        return new Vec3(Math.toRadians(angle.x), Math.toRadians(angle.y), Math.toRadians(angle.z));
    }

    /**
     * Checks if a given value is within the range [min, max).
     *
     * @param value the value to check
     * @param min the inclusive lower bound of the range
     * @param max the exclusive upper bound of the range
     * @return true if the value is within the range, inclusive of the lower bound and exclusive of the upper bound; false otherwise
     */
    public static boolean isWithin(float value, float min, float max) {
        return value >= min && value < max;
    }
}