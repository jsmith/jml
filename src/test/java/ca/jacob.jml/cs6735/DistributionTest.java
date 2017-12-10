package ca.jacob.jml.cs6735;

import ca.jacob.jml.math.distribution.Gaussian;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class DistributionTest {
    private static final Logger LOG = LoggerFactory.getLogger(DistributionTest.class);

    @Test
    public void testGaussianDistribution() {
        Gaussian distribution = new Gaussian();
        assertEquals(0, distribution.probability(0, 0, 0), 1e-10);
    }
}
