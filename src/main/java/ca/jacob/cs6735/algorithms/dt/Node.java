package ca.jacob.cs6735.algorithms.dt;

import ca.jacob.cs6735.utils.Matrix;
import ca.jacob.cs6735.utils.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.cs6735.utils.Math.calculateOccurrences;
import static ca.jacob.cs6735.utils.Math.log2;

public class Node {
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private Double entropy;
    private int predictor;
    private boolean leaf;
    private Matrix data;
    private Map<Integer, Node> nodes;
    private int level;

    public Node(Integer[][] x, Integer[] y, int level) {
        this.data = new Matrix(x);
        this.data.pushCol(y);
        this.init(level);
    }

    public Node(Integer[][] data, int level) {
        this.data = new Matrix(data);
        this.init(level);
    }

    public Node(Matrix data, int level) {
        this.data = data;
        this.init(level);
    }

    private void init(int level) {
        nodes = new HashMap<Integer, Node>();
        leaf = false;
        this.level = level;
    }

    public Double entropy() {
        LOG.info("entropy - starting");
        if (this.entropy != null) return entropy;

        Map<Integer, Integer> classes = calculateOccurrences(data.col(data.colCount() - 1));
        LOG.debug("there are {} classes", classes.size());

        double sum = 0;
        for (Integer count : classes.values()) {
            sum += count;
        }
        LOG.debug("sum is " + sum);

        entropy = 0.;
        for (Integer count : classes.values()) {
            entropy -= count / sum * log2(count / sum);
        }
        LOG.debug("the entropy is {}", entropy);

        return entropy;
    }

    public void split() {
        LOG.info("split - starting for level {}", level);
        int numOfAttributes = data.colCount() - 1;

        Double minEntropy = null;
        for (int j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {}", j);
            Map<Integer, Matrix> split = new HashMap<Integer, Matrix>();
            for (int i = 0; i < data.rowCount(); i++) {
                LOG.trace("checking row {}", i);
                Integer value = data.at(i, j);

                Matrix entry = split.get(value);
                if (entry == null) {
                    LOG.debug("entry for output {} not found", value);
                    entry = new Matrix();
                    split.put(value, entry);
                }

                Vector v = new Vector(data.row(i));
                v.drop(j);
                entry.pushRow(v);
            }

            Double entropy = 0.;
            for (Map.Entry<Integer, Matrix> entry : split.entrySet()) {
                entropy += new Node(entry.getValue(), level + 1).entropy();
            }
            LOG.debug("the total entropy of the child nodes for attribute {} is {}", j, entropy);

            if (minEntropy == null || entropy < minEntropy) {
                LOG.debug("attribute {} is the best attribute", j);

                minEntropy = entropy;
                predictor = j;
                nodes = new HashMap<Integer, Node>();
                for (Map.Entry<Integer, Matrix> entry : split.entrySet()) {
                    nodes.put(entry.getKey(), new Node(entry.getValue(), level + 1));
                }
            }
        }

        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            if (node.entropy() != 0 || node.entryCount() > 1) {
                node.split();
            } else {
                node.isLeaf(true);
            }
        }
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public int entryCount() {
        return data.rowCount();
    }

    public Integer predict(Integer[] e) {
        LOG.info("predict - starting for level {} and attribute {}", level, predictor);
        if (this.leaf) {
            LOG.debug("a leaf was found");
            Vector v = new Vector(data.col(data.colCount() - 1));
            Integer valueOfMaxOccurrance = v.valueOfMaxOccurrance();
            LOG.debug("value of max occurrance for vector {} is {}", v, valueOfMaxOccurrance);
            return valueOfMaxOccurrance;
        } else {
            for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
                if (e[predictor] == entry.getKey()) {
                    return entry.getValue().predict(e);
                }
            }
        }
        throw new ID3PredictionException("no predictor found for attribute " + predictor);
    }

    public int getPredictor() {
        return predictor;
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
}
