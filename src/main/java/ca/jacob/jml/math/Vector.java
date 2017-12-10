package ca.jacob.jml.math;

import ca.jacob.jml.exceptions.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.jml.Util.arrayAsList;
import static ca.jacob.jml.Util.calculateOccurrences;
import static java.lang.Math.sqrt;

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
                try {
                    this.data.add(Double.parseDouble(data[i]));
                } catch (NumberFormatException e) {
                    throw new DataException("data must all be integers or doubles, not " + data[i]);
                }
            }
    }

    public Vector(List<Double> data) {
        this.data = data;
    }

    public Vector(Vector vector) {
        this.data = new ArrayList<>(vector.data);
    }

    public Vector(Collection<Double> values) {
        this.data = new ArrayList<>(values);
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

    public int valueOfMaxOccurrence() {
        Map<Integer, Integer> occurrences = calculateOccurrences(this);
        LOG.debug("occurrences: {}", occurrences);
        boolean first = true;
        int valueOfMaxOccurrence = 0;
        for (Map.Entry<Integer, Integer> e : occurrences.entrySet()) {
            if(first) {
                first = false;
                valueOfMaxOccurrence = e.getKey();
            } else if (e.getValue() > occurrences.get(valueOfMaxOccurrence)) {
                valueOfMaxOccurrence = e.getKey();
            }
        }
        return valueOfMaxOccurrence;
    }

    public void fill(double value) {
        Collections.fill(data, value);
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
        return new Vector(this);
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

    public Vector replace(int one, int two) {
        Vector v = this.clone();
        for(int i = 0; i < this.length();  i++) {
            if(v.at(i) == one) {
                v.set(i, two);
            }
        }
        return v;
    }

    public boolean contains(int num) {
        for(int i = 0; i < data.size(); i++) {
            if(this.intAt(i) == num) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(double num) {
        for(int i = 0; i < data.size(); i++) {
            if(this.at(i) == num) {
                return true;
            }
        }
        return false;
    }

    public Vector sub(double mean) {
        Vector v = new Vector(new double[this.length()]);
        for(int i = 0; i < length(); i++) {
            v.set(i, this.at(i) - mean);
        }
        return v;
    }

    public Vector pow(int n) {
        Vector v = new Vector(new double[this.length()]);
        for(int i = 0; i < length(); i++) {
            v.set(i, java.lang.Math.pow(this.at(i), n));
        }
        return v;
    }

    public double prod() {
        double sum = 1;
        for(Double d : this) {
            sum *= d;
        }
        return sum;
    }

    public double mean() {
        return this.sum() / this.length();
    }

    public int count(double value) {
        int count = 0;
        for(Double d : this.data) {
            if(d.doubleValue() == value) {
                count++;
            }
        }
        return count;
    }

    public double stdev() {
        if(this.length() == 1) {
            return 0;
        }

        double mean = this.mean();
        double variance = this.sub(mean).pow(2).sum() / (this.length() - 1);
        return sqrt(variance);
    }

    public Vector sub(Vector other) {
        Vector v = new Vector(new double[other.length()]);
        for(int i = 0; i < other.length(); i++) {
            v.set(i, this.at(i)-other.at(i));
        }
        return v;
    }

    public void sort() {
        Collections.sort(data);
    }

    public Vector unique() {
        Vector unique = new Vector();
        for(double i : this) {
            if(!unique.contains(i)) {
                unique.add(i);
            }
        }
        return unique;
    }

    public Vector add(Vector vector) {
        Vector v = new Vector(new double[this.length()]);
        for(int i = 0; i < length(); i++) {
            v.set(i, this.at(i)+vector.at(i));
        }
        return v;
    }

    public void sort(Vector basedOn) {
        Map<Double, Double> pairs = new TreeMap<>();
        for(int i = 0; i < basedOn.length(); i++) {
            pairs.put(basedOn.at(i), this.at(i));
        }

        this.data = new ArrayList<>(pairs.values());
    }
}
