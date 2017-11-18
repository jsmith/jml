package ca.jacob.cs6735.test;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Report;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.cs6735.DataUtil.readCSV;
import static ca.jacob.jml.util.ML.removeSamplesWith;
import static ca.jacob.jml.util.ML.shuffle;
import static junit.framework.Assert.assertEquals;

public class KFoldTest {
    private static final Logger LOG = LoggerFactory.getLogger(KFoldTest.class);

    private KFold kFold;
    private DataSet dataset;
    private Matrix x;
    private Vector y;

    @Before
    public void init() {
        kFold = new KFold(5, 23l);
        x = new Matrix(new int[][]{{1}, {0}, {1}, {0}, {1}, {1}});
        y = new Vector(new int[]{1, 0, 1, 0, 1, 1});
        dataset = new DataSet(x, y, DataSet.DISCRETE);
    }

    @Test
    public void testSplit() {
        Map<Vector, Vector> map = kFold.generateIndices(dataset);
        assertEquals(map.size(), 5);
        for(Map.Entry<Vector, Vector> entry : map.entrySet()) {
            Vector trainIndices = entry.getKey();
            Vector testIndices = entry.getValue();
            assertEquals(6, trainIndices.length() + testIndices.length());
        }
    }

    @Test
    public void testKFoldProcess() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data); //ignore these for now
        Matrix mat = new Matrix(data);
        mat.dropCol(0); // removing id

        DataSet dataset = new DataSet(mat, DataSet.DISCRETE);

        Algorithm a = new ID3(5, 200);

        Report r = kFold.generateReport(a, dataset);
        Vector accuracies = r.getAccuracies();
        LOG.info("kfold test accuracy: {}", accuracies.mean());
    }
}
