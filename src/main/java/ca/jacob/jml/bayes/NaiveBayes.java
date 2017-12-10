package ca.jacob.jml.bayes;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Dataset;
import ca.jacob.jml.Model;
import ca.jacob.jml.math.distribution.Distribution;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.Dataset.CONTINUOUS;
import static ca.jacob.jml.Dataset.DISCRETE;

public class NaiveBayes implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayes.class);
    private static final String NAME = "Naive Bayes";

    private Distribution distribution;

    public NaiveBayes() {}

    public NaiveBayes(Distribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public Model fit(Dataset dataset) {
        Map<Integer, Dataset> separated = dataset.splitByClass();

        List<ClassSummary> summaries = new ArrayList<ClassSummary>();
        int classCount = separated.size();
        for(Map.Entry<Integer, Dataset> entry : separated.entrySet()) {
            LOG.debug("starting summary for class {}", entry.getKey());
            List<Attribute> attributes = new ArrayList<Attribute>();

            int classValue = entry.getKey();
            Dataset d = entry.getValue();
            double classProbability = ((double)d.sampleCount()) / dataset.sampleCount();

            LOG.debug("dataset: {}", d);
            for(int j = 0; j < d.attributeCount(); j++) {
                Vector attributeValues = d.attribute(j);
                if(d.attributeType(j) == CONTINUOUS) {
                    if(distribution == null) {
                        throw new BayesException("a distribution must be supplied");
                    }
                    attributes.add(new Continuous(attributeValues, distribution));
                } else if(d.attributeType(j) == DISCRETE) {
                    attributes.add(new Discrete(attributeValues, classCount));
                } else {
                    throw new AttributeException();
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
