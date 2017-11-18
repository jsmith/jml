package ca.jacob.jml.distance;

import ca.jacob.jml.util.Vector;

import static java.lang.Math.sqrt;

public class Euclidean implements DistanceFunction {
    @Override
    public double distance(Vector one, Vector two) {
        return sqrt(one.sub(two).pow(2).sum());
    }
}
