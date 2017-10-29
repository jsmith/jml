package ca.jacob.cs6735.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Math {
    private static final Logger LOG = LoggerFactory.getLogger(Math.class);

    public static Map<Double, Integer> calculateOccurrences(Double[] values) {
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

    public static Integer[] error(Integer[] one, Integer[] two) {
        Integer[] error = new Integer[one.length];
        for(Integer i = 0; i < one.length; i++) {
            if(one[i] == two[i]) {
                error[i] = 1;
            } else {
                error[i] = 0;
            }
        }
        return error;
    }

    public static Double log2(Double value) {
        return java.lang.Math.log(value) / java.lang.Math.log(2);
    }

    public static Double ln(Double value) {
        return java.lang.Math.log(value) / java.lang.Math.log(java.lang.Math.E);
    }

    public static Vector exp(Vector v) {
        Double[] data = new Double[v.length()];
        for(Integer i = 0; i < data.length; i++) {
            data[i] = java.lang.Math.exp(v.at(i));
        }
        return new Vector(data);
    }
}
