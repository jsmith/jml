package ca.jacob.cs6735.algorithms.dt;

public class ID3 {
  private Node root;

  public ID3(Integer[][] x, Integer[] y) {
    root = new Node(x, y, 1);
  }

  public void train() {
      root.split();
  }

  public int predict(Integer[] entry) {
      return root.predict(entry);
  }
}
