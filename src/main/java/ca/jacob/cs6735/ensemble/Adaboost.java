package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.dt.ID3Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.*;
import static ca.jacob.cs6735.util.Math.*;
import static java.lang.Math.sqrt;


public class Adaboost implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(Adaboost.class);

    private Algorithm algorithm;
    private int numberOfEstimators;
    private double percentageOfSamples;

    public Adaboost(Algorithm algorithm, int numberOfEstimators, double percentageOfSamples) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
        this.percentageOfSamples = percentageOfSamples;
    }

    public Model fit(Matrix x, Vector y) {
        int numberOfSamples = (int)(x.rowCount() * percentageOfSamples);
        LOG.debug("number of samples for each training iteration: {}", numberOfSamples);

        AdaboostModel model = new AdaboostModel();

        Vector weights = new Vector(new double[x.rowCount()]);
        weights.fill(1./x.rowCount());
        LOG.debug("weights 0 to 5 -> {}", weights.subVector(0, 5));

        for(int i = 0; i < numberOfEstimators; i++) {
            LOG.info("starting iteration {}", i+1);

            Vector indices = generateIndices(weights, numberOfSamples);
            Matrix xWeighted = x.rows(indices);
            Vector yWeighted = y.at(indices);
            LOG.debug("first row: {} -> {}", xWeighted.row(0), yWeighted.at(0));

            Model m = algorithm.fit(xWeighted, yWeighted);
            LOG.debug("accuracy of model: {}", m.accuracy(xWeighted, yWeighted));

            Vector h = m.predict(x);

            Vector err = error(h, y);
            LOG.debug("error 0 to 5 -> {}", err.subVector(0, 5));

            double epsilon = weights.dot(err)/weights.length();
            LOG.debug("epsilon: {}", epsilon);

            double alpha = 0.5*ln((1-epsilon)/epsilon);
            LOG.debug("alpha: {}", alpha);

            // updating weights
            weights = weights.mul(exp(err.mul(alpha))); //updating weights
            weights = weights.div(weights.sum());
            LOG.debug("weights 0 to 5 -> {}", weights.subVector(0, 5));

            model.add(m, alpha);
        }

        return model;
    }
}
