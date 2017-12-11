package ca.jacob.jml.cs6735;

import ca.jacob.jml.*;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.DataUtil.*;
import static junit.framework.Assert.assertEquals;

public class KFoldTest {
    private static final Logger LOG = LoggerFactory.getLogger(KFoldTest.class);

    private KFold kFold;
    private Dataset dataset;
    private Matrix x;
    private Vector y;

    @Before
    public void init() {
        kFold = new KFold(5);
        x = new Matrix(new int[][]{{1}, {0}, {1}, {0}, {1}, {1}});
        y = new Vector(new int[]{1, 0, 1, 0, 1, 1});
        dataset = new Dataset(x, y, Dataset.DISCRETE);
    }

    @Test
    public void testSplit() {
        List<Tuple<Vector, Vector>> indices = kFold.generateIndices(dataset);
        assertEquals(indices.size(), 5);
        for(Tuple<Vector, Vector> entry : indices) {
            Vector trainIndices = entry.first();
            Vector testIndices = entry.last();
            assertEquals(6, trainIndices.length() + testIndices.length());
        }
    }

    @Test
    public void testKFoldProcess() throws Throwable {
        Dataset dataset = loadBreastCancerData(KFoldTest.class);

        Algorithm a = new ID3(ID3.MAX_LEVEL_NONE, 1);

        Report r = kFold.generateReport(a, dataset);
        Vector accuracies = r.getAccuracies();
        LOG.info("KFold test accuracy: {}", accuracies.mean());
    }

    @Test
    public void testKFoldProcessWithMushroom() throws Throwable {
        Dataset dataset = loadMushroomData(KFoldTest.class);

        Algorithm a = new ID3(ID3.MAX_LEVEL_NONE, 1);

        Report r = kFold.generateReport(a, dataset);
        LOG.info("KFold test accuracy for mushroom data: {}", r.accuracy());
    }

    @Test
    public void testKFoldProcessWithCar() throws Throwable {
        Dataset dataset = loadCarData(KFoldTest.class);

        Algorithm a = new ID3(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);

        Report r = kFold.generateReport(a, dataset);
        LOG.info("KFold test accuracy for car data: {}", r.accuracy());
    }

    @Test
    public void testKFoldProcessWithEcoli() throws Throwable {
        Dataset dataset = loadEColiData(KFoldTest.class);

        Algorithm a = new ID3(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);

        Report r = kFold.generateReport(a, dataset);
        Vector accuracies = r.getAccuracies();

        LOG.info("KFold test accuracy for E. Coli Data: {}", accuracies.mean());
    }
}
