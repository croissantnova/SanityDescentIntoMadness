package croissantnova.sanitydim.util;

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
}