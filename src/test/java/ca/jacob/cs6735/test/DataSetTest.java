package ca.jacob.cs6735.test;

import ca.jacob.jml.util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.cs6735.DataUtil.loadLetterData;
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

        try {
            subsets = d.splitAt(0, 0);
        } catch (DataException e) {
            // Do nothing!
        }
    }

    @Test
    public void testSplitByContinuous() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}, {0, -1, 0, 0}});
        DataSet d = new DataSet(data, CONTINUOUS);
        Tuple<Double, Tuple<DataSet, DataSet>> subsets = d.splitByContinuousAttribute(1);
        assertEquals(2, subsets.last().first().sampleCount());
        assertEquals(1, subsets.last().last().sampleCount());
        assertEquals(0.5, subsets.first(), DELTA);

        data = new Matrix(new int[][]{
                {0, 0, 1, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        d = new DataSet(data, CONTINUOUS);
        subsets = d.splitByContinuousAttribute(2);
        assertEquals(1, subsets.last().first().sampleCount());
        assertEquals(3, subsets.last().last().sampleCount());
    }

    @Test
    public void testSplitByContinuousWithRealData() throws Throwable {
        DataSet d = loadLetterData(DataSetTest.class);
        Tuple<DataSet, DataSet> subset = d.splitAt(15, 2.5);
        Vector under = subset.first().attribute(15);
        Vector over = subset.last().attribute(15);

        for(double v : under) {
            assertTrue(v < 2.5);
        }

        for(double v : over) {
            assertTrue(v > 2.5);
        }

        assertEquals(d.sampleCount(), under.length() + over.length());
    }

    @Test
    public void testEntropy() throws Throwable {
        DataSet d = loadLetterData(DataSetTest.class);
        assertEquals(4.699811, d.entropy(), DELTA);
    }
}
