package ca.jacob.jml.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Dataset;
import ca.jacob.jml.Model;
import ca.jacob.jml.tree.Children;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.tree.ID3Model;
import ca.jacob.jml.tree.Node;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.*;
import static ca.jacob.jml.Dataset.DISCRETE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ID3Test {
    private static final Logger LOG = LoggerFactory.getLogger(ID3Test.class);
    private static final double DELTA = 1e-5;

    private ID3 id3;
    private Dataset dataset;

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
        dataset = new Dataset(x, y, DISCRETE);
    }

    @Test
    public void testTrain() {
        ID3Model model = (ID3Model) id3.fit(dataset);

        Node root = model.getRoot();
        assertEquals((int)1, root.getAttribute());
        assertEquals(2, root.getChildren().size());
        assertEquals(false, root.isLeaf());

        Children children = root.getChildren();
        assertNotNull(children);
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
        Dataset d = loadBreastCancerData(ID3Test.class);

        ID3 id3 = new ID3(2);
        ID3Model model = (ID3Model) id3.fit(d);

        assertEquals((int) 2, model.depth());
    }

    @Test
    public void testNoLimit() throws Throwable {
        Dataset d = loadBreastCancerData(ID3Test.class);

        ID3 id3 = new ID3(ID3.MAX_LEVEL_NONE);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);
        assertEquals((double) 100, accuracy);
    }

    @Test
    public void testNoLimitCarData() throws Throwable {
        Dataset d = loadCarData(ID3Test.class);

        ID3 id3 = new ID3(ID3.MAX_LEVEL_NONE);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);
        assertEquals((double) 100, accuracy);
    }

    @Test
    public void testDepthOne() throws Throwable {
        Dataset d = loadBreastCancerData(ID3Test.class);

        ID3 id3 = new ID3(1);
        ID3Model model = (ID3Model) id3.fit(d);

        double accuracy = model.accuracy(d);
        assertEquals(1, model.depth());
        LOG.info("accuracy for max depth 1 is {}", accuracy);
    }

    @Test
    public void testContinuousData() throws Throwable {
        Dataset letterData = loadLetterData(ID3Test.class);

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        Model model = id3.fit(letterData);

        double accuracy = model.accuracy(letterData);
        assertEquals(93.15, accuracy, DELTA);
    }
}
