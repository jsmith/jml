package ca.jacob.jml.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);


    public static double log2(double value) {
        return log(value) / log(2);
    }

    public static double ln(double value) {
        return log(value) / log(E);
    }

    public static Vector exp(Vector v) {
        double[] data = new double[v.length()];
        for(int i = 0; i < data.length; i++) {
            data[i] = java.lang.Math.exp(v.at(i));
        }
        return new Vector(data);
    }
}
