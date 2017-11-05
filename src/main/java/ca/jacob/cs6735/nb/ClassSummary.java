package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.util.Vector;

import java.util.List;

public class ClassSummary {
    private int classValue;
    private double classProbability;
    List<Attribute> attributes;

    public ClassSummary(int classValue, double classProbability, List<Attribute> attributes) {
        this.classValue = classValue;
        this.classProbability = classProbability;
        this.attributes = attributes;
    }

    public double probability(Vector e) {
        Vector conditionalProbabilities = new Vector(new double[attributes.size()]);
        for(int i = 0; i < attributes.size(); i++) {
            double attributeValue = e.at(i);
            double probability = attributes.get(i).probability(attributeValue);
            conditionalProbabilities.set(i, probability);
        }
        return conditionalProbabilities.prod() * classProbability;
    }

    public int getClassValue() {
        return classValue;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public double getClassProbability() {
        return classProbability;
    }
}
