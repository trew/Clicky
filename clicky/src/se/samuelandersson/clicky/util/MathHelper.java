package se.samuelandersson.clicky.util;

public class MathHelper
{
    /**
     * Restricts a value to be within a specified range.
     * 
     * @param value
     *            The value to clamp.
     * @param min
     *            The minimum value. If value is less than min, min will be
     *            returned.
     * @param max
     *            The maximum value. If value is greater than max, max will be
     *            returned.
     * @return The clamped value.
     */
    static public float clamp(float value, float min, float max)
    {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Restricts a value to be within a specified range.
     * 
     * @param value
     *            The value to clamp.
     * @param min
     *            The minimum value. If value is less than min, min will be
     *            returned.
     * @param max
     *            The maximum value. If value is greater than max, max will be
     *            returned.
     * @return The clamped value.
     */
    static public double clamp(double value, double min, double max)
    {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Restricts a value to be within a specified range.
     * 
     * @param value
     *            The value to clamp.
     * @param min
     *            The minimum value. If value is less than min, min will be
     *            returned.
     * @param max
     *            The maximum value. If value is greater than max, max will be
     *            returned.
     * @return The clamped value.
     */
    static public int clamp(int value, int min, int max)
    {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Convert a value from a domain (inMin to inMax) into another domain
     * (outMin to outMax)
     * 
     * <p>
     * Example:
     * <ul>
     * <li>convert (5, 0, 10, 0, 100) == 50</li>
     * <li>convert (15, 10, 20, 0, 1) == 0.5f</li>
     * </ul>
     * </p>
     */
    static public float convert(float value, float inMin, float inMax,
            float outMin, float outMax)
    {
        float base = (value - inMin) / (inMax - inMin);
        float finale = (outMax - outMin) * base + outMin;
        // clamp it in case value is not within inMin to inMax
        return MathHelper.clamp(finale, outMin, outMax);
    }

    /**
     * Same as {@link Math#hypot(double, double)}, but with float
     */
    public static float hypot(float x, float y)
    {
        return (float) (Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0)));
    }
}
