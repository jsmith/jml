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
    public void testDrop() {
        v.remove(3);
        v.remove(0);
        assertArrayEquals(new Double[]{2., 3., 4.}, v.toArray());
    }

    @Test
    public void testValueOfMaxOccurrance() {
        assertEquals(4., v.valueOfMaxOccurrance());
    }
}
