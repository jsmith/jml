package ca.jacob.jml.ensemble;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MultiAdaboost implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(MultiAdaboost.class);

    private Adaboost adaboost;

    public MultiAdaboost(Algorithm algorithm, int numberOfEstimators, double proportionOfSamples) {
        this.adaboost = new Adaboost(algorithm, numberOfEstimators, proportionOfSamples);
    }


    @Override
    public Model fit(DataSet d) {
        List<Tuple<Integer, AdaboostModel>> adaboostModels = new ArrayList<>();

        Vector classes = d.classes();
        Vector uniqueClasses = classes.unique();
        LOG.debug("there are {} unique classes", uniqueClasses.length());

        for(int i = 0; i < uniqueClasses.length(); i++) {
            int c = uniqueClasses.intAt(i);

            Vector newClasses = classes.clone();
            for(int j = 0; j < newClasses.length(); j++) {
                if(newClasses.intAt(j) == c) {
                    newClasses.set(j, 1);
                } else {
                    newClasses.set(j, -1);
                }
            }

            d.replaceClasses(newClasses);
            adaboostModels.add(new Tuple<>(c, (AdaboostModel) adaboost.fit(d)));
        }
        return new MultiAdaboostModel(adaboostModels);
    }
}
