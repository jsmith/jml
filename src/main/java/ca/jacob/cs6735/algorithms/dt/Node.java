package ca.jacob.cs6735.algorithms.dt;

import ca.jacob.cs6735.utils.Matrix;
import ca.jacob.cs6735.utils.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.cs6735.utils.Matrix.calculateOccurances;
import static ca.jacob.cs6735.utils.Matrix.log2;

public class Node {
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private Double entropy;
    private Matrix data;
    private ArrayList<Node> nodes;

    public Node(Integer[][] x, Integer[] y) {
        this.data = new Matrix(x);
        this.data.pushCol(y);
        this.initNodes();
    }

    public Node(Integer[][] data) {
        this.data = new Matrix(data);
    }

    public Node(Matrix data) {
        this.data = data;
    }

    public void initNodes() {
        this.nodes = new ArrayList<Node>();
    }

    public Double entropy() {
        LOG.info("entropy - starting");
        if (this.entropy != null) return entropy;

        Map<Integer, Integer> classes = calculateOccurances(y);
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
        LOG.info("split - starting");
        int numOfAttributes = data.colCount() - 1;

        Double minEntropy = null;
        for (int j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {}", j);
            Map<Integer, Matrix> split = new HashMap<Integer, Matrix>();
            for (int i = 0; j < data.rowCount(); j++) {
                LOG.trace("checking row {}", i);
                Integer value = data.at(i, j);

                Matrix entry = split.get(value);
                if (entry == null) {
                    LOG.debug("entry for {} not found", value);
                    entry = new Matrix();
                }

                Vector v = new Vector(data.row(i));
                v.drop(j);
                entry.pushRow(v);
            }

            Double entropy = 0.;
            for (Matrix entry : split.values()) {
                entropy += new Node(entry).entropy();
            }
            LOG.debug("the total entropy of the child nodes for attribute {} is {}", j, entropy);

            if(minEntropy == null || entropy < minEntropy) {
                minEntropy = entropy;

                LOG.debug("attribute {} is the best attribute", j);
                nodes = new ArrayList<Node>();
                for (Matrix entry : split.values()) {
                    nodes.add(new Node(entry));
                }
            }
        }
    }


}
