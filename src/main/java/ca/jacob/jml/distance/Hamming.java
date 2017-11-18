package ca.jacob.jml.distance;

import ca.jacob.jml.util.Vector;

import static ca.jacob.jml.util.ML.error;

public class Hamming implements DistanceFunction {
    @Override
    public double distance(Vector one, Vector two) {
        return error(one, two).sum();
    }
}
