package ca.jacob.cs6735;

import ca.jacob.jml.DataSet;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.jml.DataSet.DISCRETE;
import static ca.jacob.jml.Util.generateIndices;
import static ca.jacob.jml.Util.replaceWithMostCommonFromClass;
import static ca.jacob.jml.Util.toIntegers;
import static ca.jacob.jml.Util.calculateOccurrences;
import static ca.jacob.jml.math.Util.ln;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(UtilTest.class);
    private static final double DELTA = 1e-5;

    @Test
    public void testIndices() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndices(v, v.length()-1);
        assertEquals(v.length()-1, indices.length());
        LOG.info("indices: {}", indices);
    }

    @Test
    public void testIndicesDistribution() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndices(v, 1000);
        Map<Integer, Integer> occurrences = calculateOccurrences(indices);
        LOG.info("occurrences: {}", occurrences);
    }

    @Test
    public void testIndicesDistributionFromTo() {
        Vector indices = generateIndices(0, 10, 1000);
        Map<Integer, Integer> occurrences = calculateOccurrences(indices);
        LOG.info("occurrences: {}", occurrences);
    }

    @Test
    public void testLn() {
        assertEquals((double) 0, ln(1), DELTA);
        assertEquals((double)1.609437912, ln(5), DELTA);
    }

    @Test
    public void testEntropy() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        Vector attributeTypes = new Vector(new int[]{DISCRETE, DISCRETE, DISCRETE});
        DataSet dataSet = new DataSet(data, attributeTypes);
        Assert.assertEquals(1, dataSet.entropy(), DELTA);
    }

    @Test
    public void testToIntegers() {
        String[][] strings = new String[][]{{"o", "t"},{"t","t"}};
        toIntegers(strings);
        assertArrayEquals(new String[][]{{"0", "0"},{"1", "0"}}, strings);
    }

    @Test
    public void testReplaceWithMostCommonFromClass() {
        String[][] data = new String[][]{
                {"0", "0"},
                {"?", "0"},
                {"1", "0"},
                {"0", "0"},
                {"3", "0"},
                {"3", "0"},
                {"3", "0"},
                {"1", "1"},
                {"1", "1"}
        };

        replaceWithMostCommonFromClass("?", data, 1);

        String[][] expected = new String[][]{
                {"0", "0"},
                {"3", "0"},
                {"1", "0"},
                {"0", "0"},
                {"3", "0"},
                {"3", "0"},
                {"3", "0"},
                {"1", "1"},
                {"1", "1"}
        };

        assertArrayEquals(data, expected);
    }
}
