package ca.jacob.jml.tree;

import ca.jacob.jml.DataSet;
import ca.jacob.jml.math.Vector;

import java.util.List;


public abstract class Children {
    Node parent;

    public Children(Node parent) {
        this.parent = parent;
    }

    abstract public int size();
    abstract public void split(List<DataSet> dataSets);
    abstract public int predict(Vector e);
    abstract public int maxDepth();
    abstract public Node get(int i);
}
