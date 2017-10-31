package ca.jacob.cs6735.util;

import java.util.ArrayList;

import static java.lang.Math.random;

public class ML {
    public static String[][] removeSamplesWith(String key, String[][] data) {
        ArrayList<String[]> modified = new ArrayList<String[]>();
        for(int i = 0; i < data.length; i++) {
            boolean containsKey = false;
            for(int j = 0; j < data[0].length; j++) {
                if(data[i][j].contains(key)) {
                   containsKey = true;
                }
            }
            if(!containsKey) {
                modified.add(data[i]);
            }
        }

        return modified.toArray(new String[modified.get(0).length][modified.get(0).length]);
    }

    public static Integer[][] copy(Integer[][] original) {
        Integer [][] copy = new Integer[original.length][];
        for(int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static Integer[] copy(Integer[] original) {
        return original.clone();
    }

    public static Integer sign(Double n) {
        if(n >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static Integer[] generateIndices(Vector weights) {
        Integer[] indices = new Integer[weights.length()];
        for(Integer i = 0; i < weights.length(); i++) {
            Double rand = random();
            Double cumulativeProbability = 0.0;
            for (Integer j = 0; j < weights.length(); j++) {
                cumulativeProbability += weights.at(j);
                if (rand <= cumulativeProbability) {
                    indices[i] = j;
                }
            }
        }
        return indices;
    }

    public static Integer[] error(Integer[] one, Integer[] two) {
        Integer[] error = new Integer[one.length];
        for(Integer i = 0; i < one.length; i++) {
            if(!one[i].equals(two[i])) {
                error[i] = 1;
            } else {
                error[i] = 0;
            }
        }
        return error;
    }
}
