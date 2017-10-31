package ca.jacob.cs6735;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KFold {
    private Integer numberOfSplits;

    public KFold(Integer numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
    }

    public Map<Integer[][], Integer[]> split(Integer[][] x, Integer[] y) {
        Map<Integer[][], Integer[]> data = new HashMap<Integer[][], Integer[]>();
        shuffle(x, y);
        Integer numberOfSamples = x.length;
        Integer splitLength = numberOfSamples / numberOfSplits;
        for(int split = 0; split < numberOfSplits-1; split++) {
            Integer from = split*splitLength;
            Integer to = (split+1)*splitLength;
            data.put(Arrays.copyOfRange(x, from, to), Arrays.copyOfRange(y, from, to));
        }
        Integer from = (numberOfSplits-1)*splitLength;
        Integer to = numberOfSamples;
        data.put(Arrays.copyOfRange(x, from, to), Arrays.copyOfRange(y, from, to));
        return data;
    }

    public void shuffle(Integer[][] x, Integer[] y) {
        Random random = new Random();
        for (int i = x.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            Integer[] tmp = x[index];
            x[index] = x[i];
            x[i] = tmp;

            Integer temp = y[index];
            y[index] = y[i];
            y[i] = temp;
        }
    }
}
