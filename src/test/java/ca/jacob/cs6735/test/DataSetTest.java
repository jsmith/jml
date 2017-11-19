package ca.jacob.cs6735.test;

import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Tuple;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataSetTest {
    private static final double DELTA = 1e-5;

    @Test
    public void testSplitByDiscrete() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        DataSet d = new DataSet(data, DISCRETE);
        Map<Integer, DataSet> subsets = d.splitByDiscreteAttribute(1);
        assertEquals(2, subsets.size());
    }

    @Test
    public void testSplitAt() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        DataSet d = new DataSet(data, CONTINUOUS);
        Tuple<DataSet, DataSet> subsets = d.splitAt(1, 0.5);
        assertEquals(1, subsets.first().sampleCount());
        assertEquals(1, subsets.last().sampleCount());
    }

    @Test
    public void testSplitByContinuous() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}, {0, -1, 0, 0}});
        DataSet d = new DataSet(data, CONTINUOUS);
        Tuple<Double, Tuple<DataSet, DataSet>> subsets = d.splitByContinuousAttribute(1);
        assertEquals(2, subsets.last().first().sampleCount());
        assertEquals(1, subsets.last().last().sampleCount());
        assertEquals(0.5, subsets.first(), DELTA);
    }
}
