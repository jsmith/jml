package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.error;

public abstract class Model {
    private static final Logger LOG = LoggerFactory.getLogger(Model.class);

    public abstract Integer predict(Vector e);
    public abstract Vector predict(Matrix x);

    public Double accuracy(Matrix x, Vector y) {
        Vector yHat = predict(x);
        Vector err = error(y, yHat);
        return (1 - err.sum() / err.length()) * 100;
    }
}
