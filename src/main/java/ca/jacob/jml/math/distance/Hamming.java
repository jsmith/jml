package ca.jacob.jml.math.distance;

import ca.jacob.jml.math.Vector;

import static ca.jacob.jml.Util.error;

public class Hamming implements DistanceFunction {
    @Override
    public double distance(Vector one, Vector two) {
        return error(one, two).sum();
    }
}
