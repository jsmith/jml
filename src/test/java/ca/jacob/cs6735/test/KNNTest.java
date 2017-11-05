package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.distance.Hamming;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.ensemble.RandomForest;
import ca.jacob.cs6735.neighbors.KNN;
import ca.jacob.cs6735.neighbors.KNNModel;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.test.DataUtil.loadBreastCancerData;
import static ca.jacob.cs6735.util.Data.DISCRETE;

public class KNNTest {
    private static final Logger LOG = LoggerFactory.getLogger(KNNTest.class);

    @Test
    public void testKNN() throws Throwable {
        Matrix m = loadBreastCancerData(KNNTest.class);
        Data data = new Data(m, DISCRETE);
        LOG.info("data samples: {}, attributes: {}", data.sampleCount(), data.attributeCount());

        int k = 2;
        Algorithm knn = new KNN(k, false, new Hamming());

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(knn, data);
        Vector accuracies = r.getAccuracies();

        LOG.info("KNN {} Accuracy: {}", k, accuracies.sum()/accuracies.length());
    }
}
