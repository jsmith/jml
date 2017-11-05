package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.distribution.Distribution;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.jacob.cs6735.util.Data.CONTINUOUS;
import static ca.jacob.cs6735.util.Data.DISCRETE;

public class NaiveBayes implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayes.class);

    private Distribution distribution;

    public NaiveBayes() {}

    public NaiveBayes(Distribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public Model fit(Data data) {
        Map<Integer, Data> separated = data.splitByClass();

        List<ClassSummary> summaries = new ArrayList<ClassSummary>();
        for(Map.Entry<Integer, Data> entry : separated.entrySet()) {
            LOG.debug("starting summary for class {}", entry.getKey());
            List<Attribute> attributes = new ArrayList<Attribute>();

            int classValue = entry.getKey();
            Data d = entry.getValue();
            double classProbability = ((double)d.sampleCount()) / data.sampleCount();

            LOG.debug("dataset: {}", d);

            for(int j = 0; j < d.attributeCount(); j++) {
                Vector attributeValues = d.attribute(j);
                if(d.attributeType(j) == CONTINUOUS) {
                    if(distribution == null) {
                        throw new BayesException("a distribution must be supplied");
                    }

                    double mean = attributeValues.mean();
                    double stdev = attributeValues.stdev();
                    LOG.debug("attribute {}: mean -> {}; stdev -> {}", j, mean, stdev);

                    attributes.add(new ContinuousAttribute(attributeValues, distribution, mean, stdev));
                } else if(d.attributeType(j) == DISCRETE) {
                    attributes.add(new DiscreteAttribute(attributeValues));
                }

            }
            LOG.debug("{}: probability: {}; attribute count -> {}", classValue, classProbability, attributes.size());

            summaries.add(new ClassSummary(classValue, classProbability, attributes));
        }

        return new NaiveBayesModel(summaries);
    }
}
