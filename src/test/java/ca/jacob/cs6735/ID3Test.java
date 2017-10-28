package ca.jacob.cs6735;

import ca.jacob.cs6735.algorithms.dt.ID3;
import ca.jacob.cs6735.algorithms.dt.Node;
import ca.jacob.cs6735.utils.Matrix;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ID3Test {
    private ID3 id3;

    @Before
    public void init() {
        id3 = new ID3(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0});
    }

    @Test
    public void testTrain() {
        id3.train();

        Node root = id3.getRoot();
        assertEquals(1, root.getPredictor());
        assertEquals(2, root.getNodes().size());
        assertEquals(false, root.isLeaf());

        Matrix nodeZeroData = root.getNodes().get(0).getData();
        Matrix nodeOneData = root.getNodes().get(1).getData();
        assertEquals(1, nodeOneData.rowCount());
        assertEquals(2, nodeZeroData.rowCount());
    }

    @Test
    public void testPredict() {
        id3.train();
        assertEquals(1, id3.predict(new Integer[]{1, 1, 1}));
        assertEquals(0, id3.predict(new Integer[]{1, 0, 1}));
        assertEquals(1, id3.predict(new Integer[]{0, 1, 1}));
    }
}
