package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.bayes.NaiveBayes;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.ensemble.RandomForest;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.test.DataUtil.loadBreastCancerData;

public class NaiveBayesTest {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesTest.class);

    @Test
    public void testNaiveBayes() throws Throwable {
        Matrix data = loadBreastCancerData(RandomForestTest.class);

        Algorithm naiveBayes = new NaiveBayes();

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, data);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes accuracy: {}", accuracies.sum()/accuracies.length());
    }
}
