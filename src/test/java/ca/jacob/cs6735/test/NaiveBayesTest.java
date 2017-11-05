package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.distribution.GaussianDistribution;
import ca.jacob.cs6735.nb.ContinuousAttribute;
import ca.jacob.cs6735.nb.NaiveBayes;
import ca.jacob.cs6735.nb.NaiveBayesModel;
import ca.jacob.cs6735.nb.ClassSummary;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.test.DataUtil.loadBreastCancerData;
import static ca.jacob.cs6735.test.DataUtil.loadEcoliData;
import static ca.jacob.cs6735.util.Data.CONTINUOUS;
import static ca.jacob.cs6735.util.Data.DISCRETE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class NaiveBayesTest {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesTest.class);
    private static final double DELTA = 1e-5;

    @Test
    public void testGaussianNaiveBayesTrain() {
        Matrix data = new Matrix(new int[][]{{1,20,1}, {2,21,0}, {3,22,1}, {4,22,0}});
        Data d = new Data(data, CONTINUOUS);

        NaiveBayes gnb = new NaiveBayes();
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
        Data d = new Data(x, y, CONTINUOUS);

        NaiveBayes gnb = new NaiveBayes(new GaussianDistribution());
        Model m = gnb.fit(d);
        int prediction = m.predict(new Vector(new double[]{1.1, 1}));
        assertEquals(1, prediction);
    }

    @Test
    public void testGaussianNaiveBayesPredictWithData() throws Throwable {
        Matrix data = loadEcoliData(RandomForestTest.class);
        Vector attributeTypes = new Vector(new int[]{CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS,});
        Data dataSet = new Data(data, attributeTypes);

        NaiveBayes gnb = new NaiveBayes(new GaussianDistribution());
        NaiveBayesModel m = (NaiveBayesModel)gnb.fit(dataSet);

        List<ClassSummary> summaries = m.getSummaries();
        Vector classProbabilities = new Vector();
        for(ClassSummary c : summaries) {
            classProbabilities.add(c.getClassProbability());
        }
        assertEquals(1, classProbabilities.sum(), DELTA);

        LOG.info("NaiveBayes Accuracy: {}%", m.accuracy(dataSet));
    }

    @Test
    public void testNaiveBayes() throws Throwable {
        Matrix data = loadEcoliData(RandomForestTest.class);
        Vector attributeTypes = new Vector(new int[]{CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS,});
        Data dataSet = new Data(data, attributeTypes);

        Algorithm naiveBayes = new NaiveBayes(new GaussianDistribution());

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, dataSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes accuracy: {}%", accuracies.mean());
    }

    @Test
    public void testNaiveBayesWithBreastCancer() throws Throwable {
        Matrix data = loadBreastCancerData(RandomForestTest.class);
        Vector attributeTypes = new Vector(new int[data.colCount()-1]);
        attributeTypes.fill(DISCRETE);
        Data dataSet = new Data(data, attributeTypes);

        Algorithm naiveBayes = new NaiveBayes(new GaussianDistribution());

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, dataSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes Accuracy: {}%", accuracies.mean());
    }
}
