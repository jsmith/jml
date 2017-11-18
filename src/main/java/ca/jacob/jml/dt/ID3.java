package ca.jacob.jml.dt;

import ca.jacob.jml.Model;
import ca.jacob.jml.Algorithm;
import ca.jacob.jml.util.DataSet;

public class ID3 implements Algorithm {
    public static final int MAX_LEVEL_NONE = Integer.MAX_VALUE;
    public static final int MIN_SAMPLES_NONE = Integer.MIN_VALUE;
    public static final String NAME = "ID3";

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

    public Model fit(DataSet dataSet) {
        Node root = new Node(dataSet, 1, maxLevel, minNumberOfSamples);
        root.split();
        return new ID3Model(root);
    }

    @Override
    public String toString() {
        return NAME+"(maxLevel:"+maxLevel+", minNumOfSamples:"+minNumberOfSamples+")";
    }
}
