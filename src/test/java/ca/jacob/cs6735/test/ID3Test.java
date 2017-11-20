package ca.jacob.cs6735.test;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.dt.ID3Model;
import ca.jacob.jml.dt.Node;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.*;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static ca.jacob.jml.util.ML.removeSamplesWith;
import static junit.framework.Assert.assertEquals;

public class ID3Test {
    private static final Logger LOG = LoggerFactory.getLogger(ID3Test.class);
    private static final double DELTA = 1e-5;

    private ID3 id3;
    private DataSet dataset;

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
        dataset = new DataSet(x, y, DISCRETE);
    }

    @Test
    public void testTrain() {
        ID3Model model = (ID3Model) id3.fit(dataset);

        Node root = model.getRoot();
        assertEquals((int)1, root.getAttribute());
        assertEquals(2, root.getChildren().size());
        assertEquals(false, root.isLeaf());

        DataSet nodeZeroDataSet = root.getChildren().get(0).getDataSet();
        DataSet nodeOneDataSet = root.getChildren().get(1).getDataSet();
        assertEquals((int)1, nodeOneDataSet.sampleCount());
        assertEquals((int)2, nodeZeroDataSet.sampleCount());
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

        DataSet d = new DataSet(new Matrix(data), DISCRETE);

        ID3 id3 = new ID3(2);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);

        assertEquals((int) 2, model.depth());
    }

    @Test
    public void testNoLimit() throws Throwable {
        DataSet d = loadBreastCancerData(ID3Test.class);

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
        DataSet d = new DataSet(m, DISCRETE);

        ID3 id3 = new ID3(1);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);
        assertEquals(1, model.depth());
        LOG.info("accuracy for max depth 1 is {}", accuracy);
    }

    @Test
    public void testContinuousData() throws Throwable {
        DataSet letterData = loadLetterData(ID3Test.class);

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        Model model = id3.fit(letterData);

        double accuracy = model.accuracy(letterData);
        assertEquals(93.15, accuracy, DELTA);
    }
}
