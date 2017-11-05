package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.distribution.Distribution;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContinuousAttribute implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(ContinuousAttribute.class);

    private Vector values;
    private int attribute;
    private Distribution distribution;
    private double mean;
    private double stdev;

    public ContinuousAttribute(int attribute, Vector values, Distribution distribution) {
        this.values = values;
        this.distribution = distribution;
        this.mean = this.values.mean();
        this.stdev = this.values.stdev();
        this.attribute = attribute;
        LOG.debug("attribute {}: mean -> {}; stdev -> {}", this.attribute, mean, stdev);
    }

    @Override
    public double probability(double value) {
        return distribution.probability(value, mean, stdev);
    }

    public double getMean() {
        return mean;
    }

    public double getStdev() {
        return stdev;
    }
}
