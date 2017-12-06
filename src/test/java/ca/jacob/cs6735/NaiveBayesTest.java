package ca.jacob.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.Model;
import ca.jacob.jml.math.distribution.GaussianDistribution;
import ca.jacob.jml.bayes.ContinuousAttribute;
import ca.jacob.jml.bayes.NaiveBayes;
import ca.jacob.jml.bayes.NaiveBayesModel;
import ca.jacob.jml.bayes.ClassSummary;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.Report;
import ca.jacob.jml.math.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;
import static ca.jacob.cs6735.DataUtil.loadEColiData;
import static ca.jacob.jml.DataSet.CONTINUOUS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class NaiveBayesTest {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesTest.class);
    private static final double DELTA = 1e-5;

    @Test
    public void testGaussianNaiveBayesTrain() {
        Matrix data = new Matrix(new int[][]{{1,20,1}, {2,21,0}, {3,22,1}, {4,22,0}});
        DataSet d = new DataSet(data, CONTINUOUS);

        NaiveBayes gnb = new NaiveBayes(new GaussianDistribution());
        NaiveBayesModel m = (NaiveBayesModel) gnb.fit(d);
        List<ClassSummary> summaries = m.getSummaries();
        int i = 0;
        for(ClassSummary s : summaries) {
            if(i == 0) {
                ContinuousAttribute a = (ContinuousAttribute) s.getAttributes().get(0);
                assertEquals(3, a.getMean(), DELTA);
                assertEquals(1.4142135623730951, a.getStdev(), DELTA);
            }
            LOG.info("summary: {}", s);
            i++;
        }
    }

    @Test
    public void testGaussianNaiveBayesPredict() {
        Matrix x = new Matrix(new double[][]{{1, 0.5},{1.2, 0.7},{20, 5}});
        Vector y = new Vector(new double[]{1, 1, 2});
        DataSet d = new DataSet(x, y, CONTINUOUS);

        NaiveBayes gnb = new NaiveBayes(new GaussianDistribution());
        Model m = gnb.fit(d);
        int prediction = m.predict(new Vector(new double[]{1.1, 1}));
        assertEquals(1, prediction);
    }

    @Test
    public void testGaussianNaiveBayesPredictWithData() throws Throwable {
        DataSet dataSetSet = loadEColiData(RandomForestTest.class);

        NaiveBayes gnb = new NaiveBayes(new GaussianDistribution());
        NaiveBayesModel m = (NaiveBayesModel)gnb.fit(dataSetSet);

        List<ClassSummary> summaries = m.getSummaries();
        Vector classProbabilities = new Vector();
        for(ClassSummary c : summaries) {
            classProbabilities.add(c.getClassProbability());
        }
        assertEquals(1, classProbabilities.sum(), DELTA);

        LOG.info("NaiveBayes Accuracy: {}%", m.accuracy(dataSetSet));
    }

    @Test
    public void testNaiveBayes() throws Throwable {
        DataSet dataSetSet = loadEColiData(RandomForestTest.class);

        Algorithm naiveBayes = new NaiveBayes(new GaussianDistribution());

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, dataSetSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes accuracy: {}%", accuracies.mean());
    }

    @Test
    public void testNaiveBayesWithBreastCancer() throws Throwable {
        DataSet dataSetSet = loadBreastCancerData(RandomForestTest.class);

        Algorithm naiveBayes = new NaiveBayes(new GaussianDistribution());

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, dataSetSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes Accuracy: {}%", accuracies.mean());
    }
}
