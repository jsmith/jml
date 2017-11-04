package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.error;

public abstract class Model {
    private static final Logger LOG = LoggerFactory.getLogger(Model.class);

    public abstract int predict(Vector e);
    public abstract Vector predict(Matrix x);

    public double accuracy(Data data) {
        Matrix x = data.getX();
        Vector y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);
        Vector yHat = predict(x);
        LOG.info("y {}, yHat {}", y.subVector(0, 5), yHat.subVector(0, 5));
        Vector err = error(y, yHat);
        return (1 - err.sum() / err.length()) * 100;
    }
}
