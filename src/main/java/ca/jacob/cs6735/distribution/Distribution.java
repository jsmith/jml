package ca.jacob.cs6735.distribution;

import ca.jacob.cs6735.nb.ClassSummary;
import ca.jacob.cs6735.util.Matrix;

import java.util.List;
import java.util.Map;

public interface Distribution {
    public double probability(double x, double mean, double stdev);
}
