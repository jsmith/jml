package ca.jacob.cs6735.algorithms.dt;

public class ID3 {
    private boolean trained;
    private Node root;

    public ID3(Integer[][] x, Integer[] y) {
        root = new Node(x, y, 1);
        trained = false;
    }

    public void train() {
        if(!trained) {
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
