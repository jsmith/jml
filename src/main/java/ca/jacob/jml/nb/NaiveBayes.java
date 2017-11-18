package ca.jacob.jml.nb;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Model;
import ca.jacob.jml.distribution.Distribution;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;

public class NaiveBayes implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayes.class);
    private static final String NAME = "Naive Bayes";

    private Distribution distribution;

    public NaiveBayes() {}

    public NaiveBayes(Distribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public Model fit(DataSet dataSet) {
        Map<Integer, DataSet> separated = dataSet.splitByClass();

        List<ClassSummary> summaries = new ArrayList<ClassSummary>();
        for(Map.Entry<Integer, DataSet> entry : separated.entrySet()) {
            LOG.debug("starting summary for class {}", entry.getKey());
            List<Attribute> attributes = new ArrayList<Attribute>();

            int classValue = entry.getKey();
            DataSet d = entry.getValue();
            double classProbability = ((double)d.sampleCount()) / dataSet.sampleCount();

            LOG.debug("dataset: {}", d);
            for(int j = 0; j < d.attributeCount(); j++) {
                Vector attributeValues = d.attribute(j);
                if(d.attributeType(j) == CONTINUOUS) {
                    if(distribution == null) {
                        throw new BayesException("a distribution must be supplied");
                    }
                    attributes.add(new ContinuousAttribute(j, attributeValues, distribution));
                } else if(d.attributeType(j) == DISCRETE) {
                    attributes.add(new DiscreteAttribute(j, attributeValues));
                }

            }
            LOG.debug("{}: probability: {}; attribute count -> {}", classValue, classProbability, attributes.size());

            summaries.add(new ClassSummary(classValue, classProbability, attributes));
        }

        return new NaiveBayesModel(summaries);
    }

    @Override
    public String toString() {
        return NAME+"(dist:"+distribution+")";
    }
}