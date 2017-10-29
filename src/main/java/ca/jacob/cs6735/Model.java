package ca.jacob.cs6735;

public interface Model {
    public Integer predict(Integer[] e);
    public Integer[] predict(Integer[][] data);
}
