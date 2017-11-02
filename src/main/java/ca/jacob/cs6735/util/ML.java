package ca.jacob.cs6735.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Random;

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

    public static Vector generateIndices(Vector weights) {
        Vector indices = new Vector(new Integer[weights.length()]);
        for(Integer i = 0; i < weights.length(); i++) {
            Double rand = random();
            Double cumulativeProbability = 0.0;
            for (Integer j = 0; j < weights.length(); j++) {
                cumulativeProbability += weights.at(j);
                if (rand <= cumulativeProbability) {
                    indices.set(i, j);
                }
            }
        }
        return indices;
    }

    public static Vector range(Integer from, Integer to) {
        Integer[] range = new Integer[to-from];
        for(int i = 0; i < to-from; i++) {
            range[i] = from+i;
        }
        return new Vector(range);
    }

    public static void shuffle(Vector v) {
        Random random = new Random();

        for (int i = v.length() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            v.swap(i, index);
        }
    }

    public static void shuffle(Vector v, Long seed) {
        if(seed == null) {
            shuffle(v);
            return;
        }

        Random random = new Random(seed);
        for (int i = v.length() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            v.swap(i, index);
        }
    }

    public static Vector error(Vector one, Vector two) {
        Vector error = new Vector(new Double[one.length()]);
        for(Integer i = 0; i < one.length(); i++) {
            if(!one.at(i).equals(two.at(i))) {
                error.set(i, 1.);
            } else {
                error.set(i, 0.);
            }
        }
        return error;
    }

}
