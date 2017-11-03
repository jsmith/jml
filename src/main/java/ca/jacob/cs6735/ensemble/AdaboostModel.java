package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.cs6735.util.ML.sign;

public class AdaboostModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(AdaboostModel.class);

    private Vector alphas;
    private List<Model> models;

    @Override
    public int predict(Vector e) {
        double sum = 0;
        for(int i = 0; i < models.size(); i++) {
            sum += alphas.at(i) * models.get(i).predict(e);
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

    public void add(Model model, double alpha) {
        if(models == null) {
            models = new ArrayList<Model>();
            alphas = new Vector();
        }
        models.add(model);
        alphas.add(alpha);
    }
}
