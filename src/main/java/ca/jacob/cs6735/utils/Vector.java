package ca.jacob.cs6735.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.DoubleStream;

import static ca.jacob.cs6735.utils.Math.calculateOccurrences;

public class Vector {
    private static final Logger LOG = LoggerFactory.getLogger(Vector.class);

    private Double[] data;

    public Vector(Integer[] data) {
        this.data = new Double[data.length];
        for(Integer i = 0; i < data.length; i++) {
            this.data[i] = data[i].doubleValue();
        }
    }

    public Vector() {}

    public Vector(Double[] data) {
        this.data = data;
    }

    public void push(Integer value) {
        Double[] tmp = new Double[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        tmp[tmp.length-1] = value.doubleValue();
        data = tmp;
    }

    public void drop(Integer i) {
        Double[] tmp = new Double[data.length - 1];
        System.arraycopy(data, 0, tmp, 0, i);
        System.arraycopy(data, i + 1, tmp, i, data.length - 1 - i);
        data = tmp;
    }

    public Double[] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    public Double at(Integer i) {
        return data[i];
    }

    public Double valueOfMaxOccurrance() {
        Map<Double, Integer> occurrances = calculateOccurrences(data);
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
        Arrays.fill(this.data, value);
    }

    public Double dot(Integer[] other) {
        return dot(new Vector(other));
    }

    public Double dot(Vector other) {
        if(this.length() != other.length()) {
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
        return data.length;
    }

    public Vector mul(Double value) {
        Double[] data = new Double[this.data.length];
        for(Integer i = 0; i < data.length; i++) {
            data[i] = this.data[i] * value;
        }
        return new Vector(data);
    }

    public Vector mul(Vector other) {
        Double[] data = new Double[this.data.length];
        for(Integer i = 0; i < data.length; i++) {
            data[i] = this.at(i) * other.at(i);
        }
        return new Vector(data);
    }

    public Vector div(Double value) {
        Double[] data = new Double[this.data.length];
        for(Integer i = 0; i < data.length; i++) {
            data[i] = this.data[i] / value;
        }
        return new Vector(data);
    }

    public String toString() {
        return Arrays.toString(data);
    }

}
