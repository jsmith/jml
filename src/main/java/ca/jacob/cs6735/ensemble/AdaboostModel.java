package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static ca.jacob.cs6735.util.ML.sign;

public class AdaboostModel extends Model {
    private Map<Model, Double> models;

    @Override
    public int predict(Vector e) {
        double sum = 0.;
        for(Map.Entry<Model, Double> model : models.entrySet()) {
            sum += model.getValue() * model.getKey().predict(e);
        }
        return sign(sum);
    }

    @Override
    public Vector predict(Matrix data) {
        Vector predictions = new Vector(new double[data.rowCount()]);
        for(int i = 0; i < data.rowCount(); i++) {
            predictions.set(i, predict(data.row(i)));
        }
        return predictions;
    }

    public void add(Model model, double stage) {
        if(models == null) {
            models = new HashMap<Model, Double>();
        }
        models.put(model, stage);
    }
}
