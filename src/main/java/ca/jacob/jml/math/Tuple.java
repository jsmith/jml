package ca.jacob.jml.math;

import java.util.ArrayList;
import java.util.List;

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

    public List<T> asList() {
        List<T> list = new ArrayList<>();
        list.add(t);
        list.add((T)v);
        return list;
    }

    @Override
    public String toString() {
        return "("+t+", "+v+")";
    }
}
