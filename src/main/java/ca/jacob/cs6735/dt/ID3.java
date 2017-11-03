package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

public class ID3 implements Algorithm {
    public static final Integer MAX_LEVEL_NONE = -1;
    public static final Integer MIN_SAMPLES_NONE = Integer.MAX_VALUE;

    private Integer maxLevel;
    private Integer minNumberOfSamples;

    public ID3(Integer maxLevel, Integer minNumberOfSamples) {
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
    }

    public ID3(Integer maxLevel) {
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = MIN_SAMPLES_NONE;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setMinNumberOfSamples(Integer minNumberOfSamples) {
        this.minNumberOfSamples = minNumberOfSamples;
    }

    public Model fit(Matrix x, Vector y) {
        Node root = new Node(x, y, 1, maxLevel, minNumberOfSamples);
        root.split();
        return new ID3Model(root);
    }
}
