package ca.jacob.jml.ensemble;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.util.ML.*;
import static ca.jacob.jml.util.Math.*;


public class Adaboost implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(Adaboost.class);
    private static final String NAME = "Adaboost";

    private Algorithm algorithm;
    private int numberOfEstimators;
    private double percentageOfSamples;

    public Adaboost(Algorithm algorithm, int numberOfEstimators, double percentageOfSamples) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
        this.percentageOfSamples = percentageOfSamples;
    }

    public Model fit(DataSet dataSet) {
        int numberOfSamples = (int)(dataSet.sampleCount() * percentageOfSamples);
        LOG.debug("number of samples for each training iteration: {}", numberOfSamples);

        AdaboostModel model = new AdaboostModel();

        Vector weights = new Vector(new double[dataSet.sampleCount()]);
        weights.fill(1./ dataSet.sampleCount());
        LOG.debug("weights 0 to 5 -> {}", weights.subVector(0, 5));

        for(int i = 0; i < numberOfEstimators; i++) {
            LOG.info("starting iteration {}", i+1);

            Vector indices = generateIndices(weights, numberOfSamples);
            DataSet weightedDataSet = dataSet.samples(indices);
            LOG.debug("first row: {} -> {}", weightedDataSet.sample(0));

            Model m = algorithm.fit(weightedDataSet);
            //LOG.debug("accuracy of model: {}", m.accuracy(weightedDataSet));

            Vector h = m.predict(dataSet.getX());

            Vector err = error(h, dataSet.getY());
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

    @Override
    public String toString() {
        return NAME + "(estimators:"+numberOfEstimators+", percentage:"+percentageOfSamples+") with " + algorithm.toString();
    }
}
