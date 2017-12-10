package ca.jacob.jml.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.math.distance.Hamming;
import ca.jacob.jml.neighbors.KNN;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.Report;
import ca.jacob.jml.math.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;

public class KNNTest {
    private static final Logger LOG = LoggerFactory.getLogger(KNNTest.class);

    @Test
    public void testKNN() throws Throwable {
        DataSet dataSet = loadBreastCancerData(KNNTest.class);
        LOG.info("dataSet samples: {}, attributes: {}", dataSet.sampleCount(), dataSet.attributeCount());

        int k = 3;
        Algorithm knn = new KNN(k, false, new Hamming());

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(knn, dataSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("KNN {} Accuracy: {}", k, accuracies.sum()/accuracies.length());
    }
}
