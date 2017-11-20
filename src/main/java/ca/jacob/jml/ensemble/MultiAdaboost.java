package ca.jacob.jml.ensemble;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Vector;

public class MultiAdaboost implements Algorithm {
    private Algorithm algorithm;
    private int numberOfEstimators;
    private double proportionOfSamples;

    public MultiAdaboost(Algorithm algorithm, int numberOfEstimators, double proportionOfSamples) {
        this.algorithm = algorithm;
        this.numberOfEstimators = numberOfEstimators;
        this.proportionOfSamples = proportionOfSamples;
    }


    @Override
    public Model fit(DataSet d) {
        Vector classes = d.classes();
        Vector uniqueClasses = d.unique();
        for(int i = 0; i < uniqueClasses.length(); i++) {
            int c = uniqueClasses.intAt(i);

        }
        return new MultiAdaboostModel();
    }
}
