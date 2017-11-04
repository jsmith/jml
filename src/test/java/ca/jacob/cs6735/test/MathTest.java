package ca.jacob.cs6735.test;

import ca.jacob.cs6735.util.Vector;
import org.junit.Test;

import static ca.jacob.cs6735.util.Math.mean;
import static ca.jacob.cs6735.util.Math.stdev;
import static org.junit.Assert.assertEquals;

public class MathTest {
    private static final double DELTA = 1e-5;

    @Test
    public void testStdDevAndMean() {
        Vector v = new Vector(new int[]{1, 2, 3, 4, 5});
        double stdev = stdev(v);
        double mean = mean(v);
        assertEquals(1.58113883008, stdev, DELTA);
        assertEquals(3., mean, DELTA);
    }
}
