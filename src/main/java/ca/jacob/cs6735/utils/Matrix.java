package ca.jacob.cs6735.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.DoubleStream;

public class Matrix {
    private static final Logger LOG = LoggerFactory.getLogger(Matrix.class);

    private Double[][] data;

    public Matrix(Double[][] data) {
        this.data = data;
    }

    public Matrix(Integer[][] data) {
        this.data = new Double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                this.data[i][j] = data[i][j].doubleValue();
            }
        }
    }

    public Matrix(String[][] data) {
        this.data = new Double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                this.data[i][j] = Double.parseDouble(data[i][j]);
            }
        }
    }

    public Matrix() {
        this.data = new Double[0][0];
    }

    public Double[] row(Integer i) {
        return data[i];
    }

    public void pushRow(Double[] row) {
        Double[][] tmp = new Double[this.rowCount() + 1][row.length];
        System.arraycopy(data, 0, tmp, 0, this.rowCount());
        for(Integer j = 0; j < row.length; j++) {
            tmp[this.rowCount()][j] = row[j];
        }
        data = tmp;
    }

    public void pushRow(Integer[] row) {
        Double[][] tmp = new Double[this.rowCount() + 1][this.colCount()];
        System.arraycopy(data, 0, tmp, 0, this.rowCount());
        for(Integer j = 0; j < data[0].length; j++) {
            tmp[this.rowCount()][j] = row[j].doubleValue();
        }
        data = tmp;
    }

    public void pushRow(Vector v) {
        this.pushRow(v.toArray());
    }

    public void pushCol(Integer[] col) {
        Double[][] tmp = new Double[this.rowCount()][this.colCount() + 1];
        for (Integer i = 0; i < this.rowCount(); i++) {
            System.arraycopy(data[i], 0, tmp[i], 0, this.colCount());
            tmp[i][this.colCount()] = col[i].doubleValue();
        }
        data = tmp;
    }

    public void dropCol(int j) {
        Double[][] tmp = new Double[this.rowCount()][this.colCount() - 1];
        for (Integer i = 0; i < this.rowCount(); i++) {
            System.arraycopy(data[i], 0, tmp[i], 0, j);
            System.arraycopy(data[i], j + 1, tmp[i], j, data[i].length - 1 - j);
        }
        data = tmp;
    }

    public Double[] col(Integer j) {
        Double[] col = new Double[data.length];
        for (Integer i = 0; i < data.length; i++) {
            col[i] = data[i][j];
        }
        return col;
    }

    public Integer[] colAsInts(Integer j) {
        Integer[] col = new Integer[data.length];
        for (Integer i = 0; i < data.length; i++) {
            col[i] = data[i][j].intValue();
        }
        return col;
    }

    public Integer[][] toIntArray() {
        Integer[][] arr = new Integer[data.length][data[0].length];
        for (Integer i = 0; i < data.length; i++) {
            for (Integer j = 0; j < data[0].length; j++) {
                arr[i][j] = data[i][j].intValue();
            }
        }
        return arr;
    }

    public Double at(Integer i, Integer j) {
        return data[i][j];
    }

    public Integer rowCount() {
        return data.length;
    }

    public Integer colCount() {
        if (data.length == 0) {
            return 0;
        }

        return data[0].length;
    }

}
