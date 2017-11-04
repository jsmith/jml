package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.distribution.Distribution;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.jacob.cs6735.util.ML.splitByColumn;
import static ca.jacob.cs6735.util.Math.mean;
import static ca.jacob.cs6735.util.Math.stdev;

public class NaiveBayes implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayes.class);

    private Distribution distribution;
    private Vector continuousAttributes;

    public NaiveBayes() {}

    public NaiveBayes(Distribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public Model fit(Data data) {
        x.pushCol(y);
        Map<Integer, Matrix> separated = splitByColumn(x, x.colCount()-1);
        x.dropCol(x.colCount()-1);


        List<ClassSummary> summaries = new ArrayList<ClassSummary>();
        for(Map.Entry<Integer, Matrix> entry : separated.entrySet()) {
            int classValue = entry.getKey();

            Matrix instances = entry.getValue();
            instances.dropCol(instances.colCount()-1);

            double classProbability = ((double)instances.rowCount()) / x.rowCount();

            Vector means = new Vector(new double[continuousAttributes.length()]);
            Vector stdevs = new Vector(new double[instances.colCount()]);
            for(int j = 0; j < instances.colCount(); j++) {
                Vector col = instances.col(j);
                means.set(j, mean(col));
                stdevs.set(j, stdev(col));
            }
            LOG.debug("Class {}: means -> {}; stdevs -> {}", classValue, means, stdevs);
            summaries.add(new ClassSummary(classValue, classProbability, means, stdevs));
        }

        return new NaiveBayesModel(summaries, continuousAttributes, distribution);
    }
}
