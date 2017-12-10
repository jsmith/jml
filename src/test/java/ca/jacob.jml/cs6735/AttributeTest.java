package ca.jacob.jml.cs6735;

import ca.jacob.jml.math.distribution.Gaussian;
import ca.jacob.jml.bayes.Continuous;
import ca.jacob.jml.bayes.Discrete;
import ca.jacob.jml.math.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeTest {
    private static final double DELTA = 1e-5;

    @Test
    public void testDiscreateAttribute() {
        Discrete attribute = new Discrete(new Vector(new int[]{1, 1, 0, 0, 1, 0}), 2);
        assertEquals(0.5, attribute.probability(1), DELTA);
    }

    @Test
    public void testContinuousAttribute() {
        Continuous attribute = new Continuous(new Vector(new int[]{1, 1, 0, 0, 1, 0}), new Gaussian());
        assertEquals(0.5, attribute.getMean(), DELTA);
        assertEquals(0.54772, attribute.getStdev(), DELTA);
        assertEquals(0.48016821060, attribute.probability(1), DELTA);
    }
}
