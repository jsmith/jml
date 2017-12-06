package ca.jacob.cs6735;

import ca.jacob.jml.tree.Children;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.tree.Node;
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

    @Before
    public void setup() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        Vector attributeTypes = new Vector(new int[]{DISCRETE, DISCRETE, DISCRETE});
        node = new Node(data, attributeTypes, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.setAttribute(0);
    }

    @Test
    public void testEntropy() {
        assertEquals(1, node.entropy(), DELTA);
    }

    @Test
    public void testSplit() {
        node.split();
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
        DataSet dataset = new DataSet(data, CONTINUOUS);
        node = new Node(dataset, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split();
        assertEquals(2, node.getChildren().size());

        data = new Matrix(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        dataset = new DataSet(data, CONTINUOUS);
        node = new Node(dataset, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split();
        assertNull(node.getChildren());

        data = new Matrix(new int[][]{
                {0, 0, 3, 1},
                {0, 0, 2, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        dataset = new DataSet(data, CONTINUOUS);
        node = new Node(dataset, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split();
        assertEquals(2, node.getChildren().size());
        assertEquals(0.5, node.getChildren().getPivot(), DELTA);

        data = new Matrix(new int[][]{
                {0, 0, 1, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        dataset = new DataSet(data, CONTINUOUS);
        node = new Node(dataset, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        node.split();
        assertEquals(2, node.getChildren().size());
        assertEquals(0.5, node.getChildren().getPivot(), DELTA);
        assertEquals(1, node.getChildren().get(0).sampleCount());
        assertEquals(3, node.getChildren().get(1).sampleCount());
    }

    @Test
    public void testDepth() {
        DataSet data = new DataSet(new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}}), DISCRETE);

        Children children = new Children(node);
        node.setChildren(children);
        Node n = new Node(data, node);
        n.setAttribute(0);
        children.put(1, n);

        children = new Children(node.getChildren().get(0));
        node.getChildren().get(0).setChildren(children);
        n = new Node(data, node);
        n.setAttribute(0);
        children.put(1, n);

        assertEquals(2, node.depth());
    }
}
