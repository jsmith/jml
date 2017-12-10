package ca.jacob.jml.cs6735;

import ca.jacob.jml.Algorithm;
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
import static ca.jacob.cs6735.DataUtil.loadCarData;
import static ca.jacob.jml.Util.readCSV;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AdaBoostTest {
    private static final Logger LOG = LoggerFactory.getLogger(AdaBoostTest.class);

    private KFold kFold;

    @Before
    public void init() {
        kFold = new KFold(5);
    }

    @Test
    public void testID3WithData() throws Throwable {
        ID3 id3 = new ID3(1); // stumps
        AdaBoost adaBoost = new AdaBoost(id3, 50, 0.3);

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

        Report r = kFold.generateReport(adaBoost, dataset);

        Vector accuracies = r.getAccuracies();
        LOG.info("NB KFold Test accuracy: {}", accuracies.mean());
        assertTrue(accuracies.mean() > 90);
    }

    @Test
    public void testMultiClass() throws Throwable {
        Algorithm adaBoost = new AdaBoost(new ID3(1), 400, 0.4);
        //adaBoost = new ID3(ID3.MAX_LEVEL_NONE);

        Report r = kFold.generateReport(adaBoost, loadCarData(AdaBoostTest.class));

        Vector accuracies = r.getAccuracies();
        LOG.info("Car Multi Class AdaBoost KFold Test accuracy: {}", accuracies.mean());
        //assertTrue(accuracies.mean() > 90);
    }
}
