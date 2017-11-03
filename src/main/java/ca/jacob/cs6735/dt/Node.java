package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.cs6735.util.Math.calculateOccurrences;
import static ca.jacob.cs6735.util.Math.log2;

public class Node {
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private double entropy;
    private int attribute;
    private boolean leaf;
    private Matrix data;
    private Map<Integer, Node> children;
    private int level;
    private int maxLevel;
    private int minNumberOfSamples;

    public Node(Matrix x, Vector y, int level, int maxLevel, int minNumberOfSamples) {
        this.data = x;
        this.data.pushCol(y);
        this.init(level, maxLevel, minNumberOfSamples);
    }

    public Node(int[][] data, int level, int maxLevel, int minNumberOfSamples) {
        this.data = new Matrix(data);
        this.init(level, maxLevel, minNumberOfSamples);
    }

    public Node(Matrix data, int level, int maxLevel, int minNumberOfSamples) {
        this.data = data;
        this.init(level, maxLevel, minNumberOfSamples);
    }

    private void init(int level, int maxLevel, int minNumberOfSamples) {
        children = new HashMap<Integer, Node>();
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.entropy = -1;
    }

    public double entropy() {
        if (this.entropy >= 0) return entropy;

        Map<Double, Integer> classes = calculateOccurrences(data.col(data.colCount() - 1));
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

        if(level == maxLevel || this.entropy() == 0 || this.entryCount() <= 1 || this.entryCount() < minNumberOfSamples) {
            LOG.info("found leaf - level: {}, entropy: {}, numOfSamples: {}", this.level, this.entropy(), this.entryCount());
            this.leaf = true;
            return;
        }

        int numOfAttributes = data.colCount() - 1;

        double minEntropy = -1;
        for(int j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {}", j);
            Map<Integer, Matrix> split = new HashMap<Integer, Matrix>();
            for (int i = 0; i < data.rowCount(); i++) {
                LOG.trace("checking row {}", i);
                int value = (int)data.at(i, j);

                Matrix entry = split.get(value);
                if (entry == null) {
                    LOG.trace("adding test node for value {}", value);
                    entry = new Matrix();
                    split.put(value, entry);
                }

                Vector v = data.row(i);
                v.remove(j);
                entry.pushRow(v);
            }

            double entropy = 0.;
            for (Map.Entry<Integer, Matrix> entry : split.entrySet()) {
                entropy += new Node(entry.getValue(), level + 1, maxLevel, minNumberOfSamples).entropy();
            }
            LOG.debug("the total entropy of the children when splitting on attribute {} is {}", j, entropy);

            if (minEntropy < 0 || entropy < minEntropy) {
                LOG.trace("attribute {} is now the best attribute", j);

                minEntropy = entropy;
                attribute = j;
                children = new HashMap<Integer, Node>();
                for (Map.Entry<Integer, Matrix> entry : split.entrySet()) {
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

    public int entryCount() {
        return data.rowCount();
    }

    public int classify(Vector e) {
        LOG.info("predict - starting for level {} and attribute {}", level, attribute);
        if (this.leaf) {
            LOG.debug("a leaf was found, now classifying!");
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
        Vector v = data.col(data.colCount() - 1);
        return (int)v.valueOfMaxOccurrence();
    }

    public int getAttribute() {
        return attribute;
    }

    public Matrix getData() {
        return data;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void isLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public int depth() {
        if(children == null) {
            return 1;
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
