package ca.jacob.cs6735.neighbors;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.distance.DistanceFunction;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class KNNModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(KNNModel.class);

    private Data dataset;
    private int k;
    private boolean weighted;
    private DistanceFunction df;

    public KNNModel(Data dataset, int k, boolean weighted, DistanceFunction df) {
        this.dataset = dataset;
        this.k = k;
        this.weighted = weighted;
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
            if(weighted) {
                //TODO
            }
            LOG.debug("{}: index: {}; distance: {}, vote: {}", new Object[]{i, index, distance, classValue});

            votes.add(classValue);

            i++;
        }

        int prediction = (int)votes.valueOfMaxOccurrence();
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
