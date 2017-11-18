package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static ca.jacob.jml.util.ML.sign;

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
