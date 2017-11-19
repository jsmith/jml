package ca.jacob.jml.util;

public class Tuple<T, V> {
    private T t;
    private V v;

    public Tuple(T t, V v) {
        this.t = t;
        this.v = v;
    }

    public T first() {
        return t;
    }

    public V last() {
        return v;
    }
}
