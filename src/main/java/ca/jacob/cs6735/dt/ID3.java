package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.Algorithm;

public class ID3 implements Algorithm {
    public static final Integer LEVEL_NONE = -1;

    private Integer maxLevel;

    public ID3(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Model train(Integer[][] x, Integer[] y, Double[] weights) {
        Node root = new Node(x, y, 1, maxLevel);
        root.split();
        return new ID3Model(root);
    }
}
