package ca.jacob.jml;

import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.util.ML.error;

public abstract class Model {
    private static final Logger LOG = LoggerFactory.getLogger(Model.class);

    public abstract int predict(Vector e);
    public abstract Vector predict(Matrix x);

    public double accuracy(DataSet dataSet) {
        Matrix x = dataSet.getX();
        Vector y = dataSet.getY();
        Vector yHat = predict(x);
        LOG.trace("yHat {}", yHat.subVector(0, 5));
        Vector err = error(y, yHat);
        LOG.debug("err -> {}", err);
        return (1 - err.sum() / err.length()) * 100;
    }
}
