package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.ID3Model;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
import ca.jacob.cs6735.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;
import static ca.jacob.cs6735.util.ML.shuffle;
import static junit.framework.Assert.assertEquals;

public class KFoldTest {
    private static final Logger LOG = LoggerFactory.getLogger(KFoldTest.class);

    private KFold kFold;
    private Data dataset;
    private Matrix x;
    private Vector y;

    @Before
    public void init() {
        kFold = new KFold(5, 23l);
        x = new Matrix(new int[][]{{1}, {0}, {1}, {0}, {1}, {1}});
        y = new Vector(new int[]{1, 0, 1, 0, 1, 1});
        dataset = new Data(x, y, Data.DISCRETE);
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

        Data dataset = new Data(mat, Data.DISCRETE);

        Algorithm a = new ID3(5, 200);

        Report r = kFold.generateReport(a, dataset);
        Vector accuracies = r.getAccuracies();
        LOG.info("kfold test accuracy: {}", accuracies.sum()/accuracies.length());
    }
}
