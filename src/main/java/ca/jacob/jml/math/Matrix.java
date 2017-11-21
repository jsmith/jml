package ca.jacob.jml.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private static final Logger LOG = LoggerFactory.getLogger(Matrix.class);

    private List<Vector> data;

    public Matrix(double[][] data) {
        this.data = new ArrayList<Vector>();
        for(double[] row : data) {
            this.data.add(new Vector(row));
        }
    }

    public Matrix(int[][] data) {
        this.data = new ArrayList<Vector>();
        for(int[] row : data) {
            this.data.add(new Vector(row));
        }
    }

    public Matrix(String[][] data) {
        this.data = new ArrayList<Vector>();
        for(String[] row : data) {
            this.data.add(new Vector(row));
            LOG.debug("row from string: {}", this.row(this.rowCount()-1));
        }
    }

    public Matrix() {
        data = new ArrayList<Vector>();
    }

    public Vector row(int i) {
        return data.get(i).clone();
    }

    public Matrix rows(Vector indices) {
        Matrix m = new Matrix(new double[indices.length()][this.colCount()]);
        for(int i = 0; i < indices.length(); i++) {
            m.setRow(i, this.row(indices.intAt(i)));
        }
        return m;
    }

    public void pushRow(double[] row) {
        data.add(new Vector(row));
    }

    public void pushRow(Vector v) {
        this.pushRow(v.toArray());
    }

    public void setRow(int i, Vector v) {
        data.set(i, v);
    }

    public void pushCol(Vector col) {
        for(int j = 0; j < this.rowCount(); j++) {
            data.get(j).add(col.at(j));
        }
    }

    public void dropCol(int j) {
        for (int i = 0; i < this.rowCount(); i++) {
            data.get(i).remove(j);
        }
    }

    public Vector col(int j) {
        Vector v = new Vector();
        for (int i = 0; i < this.rowCount(); i++) {
            v.add(data.get(i).at(j));
        }
        return v;
    }

    public double at(int i, int j) {
        return data.get(i).at(j);
    }

    public int rowCount() {
        return data.size();
    }

    public int colCount() {
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
        if (this.rowCount() != other.rowCount()) {
            return false;
        }
        if (this.colCount() != other.colCount()) {
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

    public void setCol(int col, Vector v) {
        if(v.length() != this.rowCount()) {
            throw new MathException("vector length must match matrix row count");
        }

        for(int i = 0; i < this.rowCount(); i++) {
            this.data.get(i).set(col, v.at(i));
        }
    }

    public int intAt(int i, int j) {
        return (int) this.at(i, j);
    }

    public void swapCols(int i, int j) {
        Vector temp = this.col(i);
        this.setCol(i, this.col(j));
        this.setCol(j, temp);
    }
}
