package ca.jacob.cs6735.test;

import ca.jacob.cs6735.util.File;
import ca.jacob.cs6735.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.cs6735.util.File.readCSV;
import static ca.jacob.cs6735.util.ML.removeSamplesWith;

public class DataUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DataUtil.class);

    public static Matrix loadBreastCancerData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data); //ignore these for now
        Matrix mat = new Matrix(data);
        mat.dropCol(0); // removing id
        return mat;
    }

    public static Matrix loadEcoliData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/ecoli.data"), "\\s+");
        for(int i = 0; i < data.length; i++) {
            String[] row = data[i];
            LOG.trace("replacing {}", row[row.length-1]);
            row[row.length-1] = row[row.length-1].replace("imS", "0");
            row[row.length-1] = row[row.length-1].replace("imL", "1");
            row[row.length-1] = row[row.length-1].replace("omL", "2");
            row[row.length-1] = row[row.length-1].replace("om", "3");
            row[row.length-1] = row[row.length-1].replace("imU", "4");
            row[row.length-1] = row[row.length-1].replace("pp", "5");
            row[row.length-1] = row[row.length-1].replace("im", "6");
            row[row.length-1] = row[row.length-1].replace("cp", "7");
            LOG.trace("with {}", row[row.length-1]);
            row[0] = "0"; //replacing sequence id
        }
        Matrix mat = new Matrix(data);
        mat.dropCol(0); //removing 0's
        LOG.debug("mat dimensions: {} x {}", mat.rowCount(), mat.colCount());
        return mat;
    }
}
