package ca.jacob.jml.neighbors;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.distance.DistanceFunction;
import ca.jacob.jml.util.DataSet;

public class KNN implements Algorithm {
    private static final String NAME = "K-Nearest Neighbour";

    private int k;
    private boolean weighted;
    private DistanceFunction df;

    public KNN(int k, boolean weighted, DistanceFunction df) {
        this.k = k;
        this.weighted = weighted;
        this.df = df;
    }

    @Override
    public Model fit(DataSet d) {
        if(k > d.sampleCount()) {
            throw new KNNException("k must be smaller or equal to data set sample count");
        }
        return new KNNModel(d, k, weighted, df);
    }

    @Override
    public String toString() {
        return NAME + "(k:"+k+", weighted:"+weighted+")";
    }
}
