package ca.jacob.jml.tree;

import ca.jacob.jml.Model;
import ca.jacob.jml.math.Vector;

public class ID3Model extends Model {
    private Node root;

    public ID3Model(Node root) {
        this.root = root;
    }

    @Override
    public int predict(Vector e) {
        return root.classify(e);
    }

    public Node getRoot() {
        return root;
    }

    public int depth() {
        return root.getLevel();
    }
}
