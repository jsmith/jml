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
    private Vector classes;
    private Vector attributeTypes;
    private Children children;
    private int level;
    private int maxLevel;
    private int minNumberOfSamples;

    public Node(int maxLevel, int minNumberOfSamples) {
        this.parent = null;
        this.init(0, maxLevel, minNumberOfSamples);
    }

    public Node(Node parent) {
        this.parent = parent;
        this.init(parent.level+1, parent.maxLevel, parent.minNumberOfSamples);
    }

    private void init(int level, int maxLevel, int minNumberOfSamples) {
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.attribute = -1;
    }

    public void split(DataSet dataSet) {
        LOG.info("split - starting for level {}", level);
        classes = dataSet.classes();
        attributeTypes = dataSet.getAttributeTypes();

        if(level == maxLevel || dataSet.entropy() == 0 || dataSet.sampleCount() <= 1 || dataSet.sampleCount() < minNumberOfSamples) {
            LOG.debug("found leaf - level: {}, entropy: {}, numOfSamples: {}", this.level, dataSet.entropy(), dataSet.sampleCount());
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
                Tuple<List<Integer>, List<DataSet>> subsets = dataSet.splitByDiscreteAttribute(j);
                entropy = calculateWeightedEntropy(subsets.last());

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

        if(dataSet.entropy() <= minEntropy) {
            LOG.debug("children have {} entropy while current model has {} entropy! Creating a leaf!", minEntropy, dataSet.entropy());
            this.leaf = true;
            return;
        }

        attribute = bestAttribute;

        List<DataSet> subsets = new ArrayList<>();
        if(dataSet.attributeType(bestAttribute) == CONTINUOUS) {
            Tuple<Double, Tuple<DataSet, DataSet>> split = dataSet.splitByContinuousAttribute(bestAttribute);
            LOG.debug("pivot is {}", split.first());

            DataSet under = split.last().first();
            DataSet over = split.last().last();
            under.dropAttribute(attribute);
            over.dropAttribute(attribute);
            subsets.add(under);
            subsets.add(over);

            children = new ContinuousChildren(this, split.first());
        } else if(dataSet.attributeType(bestAttribute) == DISCRETE) {
            Tuple<List<Integer>, List<DataSet>> split = dataSet.splitByDiscreteAttribute(bestAttribute);
            subsets = split.last();
            children = new DiscreteChildren(this, split.first());
        }
        LOG.debug("there will be {} children", children.size());

        children.split(subsets);
    }

    public Children getChildren() {
        return children;
    }

    int classify(Vector e) {
        LOG.trace("classify - starting for level {} and attribute {}", level, attribute);

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
        return classes.valueOfMaxOccurrence();
    }

    public int getAttribute() {
        return attribute;
    }

    public int sampleCount() {
        return classes.length();
    }

    public boolean isLeaf() {
        return leaf;
    }

    int getAttributeType() {
        return attributeTypes.intAt(attribute);
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

    boolean isRoot() {
        return parent == null;
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
