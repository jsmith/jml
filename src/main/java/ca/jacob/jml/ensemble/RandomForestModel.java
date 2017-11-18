package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
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
        LOG.debug("predictions: {}", predictions);
        LOG.debug("prediction {}", predictions.valueOfMaxOccurrence());

        return predictions.valueOfMaxOccurrence();
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
