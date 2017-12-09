package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.exceptions.PredictionException;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaBoostModel extends Model {
    private Vector alphas;
    private Vector uniqueClasses;
    private List<Model> models;

    public AdaBoostModel(Vector classes) {
        this.uniqueClasses = classes.unique();
    }

    @Override
    public int predict(Vector e) {
        Map<Integer, Double> probabilities = new HashMap<>();
        for(int i = 0; i < uniqueClasses.length(); i++) {
            int c = uniqueClasses.intAt(i);

            double sum = 0;
            for(int j = 0; j < models.size(); j++) {
                int classification = models.get(j).predict(e);
                Double probability = probabilities.get(classification);
                if(probability == null) {
                    probability = 0.;
                    probabilities.put(classification, probability);
                }

                probability += alphas.at(j);
                probabilities.replace(classification, probability);
            }
        }

        Tuple<Integer, Double> max = null;
        for(Map.Entry<Integer, Double> probability : probabilities.entrySet()) {
            if(max == null || max.last() < probability.getValue()) {
                max = new Tuple<>(probability.getKey(), probability.getValue());
            }
        }

        if(max == null) {
            throw new PredictionException("max is null");
        }

        return max.first();
    }

    public void add(Model model, double alpha) {
        if(models == null) {
            models = new ArrayList<Model>();
            alphas = new Vector();
        }
        models.add(model);
        alphas.add(alpha);
    }
}
