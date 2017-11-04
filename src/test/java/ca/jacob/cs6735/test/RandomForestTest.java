package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.ensemble.RandomForest;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.test.DataUtil.loadBreastCancerData;
import static ca.jacob.cs6735.util.Data.DISCRETE;

public class RandomForestTest {
    private static final Logger LOG = LoggerFactory.getLogger(RandomForestTest.class);

    @Test
    public void testRandomForest() throws Throwable {
        Matrix m = loadBreastCancerData(RandomForestTest.class);
        Data data = new Data(m, DISCRETE);
        LOG.info("data samples: {}, attributes: {}", data.sampleCount(), data.attributeCount());

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE);
        RandomForest rf = new RandomForest(id3, 1000, 0.6);

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(rf, data);
        Vector accuracies = r.getAccuracies();

        LOG.info("RandomForest accuracy: {}", accuracies.sum()/accuracies.length());
    }
}
