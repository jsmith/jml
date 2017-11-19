package ca.jacob.jml.dt;

import ca.jacob.jml.PredictionError;
import ca.jacob.jml.util.AttributeException;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Tuple;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;

public class Children {
    private static final Logger LOG = LoggerFactory.getLogger(Children.class);

    private Node parent;

    private double pivot;
    private Node under;
    private Node over;

    private Map<Integer, Node> discrete;

    public Children(Node parent) {
        if(!parent.isRoot() && parent.getAttributeType() != DISCRETE && parent.getAttributeType() != CONTINUOUS) {
            throw new AttributeException("attribute type cannot be " + parent.getAttributeType());
        }

        this.parent = parent;
    }

    public Children(Node parent, Tuple<Double, Tuple<DataSet, DataSet>> subsets) {
        if(parent.getAttributeType() != CONTINUOUS) {
            throw new AttributeException();
        }

        pivot = subsets.first();
        under = new Node(subsets.last().first(), parent);
        over = new Node(subsets.last().last(), parent);
        this.parent = parent;
    }

    public Children(Node parent, Map<Integer, DataSet> subsets) {
        if(parent.getAttributeType() != DISCRETE) {
            throw new AttributeException();
        }

        this.discrete = new HashMap<>();
        for(Map.Entry<Integer, DataSet> subset : subsets.entrySet()) {
            Node child = new Node(subset.getValue(), parent);
            this.discrete.put(subset.getKey(), child);
        }
        this.parent = parent;
    }

    public int size() {
        if(parent.getAttributeType() == DISCRETE) {
            return discrete.size();
        } else if(parent.getAttributeType() == CONTINUOUS) {
            return 2;
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }

    public void split() {
        if(parent.getAttributeType() == DISCRETE) {
            for(Node child : discrete.values()) {
                LOG.debug("child: {} x {}", child.sampleCount(), child.attributeCount());
                child.split();
            }
        } else if(parent.getAttributeType() == CONTINUOUS) {
            under.split();
            over.split();
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }

    public int predict(Vector e) {
        if(parent.getAttributeType() == DISCRETE) {
            for (Map.Entry<Integer, Node> entry : discrete.entrySet()) {
                if (e.intAt(parent.getAttribute()) == entry.getKey()) {
                    return entry.getValue().classify(e);
                }
            }
        } else if(parent.getAttributeType() == CONTINUOUS) {
            if (e.intAt(parent.getAttribute()) < pivot) {
                return under.classify(e);
            } else {
                return over.classify(e);
            }
        } else {
            throw new AttributeException("unknown attribute type");
        }

        throw new PredictionError("unable to predict");
    }

    public int maxDepth() {
        if(parent.getAttributeType() == DISCRETE) {
            int max = 0;
            for(Node child : discrete.values()) {
                int depth = child.depth();
                if(depth > max) {
                    max = depth;
                }
            }
            return max;
        } else if(parent.getAttributeType() == CONTINUOUS) {
            int d1 = under.depth();
            int d2 = over.depth();
            return d1 > d2 ? d2 : d1;
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }

    public Node get(int i) {
        if(parent.getAttributeType() == DISCRETE) {
            return new ArrayList<>(discrete.values()).get(i);
        } else if(parent.getAttributeType() == CONTINUOUS) {
            if(i == 0) {
                return under;
            } else if(i == 1) {
                return over;
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
        } else {
            throw new AttributeException("unknown attribute type");
        }
    }

    public void put(int value, Node n) {
        if(parent.getAttributeType() != DISCRETE) {
            throw new AttributeException("attribute type must be discrete");
        }

        if(discrete == null) {
            discrete = new HashMap<>();
        }

        discrete.put(value, n);
    }
}
