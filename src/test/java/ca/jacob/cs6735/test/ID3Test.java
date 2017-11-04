package ca.jacob.cs6735.test;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.ID3Model;
import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.Data.DISCRETE;
import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;
import static junit.framework.Assert.assertEquals;

public class ID3Test {
    private static final Logger LOG = LoggerFactory.getLogger(ID3Test.class);

    private ID3 id3;
    private Data dataset;

    @Before
    public void init() {
        id3 = new ID3(ID3.MAX_LEVEL_NONE);
        Matrix x = new Matrix(new int[][]{
                {1, 1, 1},
                {1,0, 1},
                {1,0, 0}});
        Vector y = new Vector(new int[]{
                1,
                0,
                0});
        dataset = new Data(x, y, DISCRETE);
    }

    @Test
    public void testTrain() {
        ID3Model model = (ID3Model) id3.fit(dataset);

        Node root = model.getRoot();
        assertEquals((int)1, root.getAttribute());
        assertEquals(2, root.getChildren().size());
        assertEquals(false, root.isLeaf());

        Data nodeZeroData = root.getChildren().get(0).getData();
        Data nodeOneData = root.getChildren().get(1).getData();
        assertEquals((int)1, nodeOneData.sampleCount());
        assertEquals((int)2, nodeZeroData.sampleCount());
    }

    @Test
    public void testPredict() {
        Model model = id3.fit(dataset);
        assertEquals((int) 1, model.predict(new Vector(new int[]{1, 1, 1})));
        assertEquals((int)0, model.predict(new Vector(new int[]{1, 0, 1})));
        assertEquals((int)1, model.predict(new Vector(new int[]{0, 1, 1})));
    }

    @Test
    public void testMaxLevel() {
        id3.setMaxLevel(1);
        ID3Model model = (ID3Model)id3.fit(dataset);

        Node root = model.getRoot();
        assertEquals(2, root.getChildren().size());
        assertEquals((int)1, model.predict(new Vector(new int[]{1, 1, 1})));
    }

    @Test
    public void testWithData() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);

        Data d = new Data(new Matrix(data), DISCRETE);

        ID3 id3 = new ID3(2);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);

        assertEquals((int) 2, model.depth());
    }

    @Test
    public void testNoLimit() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);

        Data d = new Data(new Matrix(data), DISCRETE);

        ID3 id3 = new ID3(ID3.MAX_LEVEL_NONE);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);
        assertEquals((double) 100, accuracy);
    }

    @Test
    public void testDepthOne() throws Throwable {
        String[][] data = readCSV(this.getClass().getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);

        Matrix m = new Matrix(data);
        Data d = new Data(m, DISCRETE);

        ID3 id3 = new ID3(1);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);
        assertEquals(1, model.depth());
        LOG.info("accuracy for max depth 1 is {}", accuracy);
    }
}
