package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.math.Vector;
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

    public void add(Model tree) {
        if(trees == null) {
            trees = new ArrayList<Model>();
        }
        trees.add(tree);
    }
}
