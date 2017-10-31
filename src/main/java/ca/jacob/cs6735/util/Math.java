package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.E;
import static java.lang.Math.log;
import static java.lang.Math.random;

public class Math {
    private static final Logger LOG = LoggerFactory.getLogger(Math.class);

    public static Map<Double, Integer> calculateOccurrences(List<Double> values) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for (Double value : values) {
            LOG.debug("adding {} to map", value);
            Integer count = map.get(value);
            updateMap(map, value, count);
        }
        return map;
    }

    public static Map<Integer, Integer> calculateOccurrences(Integer[] values) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Integer value : values) {
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

    public static Double log2(Double value) {
        return log(value) / log(2);
    }

    public static Double ln(Double value) {
        return log(value) / log(E);
    }

    public static Vector exp(Vector v) {
        Double[] data = new Double[v.length()];
        for(Integer i = 0; i < data.length; i++) {
            data[i] = java.lang.Math.exp(v.at(i));
        }
        return new Vector(data);
    }
}
