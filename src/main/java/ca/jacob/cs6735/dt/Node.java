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

    public Node(Matrix x, Vector y, Integer level, Integer maxLevel, Integer minNumberOfSamples) {
        this.data = x;
        this.data.pushCol(y);
        this.init(level, maxLevel, minNumberOfSamples);
    }

    public Node(Integer[][] data, Integer level, Integer maxLevel, Integer minNumberOfSamples) {
        this.data = new Matrix(data);
        this.init(level, maxLevel, minNumberOfSamples);
    }

    public Node(Matrix data, Integer level, Integer maxLevel, Integer minNumberOfSamples) {
        this.data = data;
        this.init(level, maxLevel, minNumberOfSamples);
    }

    private void init(Integer level, Integer maxLevel, Integer minNumberOfSamples) {
        children = new HashMap<Integer, Node>();
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.entropy = -1;
    }

    public Double entropy() {
        if (this.entropy >= 0) return entropy;

        Map<Double, Integer> classes = calculateOccurrences(data.col(data.colCount() - 1));
        LOG.trace("there are {} potential values", classes.size());

        Double sum = 0.;
        for (Integer count : classes.values()) {
            sum += count;
        }
        LOG.trace("sum is " + sum);

        entropy = 0.;
        for (Integer count : classes.values()) {
            entropy -= count / sum * log2(count / sum);
        }
        LOG.trace("the entropy for a node on leve {} is {}", level, entropy);

        return entropy;
    }

    public void split() {
        LOG.info("split - starting for level {}", level);

        if(level.equals(maxLevel) || this.entropy().equals(0) || this.entryCount() <= 1 || this.entryCount() < minNumberOfSamples) {
            LOG.info("found leaf - level: {}, entropy: {}, numOfSamples: {}", this.level, this.entropy(), this.entryCount());
            this.leaf = true;
            return;
        }

        Integer numOfAttributes = data.colCount() - 1;

        Double minEntropy = null;
        for (Integer j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {}", j);
            Map<Integer, Matrix> split = new HashMap<Integer, Matrix>();
            for (Integer i = 0; i < data.rowCount(); i++) {
                LOG.trace("checking row {}", i);
                Integer value = data.at(i, j).intValue();

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

            Double entropy = 0.;
            for (Map.Entry<Integer, Matrix> entry : split.entrySet()) {
                entropy += new Node(entry.getValue(), level + 1, maxLevel, minNumberOfSamples).entropy();
            }
            LOG.debug("the total entropy of the children when splitting on attribute {} is {}", j, entropy);

            if (minEntropy == null || entropy < minEntropy) {
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

    public Integer entryCount() {
        return data.rowCount();
    }

    public Integer classify(Vector e) {
        LOG.info("predict - starting for level {} and attribute {}", level, attribute);
        if (this.leaf) {
            LOG.debug("a leaf was found");
            return this.predict(e);
        } else {
            for (Map.Entry<Integer, Node> entry : children.entrySet()) {
                if (e.intAt(attribute).equals(entry.getKey())) {
                    return entry.getValue().classify(e);
                }
            }
            LOG.debug("no suitable child node found");
            return this.predict(e);
        }
    }

    public Integer predict(Vector e) {
        Vector v = data.col(data.colCount() - 1);
        Integer valueOfMaxOccurrance = v.valueOfMaxOccurrance().intValue();
        return valueOfMaxOccurrance;
    }

    public Integer getAttribute() {
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

    public Integer depth() {
        if(children == null) {
            return 1;
        } else {
            Integer max = 0;
            for(Map.Entry<Integer, Node> entry : children.entrySet()) {
                Integer depth = entry.getValue().depth();
                if(depth > max) {
                    max = depth;
                }
            }
            return 1 + max;
        }
    }
}
