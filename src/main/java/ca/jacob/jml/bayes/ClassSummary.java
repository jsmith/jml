package ca.jacob.jml.bayes;

import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClassSummary {
    private static final Logger LOG = LoggerFactory.getLogger(ClassSummary.class);

    private int classValue;
    private double classProbability;
    private List<Attribute> attributes;

    ClassSummary(int classValue, double classProbability, List<Attribute> attributes) {
        this.classValue = classValue;
        this.classProbability = classProbability;
        this.attributes = attributes;
    }

    public double probability(Vector e) {
        LOG.debug("probability for class value: {}", classValue);
        double probability = classProbability;
        for(int i = 0; i < attributes.size(); i++) {
            double conditionalProbability = attributes.get(i).probability(e.at(i));
            LOG.debug("Conditional probability for attribute {} is {}", i, conditionalProbability);

            probability *= conditionalProbability;
        }
        return probability;
    }

    int getClassValue() {
        return classValue;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public double getClassProbability() {
        return classProbability;
    }
}
