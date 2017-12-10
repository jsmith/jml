package ca.jacob.jml.bayes;

import ca.jacob.jml.math.distribution.Distribution;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Continuous implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(Continuous.class);

    private Distribution distribution;
    private double mean;
    private double stdev;

    public Continuous(Vector values, Distribution distribution) {
        this.distribution = distribution;
        this.mean = values.mean();
        this.stdev = values.stdev();
        LOG.debug("accuracy -> {}; stdev -> {}", mean, stdev);
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
