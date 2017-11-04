package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.Model;
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
                assertEquals(a.getMean(), 3, DELTA);
                assertEquals(a.getStdev(), 1.4142135623730951, DELTA);
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

        NaiveBayes gnb = new NaiveBayes();
        Model m = gnb.fit(d);
        int prediction = m.predict(new Vector(new double[]{1.1, 1}));
        assertEquals(1, prediction);
    }

    @Test
    public void testGaussianNaiveBayesPredictWithData() throws Throwable {
        Matrix data = loadEcoliData(RandomForestTest.class);
        Vector attributeTypes = new Vector(new int[]{CONTINUOUS, CONTINUOUS, DISCRETE, DISCRETE, CONTINUOUS, CONTINUOUS, CONTINUOUS,});
        Data dataSet = new Data(data, attributeTypes);

        NaiveBayes gnb = new NaiveBayes();
        NaiveBayesModel m = (NaiveBayesModel)gnb.fit(dataSet);

        LOG.info("NaiveBayes accuracy: {}%", m.accuracy(dataSet));
    }

    @Test
    public void testNaiveBayes() throws Throwable {
        Matrix data = loadEcoliData(RandomForestTest.class);
        Vector attributeTypes = new Vector(new int[]{CONTINUOUS, CONTINUOUS, DISCRETE, DISCRETE, CONTINUOUS, CONTINUOUS, CONTINUOUS,});
        Data dataSet = new Data(data, attributeTypes);

        Algorithm naiveBayes = new NaiveBayes();

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, dataSet);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes accuracy: {}%", accuracies.mean());
    }
}
