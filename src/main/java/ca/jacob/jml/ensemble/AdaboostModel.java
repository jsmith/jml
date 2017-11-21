package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static ca.jacob.jml.Util.sign;

public class AdaboostModel extends Model {
    private Vector alphas;
    private List<Model> models;

    @Override
    public int predict(Vector e) {
        return sign(this.prediction(e));
    }

    public double prediction(Vector e) {
        double sum = 0;
        for(int i = 0; i < models.size(); i++) {
            sum += alphas.at(i) * models.get(i).predict(e);
        }
        return sum;
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
