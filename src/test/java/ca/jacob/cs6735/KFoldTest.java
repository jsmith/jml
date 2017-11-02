package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;
import static ca.jacob.cs6735.util.ML.shuffle;
import static junit.framework.Assert.assertEquals;

public class KFoldTest {
    private static final Logger LOG = LoggerFactory.getLogger(KFoldTest.class);

    private KFold kFold;
    private Matrix x;
    private Vector y;

    @Before
    public void init() {
        kFold = new KFold(5);
        x = new Matrix(new Integer[][]{{1}, {0}, {1}, {0}, {1}, {1}});
        y = new Vector(new Integer[]{1, 0, 1, 0, 1, 1});
    }

    @Test
    public void testSplit() {
        Map<Vector, Vector> map = kFold.split(x);
        assertEquals(map.size(), 5);
        for(Map.Entry<Vector, Vector> entry : map.entrySet()) {
            Vector trainIndices = entry.getKey();
            Vector testIndices = entry.getValue();
            assertEquals(6, trainIndices.length() + testIndices.length());
        }
    }

    @Test
    public void testKFoldProcess() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);
        Matrix mat = new Matrix(data);

        Algorithm a = new ID3(ID3.MAX_LEVEL_NONE);

        Map<Vector, Vector> indices = kFold.split(mat);
        for(Map.Entry<Vector, Vector> entry : indices.entrySet()) {
            Vector trainIndices = entry.getKey();
            Vector testIndices = entry.getValue();

            Matrix x = mat.rows(trainIndices);
            Vector y = mat.col(x.colCount()-1);
            x.dropCol(x.colCount()-1);
            Model m = a.fit(x, y);

            x = mat.rows(testIndices);
            y = mat.col(x.colCount()-1);
            x.dropCol(x.colCount()-1);
            m.accuracy(x, y);
        }
    }
}
