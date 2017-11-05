package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private static final Logger LOG = LoggerFactory.getLogger(Data.class);
    public static final int DISCRETE = 0;
    public static final int CONTINUOUS = 1;

    private Matrix x;
    private Vector y;
    private final Vector attributeTypes;

    public Data(Matrix x, Vector y, Vector attributeTypes) {
        if(x.colCount() != attributeTypes.length()) {
            LOG.error("length mismatch: attributes: {}, attribute types: {}", x.colCount(), attributeTypes.length());
            throw new DataException("attribute type vector length must match attribute count");
        }

        if(x.rowCount() != y.length()) {
            throw new DataException("x row count and y length must match!");
        }

        this.x = x;
        this.y = y;
        this.attributeTypes = attributeTypes;
    }

    public Data(Matrix data, Vector attributeTypes) {
        if(data.colCount()-1 != attributeTypes.length()) {
            LOG.error("length mismatch: attributes: {}, attribute types: {}", data.colCount()-1, attributeTypes.length());
            throw new DataException("attribute type vector length must match attribute count");
        }

        this.x = data;
        this.y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);
        this.attributeTypes = attributeTypes;
    }

    public Data(Matrix data, int attributeType) {
        Vector dataTypes = new Vector(new int[data.colCount()-1]);
        dataTypes.fill(attributeType);
        this.attributeTypes = dataTypes;

        this.x = data;
        this.y = x.col(x.colCount()-1);
        this.x.dropCol(x.colCount()-1);
    }

    public Data(Matrix x, Vector y, int attributeType) {
        Vector dataTypes = new Vector(new int[x.colCount()]);
        dataTypes.fill(attributeType);
        this.attributeTypes = dataTypes;

        this.x = x;
        this.y = y;
    }

    public Data(Vector attributeTypes) {
        this.attributeTypes = attributeTypes;
        this.x = new Matrix();
        this.y = new Vector();
    }

    public int sampleCount() {
        return x.rowCount();
    }

    public Map<Integer, Data> splitByClass() {
        Map<Integer, Data> separated = new HashMap<Integer, Data>();

        for (int i = 0; i < x.rowCount(); i++) {
            LOG.trace("checking row {}", i);
            int value = y.intAt(i);

            Data d = separated.get(value);
            if (d == null) {
                LOG.trace("adding new split based on value {}", value);
                d = new Data(this.attributeTypes);
                separated.put(value, d);
            }

            Vector v = this.sample(i);
            d.add(v);
        }

        return separated;
    }

    public void add(Vector sample) {
        LOG.debug("adding sample: {} to y: {} and x: {}", sample, y, x);
        y.add(sample.at(sample.length()-1));
        sample.remove(sample.length()-1);
        x.pushRow(sample.clone());
    }

    public Vector sample(int i) {
        Vector sample = x.row(i);
        sample.add(y.at(i));
        LOG.debug("sample is: {}", sample);
        return sample;
    }

    public double at(int i, int j) {
        return x.at(i, j);
    }

    public int attributeCount() {
        return x.colCount();
    }

    public Vector attribute(int j) {
        return x.col(j).clone();
    }

    public Vector classes() {
        return y.clone();
    }

    public int attributeType(int j) {
        return attributeTypes.intAt(j);
    }

    public Data samples(Vector indices) {
        return new Data(x.rows(indices), y.at(indices), attributeTypes);
    }

    public Matrix getX() {
        return x;
    }

    public Vector getY() {
        return y;
    }

    public Vector getAttributeTypes() {
        return attributeTypes.clone();
    }
}
