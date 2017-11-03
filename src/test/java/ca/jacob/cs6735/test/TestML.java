package ca.jacob.cs6735.test;

import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.generateIndices;
import static ca.jacob.cs6735.util.Math.ln;
import static org.junit.Assert.assertEquals;

public class TestML {
    private static final Logger LOG = LoggerFactory.getLogger(TestML.class);
    private static final double DELTA = 1e-5;

    @Test
    public void testIndices() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndices(v);
        LOG.info("indices: {}", indices);
    }

    @Test
    public void testLn() {
        assertEquals((double) 0, ln(1), DELTA);
        assertEquals((double)1.609437912, ln(5), DELTA);
    }
}
