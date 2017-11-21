package ca.jacob.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.Report;
import ca.jacob.jml.math.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;
import static ca.jacob.cs6735.DataUtil.loadCarData;
import static org.junit.Assert.assertTrue;

public class RandomForestTest {
    private static final Logger LOG = LoggerFactory.getLogger(RandomForestTest.class);

    @Test
    public void testRandomForest() throws Throwable {
        DataSet dataSet = loadBreastCancerData(RandomForestTest.class);
        LOG.info("dataSet samples: {}, attributes: {}", dataSet.sampleCount(), dataSet.attributeCount());

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE);
        RandomForest rf = new RandomForest(id3, 500, 0.6);

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(rf, dataSet);
        Vector accuracies = r.getAccuracies();

        assertTrue(accuracies.mean() > 95);
        LOG.info("RandomForest accuracy: {}", accuracies.sum()/accuracies.length());
    }

    @Test
    public void testWithCarData() throws Throwable {
        DataSet dataSet = loadCarData(RandomForestTest.class);
        LOG.info("dataSet samples: {}, attributes: {}", dataSet.sampleCount(), dataSet.attributeCount());

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE);
        RandomForest rf = new RandomForest(id3, 500, 0.6);

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(rf, dataSet);
        Vector accuracies = r.getAccuracies();

        assertTrue(accuracies.mean() > 88);
        LOG.info("RandomForest accuracy: {}", accuracies.mean());
    }
}