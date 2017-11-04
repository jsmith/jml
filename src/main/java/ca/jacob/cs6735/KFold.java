package ca.jacob.cs6735;

import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Report;
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

    private int numberOfSplits;
    private Long seed;

    public KFold(int numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
    }

    public KFold(int numberOfSplits, Long seed) {
        this.numberOfSplits = numberOfSplits;
        this.seed = seed;
    }

    public Report generateReport(Algorithm a, Data data) {
        Vector accuracies = new Vector();
        Map<Vector, Vector> indices = this.generateIndices(data);
        for(Map.Entry<Vector, Vector> entry : indices.entrySet()) {
            Vector trainIndices = entry.getKey();
            Vector testIndices = entry.getValue();

            Data trainingData = data.samples(trainIndices);
            Model m = a.fit(trainingData);

            Data testData = data.samples(testIndices);

            accuracies.add(m.accuracy(testData));
        }
        return new Report(accuracies);
    }

    public Map<Vector, Vector> generateIndices(Data data) {
        int numberOfSamples = data.sampleCount();
        int splitLength = numberOfSamples / numberOfSplits;

        // List of indices ex. 1, 2, 3 ... n-1, n
        Vector indices = new Vector();
        indices.concat(range(0, numberOfSamples));
        shuffle(indices, seed); // shuffle that list

        Map<Vector, Vector> trainTestIndices = new HashMap<Vector, Vector>();
        for(int split = 0; split < numberOfSplits-1; split++) {
            Vector trainIndices = new Vector();
            Vector testIndices = new Vector();

            // get test range
            int from = split*splitLength;
            int to = (split+1)*splitLength;
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

        int from = (numberOfSplits-1)*splitLength;
        int to = numberOfSamples;
        LOG.debug("last split from {} to {}", from, to);

        testIndices.concat(indices.subVector(from, to));
        trainIndices.concat(indices.subVector(0, from));
        trainIndices.concat(indices.subVector(to, numberOfSamples));

        trainTestIndices.put(trainIndices, testIndices);

        return trainTestIndices;
    }
}
