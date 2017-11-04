package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.distribution.Distribution;
import ca.jacob.cs6735.util.Vector;

public class DiscreteAttribute implements Attribute {
    private Vector values;

    public DiscreteAttribute(Vector values) {
        this.values = values;
    }

    @Override
    public double probability(double value) {
        return values.count(value) / values.length();
    }
}
