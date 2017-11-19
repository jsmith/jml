package ca.jacob.cs6735.test;

import ca.jacob.jml.dt.Children;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.dt.Node;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.DISCRETE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class NodeTest {
    private static final Logger LOG = LoggerFactory.getLogger(NodeTest.class);
    private static final double DELTA =  1e-10;
    private Node node;

    @Before
    public void setup() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        Vector attributeTypes = new Vector(new int[]{DISCRETE, DISCRETE, DISCRETE});
        node = new Node(data, attributeTypes,1, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
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
    public void testDepth() {
        DataSet data = new DataSet(new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}}), DISCRETE);

        Children children = new Children(node);
        Node n = new Node(data, node);
        n.setAttribute(0);
        children.put(1, n);
        node.setChildren(children);

        children = new Children(node.getChildren().get(0));
        n = new Node(data, node);
        n.setAttribute(0);
        children.put(1, n);
        node.getChildren().get(0).setChildren(children);

        assertEquals(3, node.depth());
    }

}
