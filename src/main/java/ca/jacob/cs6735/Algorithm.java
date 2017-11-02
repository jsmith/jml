package ca.jacob.cs6735;

import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

public interface Algorithm {
    Model fit(Matrix x, Vector y);
}
