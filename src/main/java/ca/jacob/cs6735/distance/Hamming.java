package ca.jacob.cs6735.distance;

import ca.jacob.cs6735.util.Vector;

import static ca.jacob.cs6735.util.ML.error;
import static ca.jacob.cs6735.util.Math.abs;

public class Hamming implements DistanceFunction {
    @Override
    public double distance(Vector one, Vector two) {
        return error(one, two).sum();
    }
}
