package ca.jacob.jml.bayes;

import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Discrete implements Attribute {
    private static final Logger LOG = LoggerFactory.getLogger(Discrete.class);

    private double unseenConditionalProbability;
    private Map<Integer, Double> conditionalProbabilities;

    public Discrete(Vector values, int classCount) {
        int valuesClassCount = values.length() + classCount; // Only value.length() + classCount stored for performance reasons
        unseenConditionalProbability = ((double)1)/(valuesClassCount);


        Vector unique = values.unique();
        conditionalProbabilities = new HashMap<>();
        for(int i = 0; i < unique.length(); i++) {
            int value = unique.intAt(i);
            double conditionalProbability = (((double)values.count(value))+1) / (valuesClassCount);
            conditionalProbabilities.put(value, conditionalProbability);
        }
    }

    @Override
    public double probability(double value) {
        Double probability = conditionalProbabilities.get((int)value);
        if(probability  == null) {
            conditionalProbabilities.put((int)value, unseenConditionalProbability); // values.count(value) would be 0 so it is omitted
            probability = unseenConditionalProbability;
        }

        return probability;
    }
}
