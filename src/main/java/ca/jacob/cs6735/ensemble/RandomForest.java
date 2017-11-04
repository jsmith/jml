package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.error;
import static ca.jacob.cs6735.util.ML.generateIndices;
import static ca.jacob.cs6735.util.Math.exp;
import static ca.jacob.cs6735.util.Math.ln;

public class RandomForest implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(RandomForest.class);

    private Algorithm algorithm;
    private int sizeOfForest;
    private double percentageOfSamples;

    public RandomForest(Algorithm algorithm, int sizeOfForest, double percentageOfSamples) {
        this.algorithm = algorithm;
        this.sizeOfForest = sizeOfForest;
        this.percentageOfSamples = percentageOfSamples;
    }

    public Model fit(Data data) {
        RandomForestModel forest = new RandomForestModel();

        int numberOfSamples = (int)(percentageOfSamples * data.sampleCount());
        LOG.info("number of samples per tree is {}", numberOfSamples);

        for(int i = 0; i < sizeOfForest; i++) {
            LOG.debug("starting iteration {}", i+1);

            Vector indices = generateIndices(0, data.sampleCount(), numberOfSamples);
            Data subset = data.samples(indices);

            Model m = algorithm.fit(subset);
            LOG.debug("accuracy of model: {}", m.accuracy(subset));

            forest.add(m);
        }

        return forest;
    }
}
