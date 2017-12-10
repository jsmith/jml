package ca.jacob.jml.ensemble;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Dataset;
import ca.jacob.jml.Model;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.Util.*;
import static ca.jacob.jml.math.Util.*;
import static java.lang.Math.log;


public class AdaBoost implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(AdaBoost.class);
    private static final String NAME = "AdaBoost";
    private static final double EPSILON = 0.001;

    private Algorithm algorithm;
    private int numberOfEstimators;
    private double proportionOfSamples;

    public AdaBoost(Algorithm algorithm, int numberOfEstimators, double proportionOfSamples) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
        this.proportionOfSamples = proportionOfSamples;
    }

    public Model fit(Dataset dataset) {
        int numberOfSamples = (int)(dataset.sampleCount() * proportionOfSamples);
        LOG.debug("number of samples for each training iteration: {}", numberOfSamples);

        AdaBoostModel model = new AdaBoostModel(dataset.classes());

        Vector weights = new Vector(new double[dataset.sampleCount()]);
        weights.fill(1./ dataset.sampleCount());

        if(weights.length() == 0) {
            throw new DataException("length of weights cannot be 0");
        }

        Vector classes = dataset.classes();
        int classCount = classes.unique().length();
        LOG.debug("there are {} unique classes", classCount);

        for(int i = 0; i < numberOfEstimators; i++) {
            LOG.debug("starting iteration {}", i+1);

            Vector indices = generateIndices(weights, numberOfSamples);
            Dataset weightedDataset = dataset.samples(indices);
            Model m = algorithm.fit(weightedDataset);

            Vector predictions = m.predict(dataset.getX());
            Vector err = error(predictions, classes); // 1 if wrong, else 0
            double error = weights.dot(err)/weights.sum();
            LOG.debug("error: {}", error);

            double alpha = log((1-error)/(error)) + log(classCount-1);
            LOG.debug("alpha: {}", alpha);

            if(Double.isNaN(alpha)) {
                alpha = log((1-error+EPSILON)/(error+EPSILON)) + log(classCount-1);
                LOG.warn("alpha is NaN");
            }

            weights = weights.mul(exp(err.mul(alpha))); //updating weights
            weights = weights.div(weights.sum()); // normalize weights

            model.add(m, alpha);
        }

        return model;
    }



    @Override
    public String toString() {
        return NAME + "(estimators:"+numberOfEstimators+", proportion:"+proportionOfSamples+") with " + algorithm.toString();
    }
}
