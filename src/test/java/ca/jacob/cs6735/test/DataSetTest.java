package ca.jacob.cs6735.test;

import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Tuple;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static org.junit.Assert.assertEquals;

public class DataSetTest {
    private static final double DELTA = 1e-5;

    private DataSet d;

    @Before
    public void init() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        d = new DataSet(data, DISCRETE);
    }

    @Test
    public void testSplitByDiscrete() {
        Map<Integer, DataSet> subsets = d.splitByDiscreteAttribute(1);
        assertEquals(2, subsets.size());
    }

    @Test
    public void testSplitAt() {
        List<DataSet> subsets = d.splitAt(1, 0.5);
        assertEquals(2, subsets.size());
    }

    @Test
    public void testSplitByContinuous() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}, {0, -1, 0, 0}});
        DataSet d = new DataSet(data, CONTINUOUS);
        Tuple<Double, List<DataSet>> subsets = d.splitByContinuousAttribute(1);
        assertEquals(2, subsets.last().size());
        assertEquals(0.5, subsets.first(), DELTA);
    }
}
