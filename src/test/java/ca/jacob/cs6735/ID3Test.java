package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.dt.ID3Model;
import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.utils.Matrix;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ID3Test {
    private ID3 id3;

    @Before
    public void init() {
        id3 = new ID3(ID3.LEVEL_NONE);
    }

    @Test
    public void testTrain() {
        ID3Model model = (ID3Model) id3.train(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0}, new Double[]{1., 1., 1.});

        Node root = model.getRoot();
        assertEquals((Integer)1, root.getPredictor());
        assertEquals(2, root.getNodes().size());
        assertEquals(false, root.isLeaf());

        Matrix nodeZeroData = root.getNodes().get(0).getData();
        Matrix nodeOneData = root.getNodes().get(1).getData();
        assertEquals((Integer)1, nodeOneData.rowCount());
        assertEquals((Integer)2, nodeZeroData.rowCount());
    }

    @Test
    public void testPredict() {
        Model model = id3.train(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0}, new Double[]{1., 1., 1.});
        assertEquals((Integer) 1, model.predict(new Integer[]{1, 1, 1}));
        assertEquals((Integer)0, model.predict(new Integer[]{1, 0, 1}));
        assertEquals((Integer)1, model.predict(new Integer[]{0, 1, 1}));
    }

    @Test
    public void testMaxLevel() {
        id3.setMaxLevel(1);
        ID3Model model = (ID3Model)id3.train(new Integer[][]{{1, 1, 1},{1,0, 1},{1,0, 0}}, new Integer[]{1, 0, 0}, new Double[]{1., 1., 1.});

        Node root = model.getRoot();
        assertEquals(0, root.getNodes().size());
        assertEquals((Integer)0, model.predict(new Integer[]{1, 1, 1}));
    }
}
