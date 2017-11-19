package ca.jacob.jml.dt;

import ca.jacob.jml.PredictionError;
import ca.jacob.jml.util.AttributeException;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Tuple;
import ca.jacob.jml.util.Vector;
import org.w3c.dom.Attr;

import java.util.HashMap;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static ca.jacob.jml.util.ML.calculateEntropy;

public class Children {
    private Node node;

    private double pivot;
    private Node under;
    private Node over;

    private Map<Integer, Node> discrete;

    public Children(Node node, Tuple<Double, Tuple<DataSet, DataSet>> subsets) {
        pivot = subsets.first();
        under = new Node(subsets.last().first(), node);
        over = new Node(subsets.last().last(), node);
    }

    public Children(Node node, Map<Integer, DataSet> subsets) {
        this.discrete = new HashMap<>();
        for(Map.Entry<Integer, DataSet> subset : subsets.entrySet()) {
            Node child = new Node(subset.getValue(), node);
            this.discrete.put(subset.getKey(), child);
        }
    }

    public int size() {
        if(node.getAttributeType() == DISCRETE) {
            return discrete.size();
        } else if(node.getAttributeType() == CONTINUOUS) {
            return 2;
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }

    public void split() {
        if(node.getAttributeType() == DISCRETE) {
            for(Node child : discrete.values()) {
                child.split();
            }
        } else if(node.getAttributeType() == CONTINUOUS) {
            under.split();
            over.split();
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }

    public int predict(Vector e) {
        if(node.getAttributeType() == DISCRETE) {
            for (Map.Entry<Integer, Node> entry : discrete.entrySet()) {
                if (e.intAt(node.getAttribute()) == entry.getKey()) {
                    return entry.getValue().classify(e);
                }
            }
        } else if(node.getAttributeType() == CONTINUOUS) {

        } else {
            throw new AttributeException("unknown attribute type");
        }

        throw new PredictionError("unable to predict");
    }

    public int maxDepth() {
        if(node.getAttributeType() == DISCRETE) {
            int max = 0;
            for(Node child : discrete.values()) {
                int depth = child.depth();
                if(depth > max) {
                    max = depth;
                }
            }
            return max;
        } else if(node.getAttributeType() == CONTINUOUS) {
            int d1 = under.depth();
            int d2 = over.depth();
            return d1 > d2 ? d2 : d1;
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }
}
