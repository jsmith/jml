package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.cs6735.util.Math.calculateOccurrences;

public class Vector implements Iterable<Double> {
    private static final Logger LOG = LoggerFactory.getLogger(Vector.class);

    private List<Double> data;

    public Vector(Integer[] data) {
        this.data = new ArrayList<Double>();
        for(Integer i = 0; i < data.length; i++) {
            this.data.add(data[i].doubleValue());
        }
    }

    public Vector() {
        this.data = new ArrayList<Double>();
    }

    public Vector(Double[] data) {
        this.data = Arrays.asList(data);
    }

    public Vector(String[] data) {
        this.data = new ArrayList<Double>();
        for(Integer i = 0; i < data.length; i++) {
            this.data.add(Double.parseDouble(data[i]));
        }
    }

    public void add(Integer value) {
        data.add(value.doubleValue());
    }

    public void add(Double value) {
        data.add(value);
    }

    public void remove(Integer i) {
        data.remove(i.intValue());
    }

    public Double[] toArray() {
        return data.toArray(new Double[data.size()]);
    }

    public Integer[] toIntegerArray() {
        Integer[] arr = new Integer[this.length()];
        for(Integer i = 0; i < this.length(); i++) {
            arr[i] = this.at(i).intValue();
        }
        return arr;
    }

    public Double at(Integer i) {
        return data.get(i);
    }

    public Double valueOfMaxOccurrance() {
        Map<Double, Integer> occurrances = calculateOccurrences(this);
        LOG.debug("occurances: {}", occurrances);
        Double valueOfMaxOccurrence = null;
        for (Map.Entry<Double, Integer> e : occurrances.entrySet()) {
            if (valueOfMaxOccurrence == null || e.getValue() > occurrances.get(valueOfMaxOccurrence)) {
                valueOfMaxOccurrence = e.getKey();
            }
        }
        return valueOfMaxOccurrence;
    }

    public void fill(Double value) {
        Collections.fill(data, value);
    }

    public Double dot(Integer[] other) {
        return dot(new Vector(other));
    }

    public Double dot(Vector other) {
        if(!this.length().equals(other.length())) {
            throw new MathException("vector lengths must match");
        }

        Double sum = 0.;
        for(Integer i = 0; i < this.length(); i++) {
            sum += this.at(i) * other.at(i);
        }

        return sum;
    }

    public Double sum() {
        Double sum = 0.;
        for(Integer i = 0; i < this.length(); i++) {
            sum += this.at(i);
        }
        return sum;
    }

    public Integer length() {
        return data.size();
    }

    public Vector mul(Double value) {
        Vector v = new Vector(new Double[this.length()]);
        for(Integer i = 0; i < length(); i++) {
            v.set(i, this.at(i) * value);
        }
        return v;
    }

    public Vector mul(Vector other) {
        if(!this.length().equals(other.length())) {
            throw new MathException("vector lengths must match");
        }

        Vector v = new Vector(new Double[this.length()]);
        for(Integer i = 0; i < this.length(); i++) {
            v.set(i, this.at(i) * other.at(i));
        }
        return v;
    }

    public Vector div(Double value) {
        Vector v = new Vector(new Double[this.length()]);
        for(Integer i = 0; i < length(); i++) {
            v.set(i, this.at(i) / value);
        }
        return v;
    }

    public void set(Integer i, Double value) {
        data.set(i, value);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Vector)) {
            return false;
        }

        Vector other = (Vector) object;
        if (!this.length().equals(other.length())) {
            return false;
        }

        for (int i = 0; i < this.length(); i++) {
            if(!this.at(i).equals(other.at(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {
            private Integer currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < length();
            }

            @Override
            public Double next() {
                return at(currentIndex++);
            }
        };
    }
}
