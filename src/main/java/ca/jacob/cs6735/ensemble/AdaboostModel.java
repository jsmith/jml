package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Model;

import java.util.HashMap;
import java.util.Map;

import static ca.jacob.cs6735.util.ML.sign;

public class AdaboostModel implements Model {
    private Map<Model, Double> models;

    @Override
    public Integer predict(Integer[] e) {
        Double sum = 0.;
        for(Map.Entry<Model, Double> model : models.entrySet()) {
            sum += model.getValue() * model.getKey().predict(e);
        }
        return sign(sum);
    }

    @Override
    public Integer[] predict(Integer[][] data) {
        Integer[] predictions = new Integer[data.length];
        for(int i = 0; i < data.length; i++) {
            predictions[i] = predict(data[i]);
        }
        return predictions;
    }

    public void add(Model model, Double stage) {
        if(models == null) {
            models = new HashMap<Model, Double>();
        }
        models.put(model, stage);
    }
}
