package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.error;
import static ca.jacob.cs6735.util.ML.generateIndices;
import static ca.jacob.cs6735.util.Math.exp;
import static ca.jacob.cs6735.util.Math.ln;
import static java.lang.Math.sqrt;

public class MultiAdaboost implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(MultiAdaboost.class);

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
