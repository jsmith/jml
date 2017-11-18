package ca.jacob.jml.distribution;

public interface Distribution {
    public double probability(double x, double mean, double stdev);
}
