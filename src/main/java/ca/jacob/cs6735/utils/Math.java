package ca.jacob.cs6735.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Math {
    private static final Logger LOG = LoggerFactory.getLogger(Math.class);

    public static Map<Integer, Integer> calculateOccurrences(Integer[] values) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Integer value : values) {
            LOG.debug("adding {} to map", value);
            Integer count = map.get(value);
            if (count == null) {
                count = 1;
                map.put(value, count);
            } else {
                count++;
                map.replace(value, count);
            }
        }
        return map;
    }

    public static Double log2(double value) {
        return java.lang.Math.log(value) / java.lang.Math.log(2);
    }
}
