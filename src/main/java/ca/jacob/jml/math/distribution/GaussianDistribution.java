package ca.jacob.jml.math.distribution;

import ca.jacob.jml.exceptions.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GaussianDistribution implements Distribution {
    private static final Logger LOG = LoggerFactory.getLogger(GaussianDistribution.class);

    public double probability(double x, double mean, double stdev) {
        if(pow(stdev, 2) == 0) {
            return 0;
        }

        double exponent = java.lang.Math.exp(-pow(x-mean, 2)/(2*pow(stdev, 2)));
        double probability = (1 / (sqrt(2*PI) * stdev)) * exponent;

        if(Double.isNaN(probability)) {
            LOG.error("x: {}, mean: {}, stdev: {}", x, mean, stdev);
            throw new DataException("something went wrong");
        }
        return probability;
    }
}
