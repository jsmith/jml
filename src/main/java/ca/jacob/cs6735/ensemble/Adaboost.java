package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.utils.Vector;

import java.util.Arrays;

import static ca.jacob.cs6735.utils.Math.error;
import static ca.jacob.cs6735.utils.Math.exp;
import static ca.jacob.cs6735.utils.Math.ln;

public class Adaboost {
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
            Model m = algorithm.train(x, y, weights.toArray());
            Integer[] predictions = m.predict(x);
            Vector terror = new Vector(error(predictions, y));
            Double error = weights.dot(predictions)/weights.sum();
            Double stage = ln((1-error)/error);
            weights = weights.mul(exp(terror.mul(stage)));
            model.add(m);
        }



        return model;
    }
}
