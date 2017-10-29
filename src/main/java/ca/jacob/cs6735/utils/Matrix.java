package ca.jacob.cs6735.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Matrix {
    private static final Logger LOG = LoggerFactory.getLogger(Matrix.class);

    private Double[][] data;

    public Matrix(Double[][] values) {
        this.data = values;
    }

    public Double[] row(Integer i) {
        return data[i];
    }

    public void pushRow(Integer[] row) {
        Double[][] tmp = new Double[this.rowCount() + 1][this.colCount()];
        System.arraycopy(data, 0, tmp, 0, this.rowCount());
        for(Integer j = 0; j < data[0].length; j++) {
            tmp[this.rowCount()][j] = row[j];
        }
        data = tmp;
    }

    public void pushCol(Integer[] col) {
        Double[][] tmp = new Double[this.rowCount()][this.colCount() + 1];
        for (Integer i = 0; i < this.rowCount(); i++) {
            System.arraycopy(data[i], 0, tmp[i], 0, this.colCount());
            tmp[i][this.colCount()] = col[i];
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

    @Override
    public Integer hashCode() {
        return 0;
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

        for (Integer i = 0; i < this.rowCount(); i++) {
            for (Integer j = 0; j < this.colCount(); j++) {
                if (this.at(i, j) != other.at(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

}
