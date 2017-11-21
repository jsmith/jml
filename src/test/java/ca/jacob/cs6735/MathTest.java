package ca.jacob.cs6735;

import ca.jacob.jml.distribution.GaussianDistribution;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.math.Vector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.DataUtil.loadEcoliData;
import static org.junit.Assert.assertEquals;

public class MathTest {
    private static final Logger LOG = LoggerFactory.getLogger(MathTest.class);

    private static final double DELTA = 1e-5;

    @Test
    public void testStdDevAndMean() {
        Vector v = new Vector(new int[]{1, 2, 3, 4, 5});
        double stdev = v.stdev();
        double mean = v.mean();
        assertEquals(1.58113883008, stdev, DELTA);
        assertEquals(3., mean, DELTA);
    }

    @Test
    public void testStdDevAndMeanWithData() throws Throwable {
        DataSet data = loadEcoliData(RandomForestTest.class);
        Vector v = data.getX().col(0);
        LOG.debug("v -> {}", v.subVector(0, 5));
        double stdev = v.stdev();
        double mean = v.mean();
        assertEquals(0.1946339757237703, stdev, DELTA);
        assertEquals(0.5000595238095235, mean, DELTA);
    }

    @Test
    public void testProbability() {
        double probability = new GaussianDistribution().probability(71.5, 73, 6.2);
        assertEquals(0.0624896575937, probability, DELTA);
    }
}
