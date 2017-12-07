package ca.jacob.cs6735;

import ca.jacob.jml.DataSet;
import ca.jacob.jml.KFold;
import ca.jacob.jml.Report;
import ca.jacob.jml.bayes.NaiveBayes;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.ensemble.AdaBoostModel;
import ca.jacob.jml.ensemble.MultiAdaBoost;
import ca.jacob.jml.ensemble.MultiAdaBoostModel;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Tuple;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;
import static ca.jacob.cs6735.DataUtil.loadCarData;
import static ca.jacob.jml.DataSet.DISCRETE;
import static org.junit.Assert.assertEquals;

public class MultiAdaBoostTest {
    private static final Logger LOG = LoggerFactory.getLogger(MultiAdaBoostTest.class);

    @Test
    public void testFit() {
        ID3 id3 = new ID3(1);
        MultiAdaBoost multiAdaboost = new MultiAdaBoost(id3, 100, 0.5);

        Matrix data = new Matrix(new int[][]{
            {1, 1, 1, 1},
            {1, 0, 1, 2},
            {1, 0, 0, 3},
        });
        DataSet d = new DataSet(data, DISCRETE);

        MultiAdaBoostModel m = (MultiAdaBoostModel) multiAdaboost.fit(d);
        List<Tuple<Integer, AdaBoostModel>> models = m.getModels();

        assertEquals(3, models.size());
    }

    @Test
    public void testMultiAdaboost() throws Throwable {
        ID3 id3 = new ID3(1);
        MultiAdaBoost multiAdaboost = new MultiAdaBoost(id3, 300, 0.3);

        DataSet d = loadCarData(MultiAdaBoostTest.class);

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(multiAdaboost, d);

        double mean = r.mean();
        LOG.info("Multi AdaBoost Mean for Car Data: {}", mean);
    }

    @Test
    public void testMultiAdaboostNaiveBayes() throws Throwable {
        MultiAdaBoost multiAdaboost = new MultiAdaBoost(new NaiveBayes(), 89, 0.7);

        DataSet d = loadBreastCancerData(MultiAdaBoostTest.class);

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(multiAdaboost, d);

        double mean = r.mean();
        LOG.info("{}", d);
        LOG.info("{}", multiAdaboost);
        LOG.info("{}", r);
        LOG.info("Multi AdaBoost with Naive Bayes Mean for Breast Cancer Data: {}", mean);
    }
}
