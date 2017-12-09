package ca.jacob.jml.tree;

import ca.jacob.jml.DataSet;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.exceptions.PredictionException;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.DataSet.DISCRETE;

public class DiscreteChildren extends Children {
    private static final Logger LOG = LoggerFactory.getLogger(DiscreteChildren.class);

    private Map<Integer, Node> nodes;

    DiscreteChildren(Node parent, List<Integer> values) {
        super(parent);
        if(parent.getAttributeType() != DISCRETE) {
            throw new AttributeException();
        }

        this.nodes = new HashMap<>();
        for(Integer value : values) {
            Node child = new Node(parent);
            this.nodes.put(value, child);
        }
        this.parent = parent;
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public void split(List<DataSet> subsets) {
        if(subsets.size() != nodes.size()) {
            throw new DataException("nodes and subsets sizes must match");
        }

        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).split(subsets.get(i));
        }
    }

    @Override
    public int predict(Vector e) {
        int attribute = parent.getAttribute();
        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            int value = entry.getKey();
            Node child = entry.getValue();
            if (e.intAt(attribute) == value) {
                e = e.clone();
                e.remove(attribute);
                return child.classify(e);
            }
        }

        throw new PredictionException("not corresponding child found");
    }

    @Override
    public int maxDepth() {
        int max = 0;
        for(Node child : nodes.values()) {
            int depth = child.depth();
            if(depth > max) {
                max = depth;
            }
        }
        return max;
    }

    @Override
    public Node get(int i) {
        return new ArrayList<>(nodes.values()).get(i);
    }

    public void put(int value, Node n) {
        if(nodes == null) {
            nodes = new HashMap<>();
        }

        nodes.put(value, n);
    }
}
