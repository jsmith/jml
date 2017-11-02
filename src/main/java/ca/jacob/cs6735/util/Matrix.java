package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Matrix {
    private static final Logger LOG = LoggerFactory.getLogger(Matrix.class);

    private List<Vector> data;

    public Matrix(Double[][] data) {
        this.data = new ArrayList<Vector>();
        for(Double[] row : data) {
            this.data.add(new Vector(row));
        }
    }

    public Matrix(Integer[][] data) {
        this.data = new ArrayList<Vector>();
        for(Integer[] row : data) {
            this.data.add(new Vector(row));
        }
    }

    public Matrix(String[][] data) {
        this.data = new ArrayList<Vector>();
        for(String[] row : data) {
            this.data.add(new Vector(row));
        }
    }

    public Matrix() {
        data = new ArrayList<Vector>();
    }

    public Vector row(Integer i) {
        return data.get(i).clone();
    }

    public Matrix rows(Vector indices) {
        Matrix m = new Matrix(new Double[indices.length()][this.colCount()]);
        for(int i = 0; i < indices.length(); i++) {
            m.setRow(i, this.row(indices.at(i).intValue()));
        }
        return m;
    }

    public void pushRow(Double[] row) {
        data.add(new Vector(row));
    }

    public void pushRow(Integer[] row) {
        data.add(new Vector(row));
    }

    public void pushRow(Vector v) {
        this.pushRow(v.toArray());
    }

    public void setRow(Integer i, Vector v) {
        data.set(i, v);
    }

    public void pushCol(Integer[] col) {
        for (int j = 0; j < this.rowCount(); j++) {
            data.get(j).add(col[j]);
        }
    }

    public void pushCol(Vector col) {
        for(int j = 0; j < this.rowCount(); j++) {
            data.get(j).add(col.at(j));
        }
    }

    public void dropCol(int j) {
        for (Integer i = 0; i < this.rowCount(); i++) {
            data.get(i).remove(j);
        }
    }

    public Vector col(Integer j) {
        Vector v = new Vector();
        for (Integer i = 0; i < this.rowCount(); i++) {
            v.add(data.get(i).at(j));
        }
        return v;
    }

    public Integer[][] toIntArray() {
        Integer[][] arr = new Integer[this.rowCount()][this.colCount()];
        for(Integer i = 0; i < this.rowCount(); i++) {
            arr[i] = data.get(i).toIntegerArray();
        }
        return arr;
    }

    public Double at(Integer i, Integer j) {
        return data.get(i).at(j);
    }

    public Integer rowCount() {
        return data.size();
    }

    public Integer colCount() {
        if (this.rowCount() == 0) {
            return 0;
        }
        return data.get(0).length();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Matrix)) {
            return false;
        }

        Matrix other = (Matrix) object;
        if (!this.rowCount().equals(other.rowCount())) {
            return false;
        }
        if (!this.colCount().equals(other.colCount())) {
            return false;
        }

        for (int i = 0; i < this.rowCount(); i++) {
            if(!this.row(i).equals(other.row(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
