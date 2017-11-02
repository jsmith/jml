package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.ML.error;

public abstract class Model {
    private static final Logger LOG = LoggerFactory.getLogger(Model.class);

    public abstract Integer predict(Integer[] e);
    public abstract Integer[] predict(Integer[][] x);

    public Double accuracy(Integer[][] x, Integer[] y) {
        Integer[] yHat = predict(x);
        Vector err = new Vector(error(y, yHat));
        return (1 - err.sum() / err.length()) * 100;
    }
}
