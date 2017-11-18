package ca.jacob.cs6735.test;

import ca.jacob.jml.util.Matrix;
import org.junit.Test;

import static ca.jacob.cs6735.DataUtil.toIntegers;
import static org.junit.Assert.assertArrayEquals;

public class DataUtilTest {
    @Test
    public void testToIntegers() {
        String[][] strings = new String[][]{{"o", "t"},{"t","t"}};
        toIntegers(strings);
        assertArrayEquals(new String[][]{{"0", "0"},{"1", "0"}}, strings);
    }
}
