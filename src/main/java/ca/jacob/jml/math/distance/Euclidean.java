package ca.jacob.jml.math.distance;

import ca.jacob.jml.math.Vector;

import static java.lang.Math.sqrt;

public class Euclidean implements DistanceFunction {
    @Override
    public double distance(Vector one, Vector two) {
        return sqrt(one.sub(two).pow(2).sum());
    }
}
