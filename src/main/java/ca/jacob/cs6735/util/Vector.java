package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.cs6735.util.ML.arrayAsList;
import static ca.jacob.cs6735.util.ML.toPrimitiveArray;
import static ca.jacob.cs6735.util.Math.calculateOccurrences;

public class Vector implements Iterable<Double> {
    private static final Logger LOG = LoggerFactory.getLogger(Vector.class);

    private List<Double> data;

    public Vector(int[] data) {
        this.data = new ArrayList<Double>(Arrays.asList(new Double[data.length]));
        for(int i = 0; i < data.length; i++) {
            this.data.set(i, (double)data[i]);
        }
    }

    public Vector() {
        this.data = new ArrayList<Double>();
    }

    public Vector(double[] data) {
        this.data = arrayAsList(data);
    }

    public Vector(String[] data) {
        this.data = new ArrayList<Double>();
        for(int i = 0; i < data.length; i++) {
            this.data.add(Double.parseDouble(data[i]));
        }
    }

    public Vector(List<Double> data) {
        this.data = data;
    }

    public void add(int value) {
        data.add((double)value);
    }

    public void add(double value) {
        data.add(value);
    }

    public void concat(Vector v) {
        this.data.addAll(v.getData());
    }

    public Vector subVector(int from, int to) {
        return new Vector(this.data.subList(from, to));
    }

    public void remove(int i) {
        data.remove(i);
    }

    public double[] toArray() {
        return toPrimitiveArray(data);
    }

    public int[] tointArray() {
        int[] arr = new int[this.length()];
        for(int i = 0; i < this.length(); i++) {
            arr[i] = this.intAt(i);
        }
        return arr;
    }

    public double at(int i) {
        return data.get(i);
    }

    public Vector at(Vector indices) {
        Vector v = new Vector(new double[indices.length()]);
        for(int i = 0; i < indices.length(); i++) {
            LOG.debug("setting index {} to {}", i, this.data.get(v.intAt(i)));
            v.set(i, this.data.get(indices.intAt(i)));
        }
        return v;
    }

    public double valueOfMaxOccurrence() {
        Map<Double, Integer> occurrences = calculateOccurrences(this);
        LOG.debug("occurances: {}", occurrences);
        double valueOfMaxOccurrence = -1;
        for (Map.Entry<Double, Integer> e : occurrences.entrySet()) {
            if (valueOfMaxOccurrence < 0 || e.getValue().intValue() > occurrences.get(valueOfMaxOccurrence).intValue()) {
                valueOfMaxOccurrence = e.getKey();
            }
        }
        return valueOfMaxOccurrence;
    }

    public void fill(double value) {
        Collections.fill(data, value);
    }

    public double dot(int[] other) {
        return dot(new Vector(other));
    }

    public double dot(Vector other) {
        if(this.length() != other.length()) {
            throw new MathException("vector lengths must match");
        }

        double sum = 0.;
        for(int i = 0; i < this.length(); i++) {
            sum += this.at(i) * other.at(i);
        }

        return sum;
    }

    public double sum() {
        double sum = 0.;
        for(int i = 0; i < this.length(); i++) {
            sum += this.at(i);
        }
        return sum;
    }

    public int length() {
        return data.size();
    }

    public Vector mul(double value) {
        Vector v = new Vector(new double[this.length()]);
        for(int i = 0; i < length(); i++) {
            v.set(i, this.at(i) * value);
        }
        return v;
    }

    public Vector mul(Vector other) {
        if(this.length() != other.length()) {
            throw new MathException("vector lengths must match");
        }

        Vector v = new Vector(new double[this.length()]);
        for(int i = 0; i < this.length(); i++) {
            v.set(i, this.at(i) * other.at(i));
        }
        return v;
    }

    public Vector div(double value) {
        Vector v = new Vector(new double[this.length()]);
        for(int i = 0; i < length(); i++) {
            v.set(i, this.at(i) / value);
        }
        return v;
    }

    public Vector clone() {
        return new Vector(this.toArray().clone());
    }

    public void set(int i, double value) {
        data.set(i, value);
    }

    public void set(int i, int value) {
        data.set(i, (double)value);
    }

    public List<Double> getData() {
        return data;
    }

    public void swap(int i, int j) {
        double tmp = data.get(i);
        data.set(j, data.get(i));
        data.set(i, tmp);
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
        if (this.length() != other.length()) {
            return false;
        }

        for (int i = 0; i < this.length(); i++) {
            if(this.at(i) != other.at(i)) {
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

    public int intAt(int i) {
        return (int)this.at(i);
    }

    public void replace(int one, int two) {
        for(int i = 0; i < this.length();  i++) {
            if(this.at(i) == one) {
                this.set(i, two);
            }
        }
    }

    public boolean contains(int num) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i) == num) {
                return true;
            }
        }
        return false;
    }
}
