package ca.jacob.jml.cs6735;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.tree.*;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.junit.Before;
import org.junit.Test;

import static ca.jacob.jml.Dataset.CONTINUOUS;
import static ca.jacob.jml.Dataset.DISCRETE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class NodeTest {
    private static final double DELTA =  1e-10;
    private Node node;
    private Dataset dataset;

    @Before
    public void setup() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        Vector attributeTypes = new Vector(new int[]{DISCRETE, DISCRETE, DISCRETE});
        this.dataset = new Dataset(data, attributeTypes);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.setAttribute(0);
    }

    @Test
    public void testEntropy() {
        assertEquals(1, dataset.entropy(), DELTA);
    }

    @Test
    public void testSplit() {
        node.split(dataset);
        assertEquals(2, node.getChildren().size());
        assertNull(node.getChildren().get(0).getChildren());
        assertNull(node.getChildren().get(1).getChildren());
    }

    @Test
    public void testContinuous() {
        Matrix data = new Matrix(new int[][]{
                {0, 0, 1, 1},
                {0, 0, 0, 0}
        });
        Dataset dataset = new Dataset(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataset);
        assertEquals(2, node.getChildren().size());

        data = new Matrix(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        dataset = new Dataset(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataset);
        assertNull(node.getChildren());

        data = new Matrix(new int[][]{
                {0, 0, 3, 1},
                {0, 0, 2, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        dataset = new Dataset(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataset);
        assertEquals(2, node.getChildren().size());
        assertEquals(0.5, ((ContinuousChildren)node.getChildren()).getPivot(), DELTA);

        data = new Matrix(new int[][]{
                {0, 0, 1, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        dataset = new Dataset(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataset);
        assertEquals(2, node.getChildren().size());
        assertEquals(0.5, ((ContinuousChildren)node.getChildren()).getPivot(), DELTA);
    }

    @Test
    public void testDepth() {
        Dataset data = new Dataset(new Matrix(new int[][]{
                {0, 1, 1, 1},
                {0, 0, 0, 0}
        }), DISCRETE);
        node.split(data);
        assertEquals(1, node.depth());
    }
}
