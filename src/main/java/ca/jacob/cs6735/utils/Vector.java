package ca.jacob.cs6735.utils;

import java.util.Map;

import static ca.jacob.cs6735.utils.Matrix.calculateOccurances;

public class Vector {
    private Integer[] data;

    public Vector(Integer[] values) {
        this.data = values;
    }

    public Vector() {
        this.data = new Integer[0];
    }

    public void push(Integer value) {
        Integer[] tmp = new Integer[data.length+1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
    }

    public void drop(int i) {
        Integer[] tmp = new Integer[data.length-1];
        System.arraycopy(data, 0, tmp, 0, i);
        System.arraycopy(data, i+1, tmp, i, data.length-1-i);
        data = tmp;
    }

    public Integer[] toArray() {
        return data;
    }

    public Integer at(int i) {
        return data[i];
    }

    public Integer valueOfMaxOccurrance() {
        Map<Integer, Integer> occurrances = calculateOccurances(data);
        Integer valueOfMaxOccurrence = null;
        for(Map.Entry<Integer, Integer> e : occurrances.entrySet()) {
            if(valueOfMaxOccurrence == null || e.getValue() > occurrances.get(valueOfMaxOccurrence)) {
                valueOfMaxOccurrence = e.getKey();
            }
        }
        return occurrances.get(valueOfMaxOccurrence);
    }
}
