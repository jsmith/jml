package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.ID3Model;
import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
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
    private Matrix x;
    private Vector y;

    @Before
    public void init() {
        id3 = new ID3(ID3.MAX_LEVEL_NONE);
        x = new Matrix(new int[][]{
                {1, 1, 1},
                {1,0, 1},
                {1,0, 0}});
        y = new Vector(new int[]{
                1,
                0,
                0});
    }

    @Test
    public void testTrain() {
        ID3Model model = (ID3Model) id3.fit(x, y);

        Node root = model.getRoot();
        assertEquals((int)1, root.getAttribute());
        assertEquals(2, root.getChildren().size());
        assertEquals(false, root.isLeaf());

        Matrix nodeZeroData = root.getChildren().get(0).getData();
        Matrix nodeOneData = root.getChildren().get(1).getData();
        assertEquals((int)1, nodeOneData.rowCount());
        assertEquals((int)2, nodeZeroData.rowCount());
    }

    @Test
    public void testPredict() {
        Model model = id3.fit(x, y);
        assertEquals((int) 1, model.predict(new Vector(new int[]{1, 1, 1})));
        assertEquals((int)0, model.predict(new Vector(new int[]{1, 0, 1})));
        assertEquals((int)1, model.predict(new Vector(new int[]{0, 1, 1})));
    }

    @Test
    public void testMaxLevel() {
        id3.setMaxLevel(1);
        ID3Model model = (ID3Model)id3.fit(x, y);

        Node root = model.getRoot();
        assertEquals(0, root.getChildren().size());
        assertEquals((int)0, model.predict(new Vector(new int[]{1, 1, 1})));
    }

    @Test
    public void testWithData() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);

        Matrix x = new Matrix(data);
        Vector y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);

        ID3 id3 = new ID3(2);
        ID3Model model = (ID3Model) id3.fit(x, y);

        double accuracy = model.accuracy(x, y);
        //assertEquals(accuracy, 100.);

        assertEquals((int) 2, model.depth());
    }

    @Test
    public void testNoLimit() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);

        Matrix x = new Matrix(data);
        Vector y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);

        ID3 id3 = new ID3(ID3.MAX_LEVEL_NONE);
        ID3Model model = (ID3Model) id3.fit(x, y);

        double accuracy = model.accuracy(x, y);
        assertEquals((double) 100, accuracy);
    }
}
