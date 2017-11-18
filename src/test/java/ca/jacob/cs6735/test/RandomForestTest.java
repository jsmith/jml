package ca.jacob.cs6735.test;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Report;
import ca.jacob.jml.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;
import static ca.jacob.jml.util.DataSet.DISCRETE;

public class RandomForestTest {
    private static final Logger LOG = LoggerFactory.getLogger(RandomForestTest.class);

    @Test
    public void testRandomForest() throws Throwable {
        DataSet dataSet = loadBreastCancerData(RandomForestTest.class);
        LOG.info("dataSet samples: {}, attributes: {}", dataSet.sampleCount(), dataSet.attributeCount());

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE);
        RandomForest rf = new RandomForest(id3, 1000, 0.6);

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(rf, dataSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("RandomForest accuracy: {}", accuracies.sum()/accuracies.length());
    }
}
