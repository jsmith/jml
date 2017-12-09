package ca.jacob.cs6735;

import ca.jacob.jml.tree.*;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.junit.Before;
import org.junit.Test;

import static ca.jacob.jml.DataSet.CONTINUOUS;
import static ca.jacob.jml.DataSet.DISCRETE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class NodeTest {
    private static final double DELTA =  1e-10;
    private Node node;
    private DataSet dataSet;

    @Before
    public void setup() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        Vector attributeTypes = new Vector(new int[]{DISCRETE, DISCRETE, DISCRETE});
        this.dataSet = new DataSet(data, attributeTypes);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.setAttribute(0);
    }

    @Test
    public void testEntropy() {
        assertEquals(1, dataSet.entropy(), DELTA);
    }

    @Test
    public void testSplit() {
        node.split(dataSet);
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
        DataSet dataSet = new DataSet(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataSet);
        assertEquals(2, node.getChildren().size());

        data = new Matrix(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        dataSet = new DataSet(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataSet);
        assertNull(node.getChildren());

        data = new Matrix(new int[][]{
                {0, 0, 3, 1},
                {0, 0, 2, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        dataSet = new DataSet(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataSet);
        assertEquals(2, node.getChildren().size());
        assertEquals(0.5, ((ContinuousChildren)node.getChildren()).getPivot(), DELTA);

        data = new Matrix(new int[][]{
                {0, 0, 1, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        dataSet = new DataSet(data, CONTINUOUS);
        node = new Node(ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split(dataSet);
        assertEquals(2, node.getChildren().size());
        assertEquals(0.5, ((ContinuousChildren)node.getChildren()).getPivot(), DELTA);
        assertEquals(1, node.getChildren().get(0).sampleCount());
        assertEquals(3, node.getChildren().get(1).sampleCount());
    }

    @Test
    public void testDepth() {
        DataSet data = new DataSet(new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}}), DISCRETE);
        node.split(data);
        assertEquals(1, node.depth());
    }
}
