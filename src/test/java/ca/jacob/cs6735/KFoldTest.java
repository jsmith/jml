package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.ID3Model;
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
    private Integer[][] x;
    private Integer[] y;

    @Before
    public void init() {
        kFold = new KFold(5);
        x = new Integer[][]{{1}, {0}, {1}, {0}, {1}, {1}};
        y = new Integer[]{1, 0, 1, 0, 1, 1};
    }

    @Test
    public void testSplit() {
        Map<Integer[][], Integer[]> map = kFold.split(x, y);
        assertEquals(map.size(), 5);
        int count = 0;
        for(Map.Entry<Integer[][], Integer[]> entry : map.entrySet()) {
            assertEquals(entry.getKey()[0][0], entry.getValue()[0]);
            count += entry.getKey().length;
        }
        assertEquals(6, count);
    }

    @Test
    public void testKFoldProcess() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);
        Matrix mat = new Matrix(data);
        y = mat.col(mat.colCount()-1).toIntegerArray();
        x = mat.toIntArray();
        Vector accuracy = new Vector();
        for(Map.Entry<Integer[][], Integer[]> entry : kFold.split(x, y).entrySet()) {
            Algorithm a = new ID3(ID3.LEVEL_NONE);
            Model m = a.fit(entry.getKey(), entry.getValue());
            accuracy.add(m.accuracy(x, y));
        }
    }
}
