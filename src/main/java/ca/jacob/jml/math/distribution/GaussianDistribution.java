package ca.jacob.jml.math.distribution;

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
}
