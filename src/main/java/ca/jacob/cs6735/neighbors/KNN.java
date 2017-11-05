package ca.jacob.cs6735.neighbors;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.distance.DistanceFunction;
import ca.jacob.cs6735.util.Data;

public class KNN implements Algorithm {
    private int k;
    private boolean weighted;
    private DistanceFunction df;

    public KNN(int k, boolean weighted, DistanceFunction df) {
        this.k = k;
        this.weighted = weighted;
        this.df = df;
    }

    @Override
    public Model fit(Data d) {
        if(k > d.sampleCount()) {
            throw new KNNException("k must be smaller or equal to data set sample count");
        }
        return new KNNModel(d, k, weighted, df);
    }
}
