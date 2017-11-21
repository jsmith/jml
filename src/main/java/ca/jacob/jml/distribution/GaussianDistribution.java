package ca.jacob.jml.distribution;

import ca.jacob.jml.math.MathException;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GaussianDistribution implements Distribution {
    private static final Logger LOG = LoggerFactory.getLogger(GaussianDistribution.class);

    public double probability(double x, double mean, double stdev) {
        double exponent = java.lang.Math.exp(-pow(x-mean, 2)/(2*pow(stdev, 2)));
        return (1 / (sqrt(2*PI) * stdev)) * exponent;
    }

    public Vector probability(Vector x, Vector means, Vector stdevs) {
        if(x.length() != means.length() || x.length() != stdevs.length()) {
            LOG.warn("They aren't the same length! x length: {}; means length: {}; stdevs length: {}", x.length(), means.length(), stdevs.length());
            throw new MathException("lengths must match!");
        }
        Vector probabilities = new Vector(new double[x.length()]);
        for(int i = 0; i < means.length(); i++) {
            probabilities.set(i, probability(x.at(i), means.at(i), stdevs.at(i)));
        }
        return probabilities;
    }
}
