package ca.jacob.jml.tree;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.Model;
import ca.jacob.jml.Algorithm;

public class ID3 implements Algorithm {
    public static final int MAX_LEVEL_NONE = Integer.MAX_VALUE;
    public static final int MIN_SAMPLES_NONE = Integer.MIN_VALUE;
    public static final String NAME = "ID3";

    private int maxLevel;
    private int minNumberOfSamples;

    public ID3(int maxLevel, int minNumberOfSamples) {
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
    }

    public ID3(int maxLevel) {
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = MIN_SAMPLES_NONE;
    }

    public ID3() {
        this.maxLevel = MAX_LEVEL_NONE;
        this.minNumberOfSamples = MIN_SAMPLES_NONE;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public Model fit(Dataset dataset) {
        Node root = new Node(maxLevel, minNumberOfSamples);
        root.split(dataset);
        return new ID3Model(root);
    }

    @Override
    public String toString() {
        return NAME+"(maxLevel:"+maxLevel+", minNumOfSamples:"+minNumberOfSamples+")";
    }
}
