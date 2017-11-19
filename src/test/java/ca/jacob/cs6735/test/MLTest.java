package ca.jacob.cs6735.test;

import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.dt.Node;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.jml.util.DataSet.DISCRETE;
import static ca.jacob.jml.util.ML.generateIndices;
import static ca.jacob.jml.util.ML.generateIndicesWithoutReplacement;
import static ca.jacob.jml.util.Math.calculateOccurrences;
import static ca.jacob.jml.util.Math.ln;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MLTest {
    private static final Logger LOG = LoggerFactory.getLogger(MLTest.class);
    private static final double DELTA = 1e-5;

    @Test
    public void testIndices() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndices(v, v.length()-1);
        assertEquals(v.length()-1, indices.length());
        LOG.info("indices: {}", indices);
    }

    @Test
    public void testGenerateIndicesWithoutReplacement() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndicesWithoutReplacement(v, 5);
        assertEquals(5, indices.length());
        Vector unique = new Vector();
        for(int i = 0; i < indices.length(); i++) {
            int num = indices.intAt(i);
            assertTrue(!unique.contains(num));
            unique.add(num);
        }
        LOG.info("indices: {}", indices);
    }

    @Test
    public void testIndicesDistribution() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndices(v, 1000);
        Map<Integer, Integer> occurrences = calculateOccurrences(indices);
        LOG.info("occurrences: {}", occurrences);
    }

    @Test
    public void testIndicesDistributionFromTo() {
        Vector indices = generateIndices(0, 10, 1000);
        Map<Integer, Integer> occurrences = calculateOccurrences(indices);
        LOG.info("occurrences: {}", occurrences);
    }

    @Test
    public void testLn() {
        assertEquals((double) 0, ln(1), DELTA);
        assertEquals((double)1.609437912, ln(5), DELTA);
    }

    @Test
    public void testEntropy() {
        Matrix data = new Matrix(new int[][]{{0, 1, 1, 1}, {0, 0, 0, 0}});
        Vector attributeTypes = new Vector(new int[]{DISCRETE, DISCRETE, DISCRETE});
        Node node = new Node(data, attributeTypes,1, ID3.MAX_LEVEL_NONE, ID3.MIN_SAMPLES_NONE);
        Assert.assertEquals(1, node.entropy(), DELTA);
    }
}
