package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
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

    public Model fit(Matrix x, Vector y) {
        RandomForestModel forest = new RandomForestModel();

        int numberOfSamples = (int)(percentageOfSamples * x.rowCount());
        LOG.info("number of samples per tree is {}", numberOfSamples);

        for(int i = 0; i < sizeOfForest; i++) {
            LOG.debug("starting iteration {}", i+1);

            Vector indices = generateIndices(0, x.rowCount(), numberOfSamples);
            Matrix xSubSet = x.rows(indices);
            LOG.debug("xSubSet rows: {}, cols: {}", xSubSet.rowCount(), xSubSet.colCount());
            Vector ySubSet = y.at(indices);
            LOG.debug("first row: {} -> {}", xSubSet.row(0), ySubSet.at(0));

            Model m = algorithm.fit(xSubSet, ySubSet);
            LOG.debug("accuracy of model: {}", m.accuracy(xSubSet, ySubSet));

            forest.add(m);
        }

        return forest;
    }
}
