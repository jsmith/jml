package ca.jacob.jml.bayes;

import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscreteAttribute implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(DiscreteAttribute.class);

    private Vector values;
    private int classCount;

    public DiscreteAttribute(Vector values, int classCount) {
        this.values = values;
        this.classCount = classCount;
    }

    @Override
    public double probability(double value) {
        LOG.debug("{} occurred {} times in {}", value, values.count(value), values);
        return (((double)values.count(value))+1) / (values.length()+classCount);
    }
}
