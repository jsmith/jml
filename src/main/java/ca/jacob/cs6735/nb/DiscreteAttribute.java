package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.distribution.Distribution;
import ca.jacob.cs6735.util.Vector;
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
