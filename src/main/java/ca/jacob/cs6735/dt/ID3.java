package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.util.Data;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;

public class ID3 implements Algorithm {
    public static final int MAX_LEVEL_NONE = -1;
    public static final int MIN_SAMPLES_NONE = Integer.MIN_VALUE;

    private int maxLevel;
    private int minNumberOfSamples;
    private String[] labels;

    public ID3(int maxLevel, int minNumberOfSamples) {
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
    }

    public ID3(int maxLevel) {
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = MIN_SAMPLES_NONE;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setMinNumberOfSamples(int minNumberOfSamples) {
        this.minNumberOfSamples = minNumberOfSamples;
    }

    public Model fit(Data data) {
        Node root = new Node(data, 1, maxLevel, minNumberOfSamples);
        root.split();
        return new ID3Model(root);
    }
}
