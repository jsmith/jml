package ca.jacob.jml;

import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.math.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.Util.calculateWeightedEntropy;
import static ca.jacob.jml.Util.calculateOccurrences;
import static ca.jacob.jml.math.Util.log2;

public class DataSet {
    private static final Logger LOG = LoggerFactory.getLogger(DataSet.class);
    public static final int DISCRETE = 0;
    public static final int CONTINUOUS = 1;

    private String name;
    private double entropy;
    private Matrix x;
    private Vector attributeTypes;
    private Vector y;

    public DataSet(Matrix x, Vector y, Vector attributeTypes) {
        this.init(x, y, attributeTypes);
    }

    public DataSet(Matrix x, Vector attributeTypes) {
        Vector y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);
        this.init(x, y, attributeTypes);
    }

    public DataSet(Matrix x, int attributeType) {
        Vector y = x.col(x.colCount()-1);
        x.dropCol(x.colCount()-1);
        this.init(x, y, attributeType);

    }

    public DataSet(Matrix x, Vector y, int attributeType) {
        this.init(x, y, attributeType);
    }

    public DataSet(Vector attributeTypes) {
        this.init(new Matrix(), new Vector(), attributeTypes);
    }

    private void init(Matrix x, Vector y, int attributeType) {
        Vector dataTypes = new Vector(new int[x.colCount()]);
        dataTypes.fill(attributeType);
        this.attributeTypes = dataTypes;
        this.init(x, y, attributeTypes);
    }

    private void init(Matrix x, Vector y, Vector attributeTypes) {
        if(x.colCount() != 0 && x.colCount() != attributeTypes.length()) {
            LOG.error("length mismatch: attributes: {}, attribute types: {}", x.colCount(), attributeTypes.length());
            throw new DataException("attribute type vector length must match attribute count");
        }

        if(x.rowCount() != y.length()) {
            throw new DataException("x row count and y length must match!");
        }

        this.entropy = -1;
        this.x = x;
        this.y = y;
        this.attributeTypes = attributeTypes;
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

    public Tuple<List<Integer>, List<DataSet>> splitByDiscreteAttribute(int attribute) {
        if(this.attributeType(attribute) != DISCRETE) {
            throw new AttributeException("must be discrete attribute");
        }

        List<Integer> values = new ArrayList<>();
        List<DataSet> subsets = new ArrayList<>();
        for (int i = 0; i < x.rowCount(); i++) {
            LOG.trace("checking row {}", i);
            int value = x.intAt(i, attribute);

            int index = values.indexOf(value);
            if (index < 0) {
                LOG.trace("adding new split based on value {}", value);
                values.add(value);
                subsets.add(new DataSet(attributeTypes.clone()));
                index = subsets.size()-1;
            }
            DataSet subset = subsets.get(index);

            Vector v = this.sample(i);
            subset.add(v);
        }
        return new Tuple<>(values, subsets);
    }

    public Tuple<Double, Tuple<DataSet, DataSet>> splitByContinuousAttribute(int attribute) {
        if(this.attributeType(attribute) != CONTINUOUS) {
            throw new DataException("must be continuous attribute");
        }

        Vector values = x.col(attribute);
        Vector classes = y.clone();
        classes.sortBasedOn(values);
        values.sort();

        LOG.debug("splitting with attribute -> {}", attribute);

        double bestPivot = -1;
        double minimumEntropy = -1;
        LOG.debug("{}", classes);
        LOG.debug("{}", values);
        for (int i = 0; i < values.length()-1; i++) {
            if(classes.intAt(i) == classes.intAt(i+1)) {
                LOG.debug("skipping as classes at({}) == at({}+1)", i, i);
                continue;
            }

            if(values.at(i) == values.at(i+1)) {
                LOG.debug("skipping as values at({}) == at({}+1)", i, i);
                continue;
            }

            double pivot = (values.at(i) + values.at(i+1)) / 2;
            LOG.debug("testing split at {}", pivot);

            Tuple<Vector, Vector> split = classes.splitBasedOn(values, pivot);
            LOG.debug("under: {}, over: {}", split.first(), split.last());

            double entropy = calculateWeightedEntropy(split.first(), split.last());
            if(minimumEntropy < 0 ||  entropy < minimumEntropy) {
                LOG.debug("best pivot is now {} with {} entropy", pivot, entropy);
                minimumEntropy = entropy;
                bestPivot = pivot;

            }
        }

        if(minimumEntropy < 0) {
            LOG.warn("no possible pivots found for {} -> {}", attribute, dataToString());
            throw new DataException("no possible places to split found");
        }

        LOG.debug("the best pivot is {}", bestPivot);
        Tuple<DataSet, DataSet> subsets = splitAt(attribute, bestPivot);
        return new Tuple<>(bestPivot, subsets);
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
        // Have we already calculated entropy?
        if(entropy > 0) {
            return entropy;
        }

        entropy = ca.jacob.jml.Util.entropy(this.classes());
        return entropy;
    }

    public void replaceClasses(Vector newClasses) {
        this.y = newClasses;
    }
}
