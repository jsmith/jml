package ca.jacob.jml.tree;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.math.Vector;

import java.util.Iterator;
import java.util.List;


public abstract class Children implements Iterable<Node> {
    Node parent;

    public Children(Node parent) {
        this.parent = parent;
    }

    abstract public int size();
    abstract public void split(List<Dataset> datasets);
    abstract public int predict(Vector e);
    abstract public int maxDepth();
    abstract public Node get(int i);
}
