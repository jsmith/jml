package ca.jacob.cs6735;

import ca.jacob.cs6735.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ca.jacob.cs6735.util.ML.shuffle;

public class KFold {
    private static final Logger LOG = LoggerFactory.getLogger(KFold.class);

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
            LOG.debug("split {}, from {} to {}", split, from, to);
            data.put(Arrays.copyOfRange(x, from, to), Arrays.copyOfRange(y, from, to));
        }
        Integer from = (numberOfSplits-1)*splitLength;
        Integer to = numberOfSamples;
        LOG.debug("last split from {} to {}", from, to);
        data.put(Arrays.copyOfRange(x, from, to), Arrays.copyOfRange(y, from, to));
        return data;
    }
}
