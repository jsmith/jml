package ca.jacob.cs6735.algorithms.dt;

public class ID3 {
    public static final int LEVEL_NONE = -1;

    private boolean trained;
    private Node root;

    public ID3(Integer[][] x, Integer[] y, int maxLevel) {
        root = new Node(x, y, 1, maxLevel);
        trained = false;
    }

    public void train() {
        if (!trained) {
            root.split();
            trained = true;
        }
    }

    public int predict(Integer[] e) {
        return root.predict(e);
    }

    public Node getRoot() {
        return root;
    }
}
