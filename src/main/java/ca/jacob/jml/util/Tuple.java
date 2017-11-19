package ca.jacob.jml.util;

public class Tuple<T, V> {
    private final T t;
    private final V v;

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
