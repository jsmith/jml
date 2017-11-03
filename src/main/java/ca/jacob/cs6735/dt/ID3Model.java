package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

public class ID3Model extends Model {
    private Node root;

    public ID3Model(Node root) {
        this.root = root;
    }

    @Override
    public int predict(Vector e) {
        return root.classify(e);
    }

    @Override
    public Vector predict(Matrix data) {
        Vector predictions = new Vector(new double[data.rowCount()]);
        for(int i = 0; i < data.rowCount(); i++) {
            predictions.set(i, predict(data.row(i)));
        }
        return predictions;
    }

    public Node getRoot() {
        return root;
    }

    public int depth() {
        return root.depth();
    }
}
