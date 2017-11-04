package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.nb.NaiveBayes;
import ca.jacob.cs6735.nb.NaiveBayesModel;
import ca.jacob.cs6735.nb.ClassSummary;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.test.DataUtil.loadEcoliData;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class NaiveBayesTest {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesTest.class);

    @Test
    public void testGaussianNaiveBayesTrain() {
        Matrix data = new Matrix(new int[][]{{1,20,1}, {2,21,0}, {3,22,1}, {4,22,0}});
        Vector y = data.col(data.colCount()-1);
        data.dropCol(data.colCount()-1);
        Matrix x = data;
        NaiveBayes gnb = new NaiveBayes();
        NaiveBayesModel m = (NaiveBayesModel) gnb.fit(x, y);
        List<ClassSummary> summaries = m.getSummaries();
        int i = 0;
        for(ClassSummary s : summaries) {
            if(i == 0) {
                assertEquals(s.getMeans(), new Vector(new double[]{3, 21.5}));
                assertEquals(s.getStdevs(), new Vector(new double[]{1.4142135623730951, 0.7071067811865476}));
            }
            LOG.info("summary: {}", s);
            i++;
        }
    }

    @Test
    public void testGaussianNaiveBayesPredict() {
        Matrix x = new Matrix(new double[][]{{1, 0.5},{1.2, 0.7},{20, 5}});
        Vector y = new Vector(new double[]{1, 1, 2});
        NaiveBayes gnb = new NaiveBayes();
        Model m = gnb.fit(x, y);
        int prediction = m.predict(new Vector(new double[]{1.1, 1}));
        assertEquals(1, prediction);
    }

    @Test
    public void testGaussianNaiveBayesPredictWithData() throws Throwable {
        Matrix data = loadEcoliData(RandomForestTest.class);
        Vector y = data.col(data.colCount()-1);
        data.dropCol(data.colCount()-1);
        Matrix x = data;

        NaiveBayes gnb = new NaiveBayes();
        NaiveBayesModel m = (NaiveBayesModel)gnb.fit(x, y);

        LOG.info("NaiveBayes accuracy: {}%", m.accuracy(x, y));
    }

    @Test
    public void testNaiveBayes() throws Throwable {
        Matrix data = loadEcoliData(RandomForestTest.class);

        Algorithm naiveBayes = new NaiveBayes();

        KFold kFold = new KFold(5);
        Report r = kFold.generateReport(naiveBayes, data);
        Vector accuracies = r.getAccuracies();

        LOG.info("NaiveBayes accuracy: {}%", accuracies.mean());
    }
}
