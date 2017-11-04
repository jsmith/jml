package ca.jacob.cs6735.test;

import ca.jacob.cs6735.util.Matrix;

import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;

public class DataUtil {

    public static Matrix loadBreastCancerData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data); //ignore these for now
        Matrix mat = new Matrix(data);
        mat.dropCol(0); // removing id
        return mat;
    }
}
