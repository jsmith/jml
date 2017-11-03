package ca.jacob.cs6735.test;

import ca.jacob.cs6735.KFold;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.ensemble.Adaboost;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;
import static junit.framework.Assert.assertEquals;

public class AdaboostTest {
    private static final Logger LOG = LoggerFactory.getLogger(AdaboostTest.class);

    private KFold kFold;

    @Before
    public void init() {
        kFold = new KFold(5, 23l);
    }

    @Test
    public void testWithData() throws Throwable {
        ID3 id3 = new ID3(1); // stumps
        Adaboost adaboost = new Adaboost(id3, 5);

        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data); //ignore these for now
        Matrix mat = new Matrix(data);
        Vector v = mat.col(mat.colCount()-1);
        v.replace(2, -1);
        v.replace(4, 1);
        mat.setCol(mat.colCount()-1, v);
        mat.dropCol(0); // removing id

        Vector accuracies = new Vector();
        Map<Vector, Vector> indices = kFold.split(mat);
        for(Map.Entry<Vector, Vector> entry : indices.entrySet()) {
            Vector trainIndices = entry.getKey();
            Vector testIndices = entry.getValue();

            Matrix x = mat.rows(trainIndices);
            Vector y = x.col(x.colCount()-1);
            x.dropCol(x.colCount()-1);
            Model m = adaboost.fit(x, y);

            x = mat.rows(testIndices);
            y = x.col(x.colCount()-1);
            assertEquals(x.rowCount(), y.length());

            x.dropCol(x.colCount()-1);
            double accuracy = m.accuracy(x, y);
            accuracies.add(accuracy);
        }
        LOG.info("kfold test accuracy: {}", accuracies.sum()/accuracies.length());
    }
}
