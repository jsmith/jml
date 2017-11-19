package ca.jacob.cs6735.test;

import ca.jacob.jml.KFold;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.ensemble.Adaboost;
import ca.jacob.jml.nb.NaiveBayes;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Report;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.loadBreastCancerData;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static ca.jacob.cs6735.DataUtil.readCSV;
import static ca.jacob.jml.util.ML.removeSamplesWith;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AdaboostTest {
    private static final Logger LOG = LoggerFactory.getLogger(AdaboostTest.class);

    private KFold kFold;

    @Before
    public void init() {
        kFold = new KFold(5, 23l);
    }

    @Test
    public void testID3WithData() throws Throwable {
        ID3 id3 = new ID3(1); // stumps
        Adaboost adaboost = new Adaboost(id3, 100, 0.3);

        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data); //ignore these for now
        Matrix mat = new Matrix(data);
        Vector v = mat.col(mat.colCount()-1);
        v = v.replace(2, -1);
        v = v.replace(4, 1);
        mat.setCol(mat.colCount()-1, v);
        mat.dropCol(0); // removing id

        Vector attributeTypes = new Vector(new int[mat.colCount()-1]);
        attributeTypes.fill(DISCRETE);

        Report r = kFold.generateReport(adaboost, new DataSet(mat, attributeTypes));

        Vector accuracies = r.getAccuracies();
        assertTrue(accuracies.mean() > 90);
        LOG.info("ID3 KFold test accuracy: {}", accuracies.mean());
    }

    @Test
    public void testNaiveBayesWithData() throws Throwable {
        NaiveBayes nb = new NaiveBayes();
        Adaboost adaboost = new Adaboost(nb, 50, 0.3);
        DataSet dataset = loadBreastCancerData(AdaboostTest.class);
        Vector y = dataset.getY();
        y = y.replace(2, -1);
        y = y.replace(4, 1);
        dataset.setY(y);

        Report r = kFold.generateReport(adaboost, dataset);

        Vector accuracies = r.getAccuracies();
        assertTrue(accuracies.mean() > 90);
        LOG.info("NB KFold Test accuracy: {}", accuracies.mean());
    }
}
