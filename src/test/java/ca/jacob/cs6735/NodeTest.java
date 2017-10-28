package ca.jacob.cs6735;

import ca.jacob.cs6735.algorithms.dt.Node;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NodeTest {
    private static final double DELTA =  1e-10;
    private Node node;

    @Before
    public void setup() {
        Integer[][] data = new Integer[][]{{0, 1, 1, 1}, {0, 0, 0, 0}};
        node = new Node(data, 1);
    }

    @Test
    public void testEntropy() {
        assertEquals(1, node.entropy(), DELTA);
    }

    @Test
    public void testSplit() {
        node.split();
        assertEquals(2, node.getNodes().size());
        assertEquals(0, node.getNodes().get(0).getNodes().size());
        assertEquals(0, node.getNodes().get(1).getNodes().size());
    }
}
