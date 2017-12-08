package ca.jacob.jml.ensemble;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.Util.*;
import static ca.jacob.jml.math.Util.*;


public class AdaBoost implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(AdaBoost.class);
    private static final String NAME = "AdaBoost";
    private static final double EPSILON = 0.0001;

    private Algorithm algorithm;
    private int numberOfEstimators;
    private double proportionOfSamples;

    public AdaBoost(Algorithm algorithm, int numberOfEstimators, double proportionOfSamples) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
        this.proportionOfSamples = proportionOfSamples;
    }

    public Model fit(DataSet dataSet) {
        /*Vector classes = dataSet.getY();
        for(int i = 0; i < classes.length(); i++) {
            if(classes.intAt(i) != -1 && classes.intAt(i) != 1) {
                throw new DataException("all classes must be either 0 or 1");
            }
        }*/

        int numberOfSamples = (int)(dataSet.sampleCount() * proportionOfSamples);
        LOG.debug("number of samples for each training iteration: {}", numberOfSamples);

        AdaBoostModel model = new AdaBoostModel();

        Vector weights = new Vector(new double[dataSet.sampleCount()]);
        weights.fill(1./ dataSet.sampleCount());

        if(weights.length() == 0) {
            throw new DataException("length of weights cannot be 0");
        }

        for(int i = 0; i < numberOfEstimators; i++) {
            LOG.info("starting iteration {}", i+1);

            Vector indices = generateIndices(weights, numberOfSamples);
            DataSet weightedDataSet = dataSet.samples(indices);
            LOG.debug("first row: {} -> {}", weightedDataSet.sample(0));


            Model m = algorithm.fit(weightedDataSet);
            //LOG.debug("accuracy of model: {}", m.accuracy(weightedDataSet));

            Vector h = m.predict(dataSet.getX());

            Vector err = error(h, dataSet.getY());

            double error = weights.dot(err)/weights.length();
            LOG.debug("error: {}", error);

            double alpha = 0.5*ln((1-error+EPSILON)/(error+EPSILON));
            LOG.debug("alpha: {}", alpha);

            if(Double.isNaN(alpha)) {
                throw new DataException("alpha cannot be NaN");
            }

            if(alpha < 0.5) {
                throw new DataException("alpha is less than 0.5 -> " + alpha);
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
