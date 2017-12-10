package ca.jacob.jml.neighbors;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.Model;
import ca.jacob.jml.math.distance.Distance;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class KNNModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(KNNModel.class);

    private Dataset dataset;
    private int k;
    private Distance df;

    public KNNModel(Dataset dataset, int k, Distance df) {
        this.dataset = dataset;
        this.k = k;
        this.df = df;
    }

    @Override
    public int predict(Vector e) {
        Map<Double, Integer> nearest = new TreeMap<>();
        Matrix x = dataset.getX();
        for(int i = 0; i < x.rowCount(); i++) {
            Vector r = x.row(i);
            double distance = df.distance(r, e);
            nearest.put(distance, i);
        }

        int i = 0;
        Vector votes = new Vector();
        for(Map.Entry<Double, Integer> entry : nearest.entrySet()) {
            if(i >= k) {
                break;
            }

            int index = entry.getValue();
            int classValue = dataset.classValue(index);

            double distance = entry.getKey();
            LOG.debug("{}: index: {}; distance: {}, vote: {}", new Object[]{i, index, distance, classValue});

            votes.add(classValue);

            i++;
        }

        int prediction = votes.valueOfMaxOccurrence();
        LOG.debug("predicting: {}", prediction);
        return prediction;
    }

    @Override
    public Vector predict(Matrix x) {
        Vector predictions = new Vector(new int[x.rowCount()]);
        for(int i = 0; i < x.rowCount(); i++) {
            predictions.set(i, predict(x.row(i)));
        }
        return predictions;
    }
}
