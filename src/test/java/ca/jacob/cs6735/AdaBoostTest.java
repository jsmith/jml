package ca.jacob.cs6735;

import ca.jacob.jml.KFold;
import ca.jacob.jml.ensemble.AdaBoost;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.bayes.NaiveBayes;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.Report;
import ca.jacob.jml.math.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;
import static ca.jacob.jml.Util.readCSV;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AdaBoostTest {
    private static final Logger LOG = LoggerFactory.getLogger(AdaBoostTest.class);

    private KFold kFold;

    @Before
    public void init() {
        kFold = new KFold(5, 23l);
    }

    @Test
    public void testID3WithData() throws Throwable {
        ID3 id3 = new ID3(1); // stumps
        AdaBoost adaBoost = new AdaBoost(id3, 100, 0.3);

        DataSet dataset = loadBreastCancerData(AdaBoostTest.class);

        Report r = kFold.generateReport(adaBoost, dataset);

        Vector accuracies = r.getAccuracies();
        LOG.info("ID3 KFold test accuracy: {}", accuracies.mean());
        assertTrue(accuracies.mean() > 90);
    }

    @Test
    public void testNaiveBayesWithData() throws Throwable {
        NaiveBayes nb = new NaiveBayes();
        AdaBoost adaBoost = new AdaBoost(nb, 50, 0.3);
        DataSet dataset = loadBreastCancerData(AdaBoostTest.class);
        Vector y = dataset.getY();
        y = y.replace(2, -1);
        y = y.replace(4, 1);
        dataset.setY(y);

        Report r = kFold.generateReport(adaBoost, dataset);

        Vector accuracies = r.getAccuracies();
        LOG.info("NB KFold Test accuracy: {}", accuracies.mean());
        assertTrue(accuracies.mean() > 90);
    }
}
