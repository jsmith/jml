package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Vector;

import static ca.jacob.cs6735.util.ML.error;
import static ca.jacob.cs6735.util.ML.generateIndices;
import static ca.jacob.cs6735.util.Math.*;
import static java.lang.Math.sqrt;


public class Adaboost implements Algorithm {
    private Algorithm algorithm;
    private Integer numberOfEstimators;

    public Adaboost(Algorithm algorithm, Integer numberOfEstimators) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
    }

    public Model fit(Integer[][] x, Integer[] y) {
        AdaboostModel model = new AdaboostModel();

        Vector weights = new Vector(new Double[x.length]);
        weights.fill(1./x.length);

        for(Integer i = 0; i < numberOfEstimators; i++) {
            Integer[] indices = generateIndices(weights);
            Integer[][] xWeighted = new Integer[x.length][x[0].length];
            Integer[] yWeighted = new Integer[y.length];
            for(Integer j = 0; j < indices.length; j++) {
                xWeighted[j] = x[indices[j]];
                yWeighted[j] = y[indices[j]];
            }

            Model classifier = algorithm.fit(xWeighted, yWeighted);
            Integer[] predictions = classifier.predict(x);
            Vector err = new Vector(error(predictions, yWeighted));

            Vector pred = new Vector(predictions);
            Vector yHat = new Vector(yWeighted);

            Double epsilon = weights.dot(err); // sum of the weights of misclassified
            Double alpha = (1/2)*ln((1-epsilon)/epsilon);
            Double Z = 2*sqrt(epsilon*(1-epsilon));
            weights = weights.div(Z).mul(exp(yHat.mul(pred).mul(-alpha)));

            model.add(classifier, alpha);
        }

        return model;
    }
}
