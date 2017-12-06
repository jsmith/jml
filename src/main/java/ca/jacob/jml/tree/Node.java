package ca.jacob.jml.tree;

import ca.jacob.jml.DataSet;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.exceptions.PredictionException;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.jml.DataSet.CONTINUOUS;
import static ca.jacob.jml.DataSet.DISCRETE;
import static ca.jacob.jml.Util.calculateWeightedEntropy;

public class Node {
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private Node parent;
    private int attribute;
    private boolean leaf;
    private DataSet dataSet;
    private Children children;
    private int level;
    private int maxLevel;
    private int minNumberOfSamples;
    private double entropy;

    public Node(DataSet dataSet, int maxLevel, int minNumberOfSamples) {
        this.dataSet = dataSet;
        this.parent = null;
        this.init(0, maxLevel, minNumberOfSamples);
    }

    public Node(Matrix data, Vector attributeTypes, int maxLevel, int minNumberOfSamples) {
        this.dataSet = new DataSet(data, attributeTypes);
        this.parent = null;
        this.init(0, maxLevel, minNumberOfSamples);
    }

    public Node(DataSet dataSet, Node parent) {
        this.dataSet = dataSet;
        this.parent = parent;
        this.init(parent.level+1, parent.maxLevel, parent.minNumberOfSamples);
    }

    public void init(int level, int maxLevel, int minNumberOfSamples) {
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.attribute = -1;
        this.entropy = -1;
    }

    public double entropy() {
        if(this.entropy < 0) {
            this.entropy = dataSet.entropy();
        }
        return entropy;
    }

    public void split() {
        LOG.info("split - starting for level {}", level);
        LOG.debug("the total entropy of the current node is " + this.entropy());

        if(level == maxLevel || this.entropy() == 0 || this.sampleCount() <= 1 || this.sampleCount() < minNumberOfSamples) {
            LOG.debug("found leaf - level: {}, entropy: {}, numOfSamples: {}", this.level, this.entropy(), this.sampleCount());
            this.leaf = true;
            return;
        }

        int numOfAttributes = dataSet.attributeCount();

        double minEntropy = -1;
        int bestAttribute = -1;
        for(int j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {} of type {}", j, dataSet.attributeType(j));

            double entropy;
            if(dataSet.attributeType(j) == DISCRETE) {
                Map<Integer, DataSet> subsets = dataSet.splitByDiscreteAttribute(j);
                entropy = calculateWeightedEntropy(subsets.values());

            } else if(dataSet.attributeType(j) == CONTINUOUS) {
                Tuple<Double, Tuple<DataSet, DataSet>> subsets = dataSet.splitByContinuousAttribute(j);
                if(subsets == null) {
                    LOG.debug("no possible subsets for attribute {}", j);
                    continue;
                }
                entropy = calculateWeightedEntropy(subsets.last());
            } else {
                throw new AttributeException("unknown data type");
            }
            LOG.debug("the total entropy of the children when splitting on attribute {} is {}", j, entropy);

            if(bestAttribute < 0 || entropy < minEntropy) {
                LOG.trace("attribute {} is now the best attribute", j);
                minEntropy = entropy;
                bestAttribute = j;
            }
        }
        LOG.debug("the best attribute is {} for level {}", bestAttribute, level);

        if(bestAttribute < 0) {
            LOG.info("no possible subsets found -> {}", dataSet.dataToString());
            this.leaf = true;
            return;
        }

        if(this.entropy() <= minEntropy) {
            LOG.debug("children have {} entropy while current model has {} entropy! Creating a leaf!", minEntropy, this.entropy());
            this.leaf = true;
            return;
        }

        attribute = bestAttribute;

        if(dataSet.attributeType(bestAttribute) == CONTINUOUS) {
            Tuple<Double, Tuple<DataSet, DataSet>> subsets = dataSet.splitByContinuousAttribute(bestAttribute);
            subsets.last().first().dropAttribute(attribute);
            subsets.last().last().dropAttribute(attribute);
            LOG.debug("pivot is {}", subsets.first());
            children = new Children(this, subsets);
        } else if(dataSet.attributeType(bestAttribute) == DISCRETE) {
            Map<Integer, DataSet> subsets = dataSet.splitByDiscreteAttribute(bestAttribute);
            for(Map.Entry<Integer, DataSet> subset : subsets.entrySet()) {
                subset.getValue().dropAttribute(attribute);
            }
            children = new Children(this, subsets);
        }
        LOG.debug("there will be {} children", children.size());

        children.split();
    }

    public Children getChildren() {
        return children;
    }

    public int sampleCount() {
        return dataSet.sampleCount();
    }

    public int classify(Vector e) {
        LOG.trace("classify - starting for level {} and attribute {}", level, attribute);

        LOG.trace("sample attributes: {} vs data attributes: {}", e.length(), dataSet.attributeCount());
        if (this.leaf) {
            LOG.trace("a leaf was found, now classifying!");
            return this.predict();
        } else {
            try {
                return children.predict(e);
            } catch (PredictionException ex) {
                LOG.trace("children unable to predict sample");
                return this.predict();
            }
        }
    }

    public int predict() {
        LOG.trace("predicting starting on level {}", level);
        Vector classes = dataSet.classes();
        return (int)classes.valueOfMaxOccurrence();
    }

    public int getAttribute() {
        return attribute;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public int getAttributeType() {
        return dataSet.attributeType(attribute);
    }

    public int depth() {
        if(children == null || this.isLeaf()) {
            return 0;
        } else {
            int max = children.maxDepth();
            return 1 + max;
        }
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public int attributeCount() {
        return dataSet.attributeCount();
    }

    @Override
    public String toString() {
        if(parent == null) {
            return "\nRoot Node: attribute - >" + attribute;
        } else {
            return parent.toString() + "\nNode Level {}: attribute -> " + attribute;
        }
    }
}
