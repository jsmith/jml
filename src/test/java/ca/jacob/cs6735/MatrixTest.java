package ca.jacob.cs6735;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static ca.jacob.cs6735.util.Math.calculateOccurrences;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MatrixTest {
    private ca.jacob.cs6735.util.Matrix matrix;

    @Before
    public void setup() {
        matrix = new ca.jacob.cs6735.util.Matrix(new Integer[][]{{1, 2, 3}, {1, 2, 3}});
    }

    @Test
    public void testRow() {
        assertArrayEquals(new Double[]{1., 2., 3.}, matrix.row(0));
    }

    @Test
    public void testCol() {
        assertArrayEquals(new Double[]{2., 2.}, matrix.col(1));
    }

    @Test
    public void testCalculateOccurrences() {
        Double[] vec = new Double[]{1., 2., 3., 1., 1.};
        Map<Double, Integer> expected = new HashMap<Double, Integer>();
        expected.put(1., 3);
        expected.put(2., 1);
        expected.put(3., 1);
        assertEquals(expected, calculateOccurrences(vec));
    }
}
