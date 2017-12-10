package ca.jacob.jml.bayes;

import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Discrete implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(Discrete.class);

    private Vector values;
    private int classCount;
    private Map<Integer, Double> conditionalProbabilities;

    public Discrete(Vector values, int classCount) {
        this.values = values;
        this.classCount = classCount;

        Vector unique = values.unique();
        conditionalProbabilities = new HashMap<>();
        for(int i = 0; i < unique.length(); i++) {
            int value = unique.intAt(i);
            conditionalProbabilities.put(value, conditionalProbability(value));
        }
    }

    private double conditionalProbability(int value) {
        return (((double)values.count(value))+1) / (values.length()+classCount);
    }

    @Override
    public double probability(double value) {
        Double probability = conditionalProbabilities.get((int)value);
        if(probability  == null) {
            conditionalProbabilities.put((int)value, conditionalProbability((int)value));
            probability = conditionalProbabilities.get((int)value);
        }

        return probability;
    }
}
