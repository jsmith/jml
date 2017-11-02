package ca.jacob.cs6735;

import ca.jacob.cs6735.util.Vector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class VectorTest {
    private static final Double DELTA = 1e-10;
    private Vector v;

    @Before
    public void init() {
        v = new Vector(new Integer[]{1, 2, 3, 4, 4});
    }

    @Test
    public void testInit() {
        Vector v1 = new Vector(new Double[]{1., 2., 3., 4., 4.});
        assertEquals(v, v1);

        Vector v2 = new Vector(new String[]{"1", "2", "3", "4", "4"});
        assertEquals(v, v2);
    }

    @Test
    public void testAdd() {
        Vector v1 = new Vector(new Integer[]{1, 2, 3, 4});
        v1.add(4);
        assertEquals(v, v1);
    }

    @Test
    public void testDropAndToArray() {
        v.remove(3);
        v.remove(0);
        assertArrayEquals(new Double[]{2., 3., 4.}, v.toArray());
    }

    @Test
    public void testToIntegerArray() {
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 4}, v.toIntegerArray());
    }

    @Test
    public void testAt() {
        assertEquals(3., v.at(2));
    }

    @Test
    public void testValueOfMaxOccurrance() {
        assertEquals(4., v.valueOfMaxOccurrance());
    }

    @Test
    public void testFill() {
        Vector v1 = new Vector(new Integer[]{1, 1, 1, 1, 1});
        v.fill(1.);
        assertEquals(v1, v);
    }

    @Test
    public void testDot() {
        Double d = 46.;
        assertEquals(d, v.dot(v));
    }
}
