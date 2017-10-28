package ca.jacob.cs6735;

import ca.jacob.cs6735.algorithms.dt.Node;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class NodeTest {
    private static final double DELTA =  1e-10;
    private Node node;

    @Before
    public void setup() {
        Integer[][] x = new Integer[][]{{0, 1, 1}, {0, 0, 0}};
        Integer[] y = new Integer[]{1, 0};
        node = new Node(x, y);
    }

    @Test
    public void testEntropy() {
        assertEquals(1, node.entropy(), DELTA);
    }
}
