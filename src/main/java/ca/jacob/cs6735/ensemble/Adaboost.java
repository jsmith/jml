package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.utils.Vector;

import static ca.jacob.cs6735.utils.Math.*;
import static java.lang.Math.sqrt;


public class Adaboost implements Algorithm {
    private Algorithm algorithm;
    private Integer numberOfEstimators;

    public Adaboost(Algorithm algorithm, Integer numberOfEstimators) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
    }

    public Model train(Integer[][] x, Integer[] y) {
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

            Model m = algorithm.train(xWeighted, yWeighted);
            Integer[] predictions = m.predict(x);
            Vector err = new Vector(predictionError(predictions, yWeighted));

            Vector h = new Vector(predictions);
            Vector yHat = new Vector(yWeighted);

            Double epsilon = weights.dot(predictions); // sum of the weights of misclassified
            Double alpha = (1/2)*ln((1-epsilon)/epsilon);
            Double Z = 2*sqrt(epsilon*(1-epsilon));
            weights = weights.div(Z).mul(exp(yHat.mul(h).mul(-alpha)));

            model.add(m, alpha);
        }

        return model;
    }
}
