package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.distribution.Distribution;
import ca.jacob.cs6735.util.Vector;

public class ContinuousAttribute implements Attribute {
    private Vector values;
    private Distribution distribution;
    private double mean;
    private double stdev;

    public ContinuousAttribute(Vector values, Distribution distribution, double mean, double stdev) {
        this.values = values;
        this.distribution = distribution;
        this.mean = mean;
        this.stdev = stdev;
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
