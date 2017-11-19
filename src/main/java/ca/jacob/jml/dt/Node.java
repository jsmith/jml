package ca.jacob.jml.dt;

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

    private double entropy;
    private int attribute;
    private boolean leaf;
    private DataSet dataSet;
    private Map<Integer, Node> children;
    private int level;
    private int maxLevel;
    private int minNumberOfSamples;

    public Node(DataSet dataSet, int level, int maxLevel, int minNumberOfSamples) {
        this.dataSet = dataSet;
        this.init(level, maxLevel, minNumberOfSamples);
    }

    public Node(Matrix data, Vector attributeTypes, int level, int maxLevel, int minNumberOfSamples) {
        this.dataSet = new DataSet(data, attributeTypes);
        this.init(level, maxLevel, minNumberOfSamples);
    }

    public void init(int level, int maxLevel, int minNumberOfSamples) {
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.entropy = -1;
    }

    public double entropy() {
        if(this.entropy < 0) {
            this.entropy = calculateEntropy(dataSet);
        }

        return entropy;
    }

    public void split() {
        LOG.info("split - starting for level {}", level);

        if(level == (maxLevel+1) || this.entropy() == 0 || this.sampleCount() <= 1 || this.sampleCount() < minNumberOfSamples) {
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
                Tuple<Double, List<DataSet>> subsets = dataSet.splitByContinuousAttribute(j);
                entropy = calculateEntropy(subsets.last());

            } else {
                throw new DataException("unknown data type");
            }
            LOG.debug("the total entropy of the children when splitting on attribute {} is {}", j, entropy);

            if(bestAttribute < 0 || entropy < minEntropy) {
                LOG.trace("attribute {} is now the best attribute", j);
                minEntropy = entropy;
                bestAttribute = j;
            }
        }

        /*
        if(dataSet.attributeType(bestAttribute) == CONTINUOUS) {
            discreteChildren = new HashMap<Integer, Node>();
            for (Map.Entry<Integer, DataSet> subset : subsets.entrySet()) {
                //children.put(subset.getKey(), new Node(subset.getValue(), level + 1, maxLevel, minNumberOfSamples));
            }
        } else if(dataSet.attributeType(bestAttribute) == DISCRETE) {
            continuousChildren = new Tuple<>()
        }


        LOG.debug("the best attribute is {} for level {}", attribute, level);
        LOG.debug("there will be {} children", children.size());

        for (Map.Entry<Integer, Node> entry : children.entrySet()) {
            Node node = entry.getValue();
            node.split();
        }*/
    }

    public Map<Integer, Node> getChildren() {
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
            for (Map.Entry<Integer, Node> entry : children.entrySet()) {
                if (e.intAt(attribute)== entry.getKey()) {
                    return entry.getValue().classify(e);
                }
            }
            LOG.debug("no suitable child node found");
            return this.predict(e);
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

    public int depth() {
        if(children == null || this.isLeaf()) {
            return 0;
        } else {
            int max = 0;
            for(Map.Entry<Integer, Node> entry : children.entrySet()) {
                int depth = entry.getValue().depth();
                if(depth > max) {
                    max = depth;
                }
            }
            return 1 + max;
        }
    }

    public void setChildren(Map<Integer, Node> children) {
        this.children = children;
    }
}
