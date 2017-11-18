package ca.jacob.cs6735.test;

import ca.jacob.jml.distribution.GaussianDistribution;
import ca.jacob.jml.nb.ContinuousAttribute;
import ca.jacob.jml.nb.DiscreteAttribute;
import ca.jacob.jml.util.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeTest {
    private static final double DELTA = 1e-5;

    @Test
    public void testDiscreateAttribute() {
        DiscreteAttribute attribute = new DiscreteAttribute(0, new Vector(new int[]{1, 1, 0, 0, 1, 0}));
        assertEquals(0.5, attribute.probability(1), DELTA);
    }

    @Test
    public void testContinuousAttribute() {
        ContinuousAttribute attribute = new ContinuousAttribute(0, new Vector(new int[]{1, 1, 0, 0, 1, 0}), new GaussianDistribution());
        assertEquals(0.5, attribute.getMean(), DELTA);
        assertEquals(0.54772, attribute.getStdev(), DELTA);
        assertEquals(0.48016821060, attribute.probability(1), DELTA);
    }
}
