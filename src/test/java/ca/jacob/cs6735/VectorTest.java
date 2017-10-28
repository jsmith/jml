package ca.jacob.cs6735;

import ca.jacob.cs6735.utils.Vector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class VectorTest {
    private Vector v;

    @Before
    public void init() {
        v = new Vector(new Integer[]{1, 2, 3, 4, 4});
    }

    @Test
    public void testDrop() {
        v.drop(3);
        v.drop(0);
        assertEquals(new Integer[]{2, 3, 4}, v.toArray());
    }

    @Test
    public void testValueOfMaxOccurrance() {
        assertEquals((Integer) 4, v.valueOfMaxOccurrance());
    }
}
