package se.samuelandersson.clicky.util;

public class Tester
{

    public static void main(String[] args)
    {
        testConvert();
    }

    public static void testConvert()
    {
        // a = convert (5, 0, 10, 0, 100) == 50
        // b = convert (5, 0, 10, 0, 1) == 0.5f
        // c = convert (5, 5, 10, 0, 100) == 0
        // d = convert (15, 10, 20, 0, 1) == 0.5f
        // e = convert (15, 10, 20, 20, 40) == 30f
        float a = MathHelper.convert(5, 0, 10, 0, 100);
        float b = MathHelper.convert(5, 0, 10, 0, 1);
        float c = MathHelper.convert(5, 5, 10, 0, 100);
        float d = MathHelper.convert(15, 10, 20, 0, 1);
        float e = MathHelper.convert(15, 10, 20, 20, 40);
        print(a, b, c, d, e);
    }

    public static void print(Object... objects)
    {
        for (Object o : objects) {
            System.out.println(o);
        }
    }
}
