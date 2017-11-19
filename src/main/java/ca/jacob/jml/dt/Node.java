package ca.jacob.jml.dt;

import ca.jacob.jml.PredictionError;
import ca.jacob.jml.util.*;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static ca.jacob.jml.util.ML.calculateEntropy;

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

    public Node(DataSet dataSet, int maxLevel, int minNumberOfSamples) {
        this.dataSet = dataSet;
        this.parent = null;
        this.init(0, maxLevel, minNumberOfSamples);
    }

    public Node(DataSet dataSet, Node parent) {
        this.dataSet = dataSet;
        this.parent = parent;
        this.init(parent.level+1, parent.maxLevel, parent.minNumberOfSamples);
    }

    public Node(Matrix data, Vector attributeTypes, int maxLevel, int minNumberOfSamples) {
        this.dataSet = new DataSet(data, attributeTypes);
        this.parent = null;
        this.init(0, maxLevel, minNumberOfSamples);
    }

    public void init(int level, int maxLevel, int minNumberOfSamples) {
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.attribute = -1;
    }

    public double entropy() {
        return dataSet.entropy();
    }

    public void split() {
        LOG.info("split - starting for level {}", level);

        if(level == maxLevel || this.entropy() == 0 || this.sampleCount() <= 1 || this.sampleCount() < minNumberOfSamples || (parent != null && parent.entropy() <= this.entropy())) {
            LOG.info("found leaf - level: {}, entropy: {}, numOfSamples: {}", this.level, this.entropy(), this.sampleCount());
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
                entropy = calculateEntropy(subsets.values());

            } else if(dataSet.attributeType(j) == CONTINUOUS) {
                Tuple<Double, Tuple<DataSet, DataSet>> subsets = dataSet.splitByContinuousAttribute(j);
                entropy = calculateEntropy(subsets.last());

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

        attribute = bestAttribute;
        LOG.debug("the best attribute is {} for level {}", bestAttribute, level);

        if(dataSet.attributeType(bestAttribute) == CONTINUOUS) {
            children = new Children(this, dataSet.splitByContinuousAttribute(bestAttribute));
        } else if(dataSet.attributeType(bestAttribute) == DISCRETE) {
            children = new Children(this, dataSet.splitByDiscreteAttribute(bestAttribute));
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
        if (this.leaf) {
            LOG.trace("a leaf was found, now classifying!");
            return this.predict(e);
        } else {
            try {
                return children.predict(e);
            } catch (PredictionError ex) {
                LOG.trace("children unable to predict sample");
                return this.predict(e);
            }
        }
    }

    public int predict(Vector e) {
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

    public void isLeaf(boolean leaf) {
        this.leaf = leaf;
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
}
