package ca.jacob.jml.math.distribution;

public interface Distribution {
    public double probability(double x, double mean, double stdev);
}
