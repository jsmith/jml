package ca.jacob.cs6735.test;

import ca.jacob.cs6735.util.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.generateIndices;

public class TestML {
    private static final Logger LOG = LoggerFactory.getLogger(TestML.class);

    @Test
    public void testIndices() {
        Vector v = new Vector(new double[10]);
        v.fill(1./10);
        Vector indices = generateIndices(v);
        LOG.info("indices: {}", indices);
    }
}
