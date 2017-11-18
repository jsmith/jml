package ca.jacob.jml.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class Math {
    private static final Logger LOG = LoggerFactory.getLogger(Math.class);

    public static Map<Integer, Integer> calculateOccurrences(Vector v) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < v.length(); i++) {
            LOG.debug("adding {} to map", v.intAt(i));
            Integer count = map.get(v.intAt(i));
            if (count == null) {
                count = 1;
                map.put(v.intAt(i), count);
            } else {
                count++;
                map.replace(v.intAt(i), count);
            }
        }
        return map;
    }

    public static Map<Integer, Integer> calculateOccurrences(int[] values) {
        return calculateOccurrences(new Vector(values));
    }


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

    public static Vector abs(Vector vector) {
        Vector v = new Vector(new double[vector.length()]);
        for(int i = 0; i < vector.length(); i++) {
            v.set(i, java.lang.Math.abs(vector.at(i)));
        }
        return v;
    }
}
