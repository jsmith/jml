package ca.jacob.cs6735.test;

import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.Node;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class NodeTest {
    private static final Logger LOG = LoggerFactory.getLogger(NodeTest.class);
    private static final double DELTA =  1e-10;
    private Node node;

    @Before
    public void setup() {
        int[][] data = new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}};
        node = new Node(data, 1, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
    }

    @Test
    public void testEntropy() {
        assertEquals(1, node.entropy(), DELTA);
    }

    @Test
    public void testSplit() {
        node.split();
        assertEquals(2, node.getChildren().size());
        assertEquals(0, node.getChildren().get(0).getChildren().size());
        assertEquals(0, node.getChildren().get(1).getChildren().size());
    }

    @Test
    public void testDepth() {
        int[][] data = new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}};

        Map<Integer, Node> children = new HashMap<Integer, Node>();
        children.put(1, new Node(data, 1, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE));
        node.setChildren(children);

        children = new HashMap<Integer, Node>();
        children.put(1, new Node(data, 1, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE));
        node.getChildren().get(1).setChildren(children);

        assertEquals(3, node.depth());
    }

}
