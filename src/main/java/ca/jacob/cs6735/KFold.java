package ca.jacob.cs6735;

import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

import java.util.HashMap;
import java.util.Map;

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

    public Map<Vector, Vector> split(Matrix x) {
        Integer numberOfSamples = x.rowCount();
        Integer splitLength = numberOfSamples / numberOfSplits;

        // List of indices ex. 1, 2, 3 ... n-1, n
        Vector indices = new Vector();
        indices.concat(range(0, numberOfSamples));
        shuffle(indices, seed); // shuffle that list

        Map<Vector, Vector> trainTestIndices = new HashMap<Vector, Vector>();
        for(int split = 0; split < numberOfSplits-1; split++) {
            Vector trainIndices = new Vector();
            Vector testIndices = new Vector();

            // get test range
            Integer from = split*splitLength;
            Integer to = (split+1)*splitLength;
            LOG.debug("split from {} to {}", from, to);

            // add shuffled indices
            testIndices.concat(indices.subVector(from, to));
            trainIndices.concat(indices.subVector(0, from));
            trainIndices.concat(indices.subVector(to, numberOfSamples));

            trainTestIndices.put(trainIndices, testIndices);
        }

        // adding remaining elements
        Vector trainIndices = new Vector();
        Vector testIndices = new Vector();

        Integer from = (numberOfSplits-1)*splitLength;
        Integer to = numberOfSamples;
        LOG.debug("last split from {} to {}", from, to);

        testIndices.concat(indices.subVector(from, to));
        trainIndices.concat(indices.subVector(0, from));
        trainIndices.concat(indices.subVector(to, numberOfSamples));

        trainTestIndices.put(trainIndices, testIndices);

        return trainTestIndices;
    }
}
