package ca.jacob.jml.tree;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.jml.Dataset.CONTINUOUS;

public class ContinuousChildren extends Children {
    private static final Logger LOG = LoggerFactory.getLogger(ContinuousChildren.class);

    private double pivot;
    private Node under;
    private Node over;

    ContinuousChildren(Node parent, double pivot) {
        super(parent);
        if(parent.getAttributeType() != CONTINUOUS) {
            throw new AttributeException();
        }

        this.pivot = pivot;
        under = new Node(parent);
        over = new Node(parent);
        this.parent = parent;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public void split(List<Dataset> subsets) {
        if(subsets.size() != 2) {
            throw new DataException("There must be 2 datasets");
        }

        under.split(subsets.get(0));
        over.split(subsets.get(1));
    }

    @Override
    public int predict(Vector e) {
        int attribute = parent.getAttribute();
        LOG.debug("instance {} vs pivot {} for attribute {}", e.at(attribute), pivot, attribute);
        if (e.at(attribute) < pivot) {
            e = e.clone();
            e.remove(attribute);
            return under.classify(e);
        } else {
            e = e.clone();
            e.remove(attribute);
            return over.classify(e);
        }
    }

    @Override
    public int maxDepth() {
        int d1 = under.getLevel();
        int d2 = over.getLevel();
        return d1 > d2 ? d2 : d1;
    }

    @Override
    public Node get(int i) {
        if(i == 0) {
            return under;
        } else if(i == 1) {
            return over;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public double getPivot() {
        return pivot;
    }
}
