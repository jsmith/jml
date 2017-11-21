package ca.jacob.jml;

import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.Util.error;

public abstract class Model {
    private static final Logger LOG = LoggerFactory.getLogger(Model.class);

    public abstract int predict(Vector e);

    public Vector predict(Matrix x) {
        Vector predictions = new Vector(new double[x.rowCount()]);
        for(int i = 0; i < x.rowCount(); i++) {
            predictions.set(i, predict(x.row(i)));
        }
        return predictions;
    }

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
