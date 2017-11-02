package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
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

    public Model fit(Matrix x, Vector y) {
        AdaboostModel model = new AdaboostModel();

        Vector weights = new Vector(new Double[x.rowCount()]);
        weights.fill(1./x.rowCount());

        for(Integer i = 0; i < numberOfEstimators; i++) {
            Vector indices = generateIndices(weights);
            Matrix xWeighted = x.rows(indices);
            Vector yWeighted = y.at(indices);

            Model classifier = algorithm.fit(xWeighted, yWeighted);
            Vector pred = classifier.predict(x);
            Vector err = error(pred, yWeighted);


            Double epsilon = weights.dot(err); // sum of the weights of misclassified
            Double alpha = (1/2)*ln((1-epsilon)/epsilon);
            Double Z = 2*sqrt(epsilon*(1-epsilon));
            weights = weights.div(Z).mul(exp(yWeighted.mul(pred).mul(-alpha)));

            model.add(classifier, alpha);
        }

        return model;
    }
}
