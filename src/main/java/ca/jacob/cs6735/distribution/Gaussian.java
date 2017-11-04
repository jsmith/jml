package ca.jacob.cs6735.distribution;

import ca.jacob.cs6735.nb.Summary;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.jacob.cs6735.util.Math.mean;
import static ca.jacob.cs6735.util.Math.stdev;

public class Gaussian {
    private static final Logger LOG = LoggerFactory.getLogger(Gaussian.class);

    private List<Summary> summaries;

    public Gaussian(Map<Integer, Matrix> data) {
        int instanceCount = 0;
        for(Map.Entry<Integer, Matrix> entry : data.entrySet()) {
            instanceCount += entry.getValue().rowCount();
        }

        summaries = new ArrayList<Summary>();
        for(Map.Entry<Integer, Matrix> entry : data.entrySet()) {
            int classValue = entry.getKey();

            Matrix instances = entry.getValue();
            instances.dropCol(instances.colCount()-1);

            double classProbability = ((double)instances.rowCount()) / instanceCount;

            Vector means = new Vector(new double[instances.colCount()]);
            Vector stdevs = new Vector(new double[instances.colCount()]);
            for(int j = 0; j < instances.colCount(); j++) {
                Vector col = instances.col(j);
                means.set(j, mean(col));
                stdevs.set(j, stdev(col));
            }
            LOG.debug("Class {}: means -> {}; stdevs -> {}", classValue, means, stdevs);
            summaries.add(new Summary(classValue, classProbability, means, stdevs));
        }
    }

    public List<Summary> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<Summary> summaries) {
        this.summaries = summaries;
    }
}
