package ca.jacob.jml.dt;

import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.jml.util.Math.calculateOccurrences;
import static ca.jacob.jml.util.Math.log2;

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
        children = new HashMap<Integer, Node>();
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.entropy = -1;
    }

    public double entropy() {
        if (this.entropy >= 0) return entropy;

        Map<Double, Integer> classes = calculateOccurrences(dataSet.classes());
        LOG.trace("there are {} potential values", classes.size());

        double sum = 0.;
        for (int count : classes.values()) {
            sum += count;
        }
        LOG.trace("sum is " + sum);

        entropy = 0.;
        for (int count : classes.values()) {
            entropy -= count / sum * log2(count / sum);
        }
        LOG.trace("the entropy for a node on leve {} is {}", level, entropy);

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
        for(int j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {}", j);
            Map<Integer, DataSet> split = new HashMap<Integer, DataSet>();
            for (int i = 0; i < dataSet.sampleCount(); i++) {
                LOG.trace("checking row {}", i);
                int value = (int) dataSet.at(i, j);

                DataSet subset = split.get(value);
                if (subset == null) {
                    LOG.trace("adding test node for value {}", value);
                    Vector attributeTypes = dataSet.getAttributeTypes();
                    attributeTypes.remove(j);
                    subset = new DataSet(attributeTypes);
                    split.put(value, subset);
                }

                Vector v = dataSet.sample(i);
                v.remove(j);
                subset.add(v);
            }

            double entropy = 0.;
            for (Map.Entry<Integer, DataSet> entry : split.entrySet()) {
                entropy += new Node(entry.getValue(), 0, 0, 0).entropy();
            }
            LOG.debug("the total entropy of the children when splitting on attribute {} is {}", j, entropy);

            if (minEntropy < 0 || entropy < minEntropy) {
                LOG.trace("attribute {} is now the best attribute", j);

                minEntropy = entropy;
                attribute = j;
                children = new HashMap<Integer, Node>();
                for (Map.Entry<Integer, DataSet> entry : split.entrySet()) {
                    children.put(entry.getKey(), new Node(entry.getValue(), level + 1, maxLevel, minNumberOfSamples));
                }
            }
        }
        LOG.debug("the best attribute is {} for level {}", attribute, level);
        LOG.debug("there will be {} children", children.size());

        for (Map.Entry<Integer, Node> entry : children.entrySet()) {
            Node node = entry.getValue();
            node.split();
        }
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
