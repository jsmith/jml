package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RandomForestModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(RandomForestModel.class);

    private List<Model> trees;

    @Override
    public int predict(Vector e) {
        Vector predictions = new Vector(new double[trees.size()]);
        for(int i = 0; i < trees.size(); i++) {
            predictions.set(i, trees.get(i).predict(e));
        }
        double prediction = predictions.sum() / predictions.length();
        LOG.debug("prediction {}", predictions.valueOfMaxOccurrence());
        return (int) predictions.valueOfMaxOccurrence();
    }

    @Override
    public Vector predict(Matrix x) {
        Vector predictions = new Vector(new double[x.rowCount()]);
        for(int i = 0; i < x.rowCount(); i++) {
            predictions.set(i, this.predict(x.row(i)));
        }
        return predictions;
    }

    public void add(Model tree) {
        if(trees == null) {
            trees = new ArrayList<Model>();
        }
        trees.add(tree);
    }
}
