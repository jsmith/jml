package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;

public class Math {
    private static final Logger LOG = LoggerFactory.getLogger(Math.class);

    public static Map<Double, Integer> calculateOccurrences(Vector v) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for (double value : v) {
            LOG.debug("adding {} to map", value);
            Integer count = map.get(value);
            updateMap(map, value, count);
        }
        return map;
    }

    public static Map<Integer, Integer> calculateOccurrences(int[] values) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int value : values) {
            LOG.debug("adding {} to map", value);
            Integer count = map.get(value);
            updateMap(map, value, count);
        }
        return map;
    }

    private static void updateMap(Map map, Object value, Integer count) {
        if (count == null) {
            count = 1;
            map.put(value, count);
        } else {
            count++;
            map.replace(value, count);
        }
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
}
