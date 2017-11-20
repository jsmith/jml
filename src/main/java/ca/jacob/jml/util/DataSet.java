package ca.jacob.jml.util;

import ca.jacob.jml.nb.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.util.ML.calculateWeightedEntropy;
import static ca.jacob.jml.util.Math.calculateOccurrences;
import static ca.jacob.jml.util.Math.log2;

public class DataSet {
    private static final Logger LOG = LoggerFactory.getLogger(DataSet.class);
    public static final int DISCRETE = 0;
    public static final int CONTINUOUS = 1;

    private String name;
    private Matrix x;
    private Vector y;
    private final Vector attributeTypes;

    public DataSet(Matrix x, Vector y, Vector attributeTypes) {
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

    public DataSet(Matrix data, Vector attributeTypes) {
        if(data.colCount()-1 != attributeTypes.length()) {
            LOG.error("length mismatch: attributes: {}, attribute types: {}", data.colCount()-1, attributeTypes.length());
            throw new DataException("attribute type vector length must match attribute count");
        }

        this.x = data;
        this.y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);
        this.attributeTypes = attributeTypes;
    }

    public DataSet(Matrix data, int attributeType) {
        Vector dataTypes = new Vector(new int[data.colCount()-1]);
        dataTypes.fill(attributeType);
        this.attributeTypes = dataTypes;

        this.x = data;
        this.y = x.col(x.colCount()-1);
        this.x.dropCol(x.colCount()-1);
    }

    public DataSet(Matrix x, Vector y, int attributeType) {
        Vector dataTypes = new Vector(new int[x.colCount()]);
        dataTypes.fill(attributeType);
        this.attributeTypes = dataTypes;

        this.x = x;
        this.y = y;
    }

    public DataSet(Vector attributeTypes) {
        this.attributeTypes = attributeTypes;
        this.x = new Matrix();
        this.y = new Vector();
    }

    public int sampleCount() {
        return x.rowCount();
    }

    public Map<Integer, DataSet> splitByClass() {
        Map<Integer, DataSet> separated = new HashMap<Integer, DataSet>();

        for (int i = 0; i < x.rowCount(); i++) {
            LOG.trace("checking row {}", i);
            int value = y.intAt(i);

            DataSet subset = separated.get(value);
            if (subset == null) {
                LOG.trace("adding new split based on value {}", value);
                subset = new DataSet(this.attributeTypes);
                separated.put(value, subset);
            }

            Vector v = this.sample(i);
            subset.add(v);
        }

        return separated;
    }

    public Map<Integer, DataSet> splitByDiscreteAttribute(int attribute) {
        if(this.attributeType(attribute) != DISCRETE) {
            throw new AttributeException("must be discrete attribute");
        }

        Map<Integer, DataSet> subsets = new HashMap<>();
        for (int i = 0; i < x.rowCount(); i++) {
            LOG.trace("checking row {}", i);
            int value = x.intAt(i, attribute);

            DataSet subset = subsets.get(value);
            if (subset == null) {
                LOG.trace("adding new split based on value {}", value);
                subset = new DataSet(this.attributeTypes.clone());
                subsets.put(value, subset);
            }

            Vector v = this.sample(i);
            subset.add(v);
        }
        return subsets;
    }

    public Tuple<Double, Tuple<DataSet, DataSet>> splitByContinuousAttribute(int attribute) {
        if(this.attributeType(attribute) != CONTINUOUS) {
            throw new DataException("must be continuous attribute");
        }

        Vector c = x.col(attribute);
        c.sort();

        LOG.debug("splitting with attribute -> {}", c);

        Tuple<Double, Tuple<DataSet, DataSet>> bestSubsets = null;
        double minimumEntropy = 0;
        for (int i = 0; i < c.length()-1; i++) {
            if(c.at(i) == c.at(i+1)) {
                continue;
            }

            double pivot = (c.at(i) + c.at(i+1)) / 2;
            Tuple<DataSet, DataSet> subsets = splitAt(attribute, pivot);

            double entropy = calculateWeightedEntropy(subsets);
            if(bestSubsets == null || entropy < minimumEntropy) {
                bestSubsets = new Tuple<>(pivot, subsets);
                minimumEntropy = entropy;
            }
        }
        return bestSubsets;
    }

    public Tuple<DataSet, DataSet> splitAt(int attribute, double pivot) {
        if(this.attributeType(attribute) != CONTINUOUS) {
            throw new DataException("splitAt must use a continuous attribute");
        }

        DataSet under = new DataSet(attributeTypes.clone());
        DataSet over = new DataSet(attributeTypes.clone());
        for (int i = 0; i < x.rowCount(); i++) {
            double value = x.at(i, attribute);

            if(value < pivot) {
                under.add(this.sample(i));
            } else if(value > pivot) {
                over.add(this.sample(i));
            } else {
                throw new DataException("given value must not match value from attribute");
            }
        }

        return new Tuple<>(under, over);
    }

    public void add(Vector sample) {
        LOG.trace("adding sample: {} to y: {} and x: {}", sample, y, x);
        y.add(sample.at(sample.length()-1));
        sample.remove(sample.length()-1);
        x.pushRow(sample.clone());
    }

    public Vector sample(int i) {
        Vector sample = x.row(i);
        sample.add(y.at(i));
        LOG.trace("sample is: {}", sample);
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

    public DataSet samples(Vector indices) {
        return new DataSet(x.rows(indices), y.at(indices), attributeTypes.clone());
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

    @Override
    public String toString() {
        return this.name + "["+this.sampleCount()+" x "+this.attributeCount()+"]";
    }

    public String dataToString() {
        String toReturn = "\n";
        for(int i = 0; i < x.rowCount(); i++) {
            toReturn += x.row(i) + " -> " + y.at(i) + "\n";
        }
        return toReturn;
    }

    public int classValue(int i) {
        return y.intAt(i);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setY(Vector y) {
        this.y = y;
    }

    public void dropAttribute(int attribute) {
        x.dropCol(attribute);
        attributeTypes.remove(attribute);
    }

    public double entropy() {
        Map<Integer, Integer> classes = calculateOccurrences(this.classes());
        LOG.trace("there are {} different class", classes.size());
        LOG.debug("classes: {}", classes);

        double sum = 0.;
        for (int count : classes.values()) {
            sum += count;
        }
        LOG.trace("sum is " + sum);

        double entropy = 0.;
        for (int count : classes.values()) {
            entropy -= count / sum * log2(count / sum);
        }

        return entropy;
    }

    public int getClassification(int i) {
        return y.intAt(i);
    }
}
