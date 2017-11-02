package ca.jacob.cs6735;

import ca.jacob.cs6735.util.Matrix;

import java.util.*;

import static ca.jacob.cs6735.util.ML.range;
import static ca.jacob.cs6735.util.ML.shuffle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.shuffle;

public class KFold {
    private static final Logger LOG = LoggerFactory.getLogger(KFold.class);

    private Integer numberOfSplits;
    private Long seed;

    public KFold(Integer numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
    }

    public KFold(Integer numberOfSplits, Long seed) {
        this.numberOfSplits = numberOfSplits;
        this.seed = seed;
    }

    public Map<List<Integer>, List<Integer>> split(Matrix x) {
        Integer numberOfSamples = x.rowCount();
        Integer splitLength = numberOfSamples / numberOfSplits;

        // List of indices ex. 1, 2, 3 ... n-1, n
        List<Integer> indices = new ArrayList<Integer>();
        indices.addAll(range(0, numberOfSamples));
        shuffle(indices, seed); // shuffle that list

        Map<List<Integer>, List<Integer>> trainTestIndices = new HashMap<List<Integer>, List<Integer>>();
        for(int split = 0; split < numberOfSplits-1; split++) {
            List<Integer> trainIndices = new ArrayList<Integer>();
            List<Integer> testIndices = new ArrayList<Integer>();

            // get test range
            Integer from = split*splitLength;
            Integer to = (split+1)*splitLength;
            LOG.debug("split from {} to {}", from, to);

            // add shuffled indices
            testIndices.addAll(indices.subList(from, to));
            trainIndices.addAll(indices.subList(0, from));
            trainIndices.addAll(indices.subList(to, numberOfSamples));

            trainTestIndices.put(trainIndices, testIndices);
        }

        // adding remaining elements
        List<Integer> trainIndices = new ArrayList<Integer>();
        List<Integer> testIndices = new ArrayList<Integer>();

        Integer from = (numberOfSplits-1)*splitLength;
        Integer to = numberOfSamples;
        LOG.debug("last split from {} to {}", from, to);

        testIndices.addAll(indices.subList(from, to));
        trainIndices.addAll(indices.subList(0, from));
        trainIndices.addAll(indices.subList(to, numberOfSamples));

        trainTestIndices.put(trainIndices, testIndices);

        return trainTestIndices;
    }
}
