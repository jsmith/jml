package ca.jacob.cs6735.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrix {
  private static final Logger LOG = LoggerFactory.getLogger(Matrix.class);

  private Integer[][] values;

  public Matrix(Integer[][] values) {
    this.values = values;
  }

  public Matrix() {
    this.values = new Integer[0][0];
  }

  public Integer[] row(int i) {
    return values[i];
  }

  public void pushRow(Integer[] row) {
    Integer[][] tmp = new Integer[this.rowCount()+1][this.colCount()];
    System.arraycopy(values, 0, tmp, 0, this.rowCount());
    tmp[this.rowCount()] = row;
    values = tmp;
  }

  public void pushRow(Vector v) {
    this.pushRow(v.toArray());
  }

  public void pushCol(Integer[] col) {
    Integer[][] tmp = new Integer[this.rowCount()][this.colCount()+1];
    for(int i = 0; i < this.rowCount(); i++) {
      System.arraycopy(values[i], 0, tmp[i], 0, this.colCount());
      tmp[i][this.colCount()] = col[i];
    }
    values = tmp;
  }

  public Integer[] col(int j) {
    Integer[] col = new Integer[values.length];
    for(int i = 0; i < values.length; i++) {
      col[i] = values[i][j];
    }
    return col;
  }

  public Integer at(int i, int j) {
    return values[i][j];
  }

  public int rowCount() {
    return values.length;
  }

  public int colCount() {
    if(values.length == 0) {
      return 0;
    }

    return values[0].length;
  }

  public static Map<Integer, Integer> calculateOccurances(Integer[] values) {
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    for(Integer value : values) {
      LOG.debug("adding {} to map", value);
      Integer count = map.get(value);
      if(count == null) {
        count = 1;
        map.put(value, count);
      } else {
        count++;
        map.replace(value, count);
      }
    }
    return map;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object object) {
    if(object == null) {
      return false;
    }
    if(!(object instanceof Matrix)) {
      return false;
    }

    Matrix other = (Matrix)object;
    if(this.rowCount() != other.rowCount()) {
      return false;
    }
    if(this.colCount() != other.colCount()) {
      return false;
    }

    for(int i = 0; i < this.rowCount(); i++) {
      for(int j = 0; j < this.colCount(); j++) {
        if(this.at(i, j) != other.at(i, j)) {
          return false;
        }
      }
    }

    return true;
  }
  public static Double log2(double value) {
    return Math.log(value) / Math.log(2);
  }
}
