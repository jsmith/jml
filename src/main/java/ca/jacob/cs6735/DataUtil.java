package ca.jacob.cs6735;

import ca.jacob.jml.DataSet;
import ca.jacob.jml.Util;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.DataSet.CONTINUOUS;
import static ca.jacob.jml.DataSet.DISCRETE;
import static ca.jacob.jml.Util.removeSamplesWith;
import static ca.jacob.jml.Util.replaceWithMostCommonFromClass;
import static ca.jacob.jml.Util.toIntegers;

public class DataUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DataUtil.class);

    public static DataSet loadBreastCancerData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        toIntegers(data);
        Matrix breastCancerMatrix = new Matrix(data);
        Vector v = breastCancerMatrix.col(breastCancerMatrix.colCount()-1);
        v = v.replace(0, -1);
        v = v.replace(1, 1);
        breastCancerMatrix.setCol(breastCancerMatrix.colCount()-1, v);

        breastCancerMatrix.dropCol(0); // removing id
        LOG.debug("breastCancerMatrix row 0 -> ", breastCancerMatrix.row(0));
        DataSet dataset = new DataSet(breastCancerMatrix, DISCRETE);
        dataset.setName("Breast Cancer Data");

        return dataset;
    }

    public static DataSet loadCarData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/car.data"));
        Util.toIntegers(data);
        Matrix carMatrix = new Matrix(data);
        DataSet dataset = new DataSet(carMatrix, DISCRETE);
        dataset.setName("Car Data");
        return dataset;
    }

    public static DataSet loadLetterData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/letter-recognition.data"));
        Util.toIntegers(data, new Vector(new int[]{0}));
        Matrix letterMatrix = new Matrix(data);
        Vector classes = letterMatrix.col(0);
        letterMatrix.dropCol(0);
        letterMatrix.pushCol(classes);
        DataSet dataset = new DataSet(letterMatrix, CONTINUOUS);
        LOG.debug("car data first three samples -> {}", dataset.samples(new Vector(new int[]{0, 1, 2})).dataToString());
        dataset.setName("Letter Data");
        return dataset;
    }

    public static DataSet loadMushroomData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/mushroom.data"));
        Util.toIntegers(data);
        Matrix mushroomMatrix = new Matrix(data);
        mushroomMatrix.swapCols(0, mushroomMatrix.colCount()-1);
        DataSet dataset = new DataSet(mushroomMatrix, DISCRETE);
        dataset.setName("Mushroom Data");
        return dataset;
    }

    public static DataSet loadEColiData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/ecoli.data"), "\\s+");
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
        LOG.warn("initial string data[][] dimensions: {} x {}", data.length, data[0].length);
        Matrix mat = new Matrix(data);
        LOG.warn("initial matrix dimensions: {} x {}", mat.rowCount(), mat.colCount());

        mat.dropCol(4); //removing 0.48s
        mat.dropCol(3); //removing 0.50's
        mat.dropCol(0); // removing sequence ids

        LOG.debug("mat dimensions: {} x {}", mat.rowCount(), mat.colCount());

        DataSet dataset = new DataSet(mat, CONTINUOUS);
        dataset.setName("E. Coli Data");
        return dataset;
    }

}
