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
}