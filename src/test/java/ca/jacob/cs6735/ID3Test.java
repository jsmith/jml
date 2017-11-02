package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.ID3Model;
import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Matrix;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;
import static junit.framework.Assert.assertEquals;

public class ID3Test {
    private static final Logger LOG = LoggerFactory.getLogger(ID3Test.class);

    private ID3 id3;

    @Before
    public void init() {
        id3 = new ID3(ID3.LEVEL_NONE);
    }

    @Test
    public void testTrain() {
        ID3Model model = (ID3Model) id3.fit(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0});

        Node root = model.getRoot();
        assertEquals((Integer)1, root.getAttribute());
        assertEquals(2, root.getNodes().size());
        assertEquals(false, root.isLeaf());

        Matrix nodeZeroData = root.getNodes().get(0).getData();
        Matrix nodeOneData = root.getNodes().get(1).getData();
        assertEquals((Integer)1, nodeOneData.rowCount());
        assertEquals((Integer)2, nodeZeroData.rowCount());
    }

    @Test
    public void testPredict() {
        Model model = id3.fit(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0});
        assertEquals((Integer) 1, model.predict(new Integer[]{1, 1, 1}));
        assertEquals((Integer)0, model.predict(new Integer[]{1, 0, 1}));
        assertEquals((Integer)1, model.predict(new Integer[]{0, 1, 1}));
    }

    @Test
    public void testMaxLevel() {
        id3.setMaxLevel(1);
        ID3Model model = (ID3Model)id3.fit(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0});

        Node root = model.getRoot();
        assertEquals(0, root.getNodes().size());
        assertEquals((Integer)0, model.predict(new Integer[]{1, 1, 1}));
    }

    @Test
    public void testWithData() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);
        Matrix m = new Matrix(data);
        Integer[] y = m.col(m.colCount()-1).toIntegerArray();
        m.dropCol(m.colCount()-1);
        Integer[][] x = m.toIntArray();
        ID3 id3 = new ID3(ID3.LEVEL_NONE);
        Model model = id3.fit(x, y);
        Double accuracy = model.accuracy(x, y);
        assertEquals(accuracy, 100.);
    }
}
