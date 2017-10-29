package ca.jacob.cs6735.dt;

import ca.jacob.cs6735.Model;

public class ID3Model implements Model {
    private Node root;

    public ID3Model(Node root) {
        this.root = root;
    }

    @Override
    public Integer predict(Integer[] e) {
        return root.classify(e);
    }

    @Override
    public Integer[] predict(Integer[][] data) {
        Integer[] predictions = new Integer[data.length];
        for(Integer i = 0; i < data.length; i++) {
            predictions[i] = predict(data[i]);
        }
        return predictions;
    }
}
