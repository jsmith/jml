package ca.jacob.cs6735;

public interface Algorithm {
    public Model train(Integer[][] x, Integer[] y, Double[] weights);
}
