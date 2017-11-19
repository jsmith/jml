package ca.jacob.cs6735.test;

import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static ca.jacob.jml.util.Math.calculateOccurrences;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MatrixTest {
    private ca.jacob.jml.util.Matrix matrix;

    @Before
    public void setup() {
        matrix = new Matrix(new int[][]{{1, 2, 3}, {1, 2, 3}});
    }

    @Test
    public void testRow() {
        Vector v = new Vector(new double[]{1., 2., 3.});
        assertEquals(v, matrix.row(0));
    }

    @Test
    public void testRows() {
        Matrix m = matrix.rows(new Vector(new int[]{0}));
        assertEquals(new Matrix(new int[][]{{1, 2, 3}}), m);
    }

    @Test
    public void testCol() {
        Vector v = new Vector(new double[]{2., 2.});
        assertEquals(v, matrix.col(1));
    }

    @Test
    public void testCalculateOccurrences() {
        Vector v = new Vector(new double[]{1., 2., 3., 1., 1.});
        Map<Integer, Integer> expected = new HashMap<Integer, Integer>();
        expected.put(1, 3);
        expected.put(2, 1);
        expected.put(3, 1);
        assertEquals(expected, calculateOccurrences(v));
    }

    @Test
    public void testReplaceCol() {
        matrix.setCol(matrix.colCount()-1, new Vector(new int[]{1, 1}));
        assertEquals(new Matrix(new int[][]{{1, 2, 1}, {1, 2, 1}}), matrix);
    }
}
