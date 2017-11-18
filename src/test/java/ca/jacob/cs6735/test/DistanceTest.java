package ca.jacob.cs6735.test;

import ca.jacob.jml.distance.DistanceFunction;
import ca.jacob.jml.distance.Euclidean;
import ca.jacob.jml.distance.Hamming;
import ca.jacob.jml.util.Vector;
import org.junit.Test;

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

public class DistanceTest {
    private final double DELTA = 1e-5;

    @Test
    public void testEuclidean() {
        DistanceFunction df = new Euclidean();
        Vector one = new Vector(new int[]{1, 0, 1, 2});
        Vector two = new Vector(new int[]{1, 1, 0, 0});
        assertEquals(sqrt(6), df.distance(one, two), DELTA);
    }

    @Test
    public void testHamming() {
        DistanceFunction df = new Hamming();
        Vector one = new Vector(new int[]{1, 0, 1, 2});
        Vector two = new Vector(new int[]{1, 1, 0, 0});
        assertEquals(3, df.distance(one, two), DELTA);
    }
}
