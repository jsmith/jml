package ca.jacob.jml.bayes;

import ca.jacob.jml.math.distribution.Distribution;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContinuousAttribute implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(ContinuousAttribute.class);

    private Vector values;
    private Distribution distribution;
    private double mean;
    private double stdev;

    public ContinuousAttribute(Vector values, Distribution distribution) {
        this.values = values;
        this.distribution = distribution;
        this.mean = this.values.mean();
        this.stdev = this.values.stdev();
        LOG.debug("mean -> {}; stdev -> {}", mean, stdev);
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
