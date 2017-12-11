package ca.jacob.jml.tree;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.exceptions.PredictionException;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

import static ca.jacob.jml.Dataset.DISCRETE;

public class DiscreteChildren extends Children {
    private static final Logger LOG = LoggerFactory.getLogger(DiscreteChildren.class);

    private List<Tuple<Integer, Node>> nodes;

    public DiscreteChildren(Node parent, List<Integer> values) {
        super(parent);
        if(parent.getAttributeType() != DISCRETE) {
            throw new AttributeException();
        }

        this.nodes = new ArrayList<>();
        for(Integer value : values) {
            this.nodes.add(new Tuple<>(value, new Node(parent)));
        }
        this.parent = parent;
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public void split(List<Dataset> subsets) {
        if(subsets.size() != nodes.size()) {
            throw new DataException("nodes and subsets sizes must match");
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i).last();
            node.split(subsets.get(i));
        }
    }

    @Override
    public int predict(Vector e) {
        int attribute = parent.getAttribute();
        for (Tuple<Integer, Node> entry : nodes) {
            int value = entry.first();
            Node child = entry.last();
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
        for(Tuple<Integer, Node> child : nodes) {
            int depth = child.last().depth();
            if(depth > max) {
                max = depth;
            }
        }
        return max;
    }

    @Override
    public Node get(int i) {
        return nodes.get(i).last();
    }

    public void put(int value, Node n) {
        if(nodes == null) {
            nodes = new ArrayList<>();
        }

        nodes.add(new Tuple<>(value, n));
    }

    int index = 0;

    @Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
            private Integer index = 0;

            @Override
            public boolean hasNext() {
                return index < nodes.size();
            }

            @Override
            public Node next() {
                return get(index++);
            }
        };
    }
}
