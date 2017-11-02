package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

public class ID3 implements Algorithm {
    public static final Integer MAX_LEVEL_NONE = -1;

    private Integer maxLevel;

    public ID3(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Model fit(Matrix x, Vector y) {
        Node root = new Node(x, y, 1, maxLevel);
        root.split();
        return new ID3Model(root);
    }
}
