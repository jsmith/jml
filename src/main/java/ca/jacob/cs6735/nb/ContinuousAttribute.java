package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.distribution.Distribution;

public class ContinuousAttribute implements Attribute {
    private Distribution distribution;
    private double mean;
    private double stdev;

    public ContinuousAttribute(Distribution distribution, double mean, double stdev) {
        this.distribution = distribution;
        this.mean = mean;
        this.stdev = stdev;
    }

    @Override
    public double probability(double value) {
        return distribution.probability(value, mean, stdev);
    }
}
