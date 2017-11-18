package ca.jacob.jml.ensemble;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.util.ML.generateIndices;

public class RandomForest implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(RandomForest.class);
    private static final String NAME = "Random Forest";

    private Algorithm algorithm;
    private int sizeOfForest;
    private double percentageOfSamples;

    public RandomForest(Algorithm algorithm, int sizeOfForest, double percentageOfSamples) {
        this.algorithm = algorithm;
        this.sizeOfForest = sizeOfForest;
        this.percentageOfSamples = percentageOfSamples;
    }

    public Model fit(DataSet dataSet) {
        RandomForestModel forest = new RandomForestModel();

        int numberOfSamples = (int)(percentageOfSamples * dataSet.sampleCount());
        LOG.info("number of samples per tree is {}", numberOfSamples);

        for(int i = 0; i < sizeOfForest; i++) {
            LOG.debug("starting iteration {}", i+1);

            Vector indices = generateIndices(0, dataSet.sampleCount(), numberOfSamples);
            DataSet subset = dataSet.samples(indices);

            Model m = algorithm.fit(subset);
            LOG.debug("accuracy of model: {}", m.accuracy(subset));

            forest.add(m);
        }

        return forest;
    }

    @Override
    public String toString() {
        return NAME + "(size:"+sizeOfForest+", percentage:"+percentageOfSamples+") with " + algorithm.toString();
    }
}
