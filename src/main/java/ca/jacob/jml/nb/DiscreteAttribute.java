package ca.jacob.jml.nb;

import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscreteAttribute implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(DiscreteAttribute.class);

    private Vector values;

    public DiscreteAttribute(Vector values) {
        this.values = values;
    }

    @Override
    public double probability(double value) {
        LOG.debug("{} occurred {} times in {}", value, values.count(value), values);
        return ((double)values.count(value)) / values.length();
    }
}
