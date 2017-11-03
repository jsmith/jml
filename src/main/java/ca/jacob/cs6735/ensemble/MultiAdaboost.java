package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

public class MultiAdaboost implements Algorithm {
    private Algorithm algorithm;
    private int numberOfEstimators;

    public MultiAdaboost(Algorithm algorithm, int numberOfEstimators) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
    }

    @Override
    public Model fit(Matrix x, Vector y) {
        return null;
    }
}
