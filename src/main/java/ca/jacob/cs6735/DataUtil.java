package ca.jacob.cs6735;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.Util;
import ca.jacob.jml.math.Matrix;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.jacob.jml.Dataset.CONTINUOUS;
import static ca.jacob.jml.Dataset.DISCRETE;
import static ca.jacob.jml.Util.toIntegers;

public class DataUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DataUtil.class);

    public static Dataset loadBreastCancerData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        toIntegers(data);
        Matrix breastCancerMatrix = new Matrix(data);

        breastCancerMatrix.dropCol(0); // removing ids

        Dataset dataset = new Dataset(breastCancerMatrix, DISCRETE);
        dataset.setName("Breast Cancer Data");

        return dataset;
    }

    public static Dataset loadCarData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/car.data"));
        toIntegers(data);
        Matrix carMatrix = new Matrix(data);

        Dataset dataset = new Dataset(carMatrix, DISCRETE);
        dataset.setName("Car Data");

        return dataset;
    }

    public static Dataset loadLetterData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/letter-recognition.data"));
        toIntegers(data, new Vector(new int[]{0}));
        Matrix letterMatrix = new Matrix(data);

        letterMatrix.swapCols(0, letterMatrix.colCount()-1); // Putting classifications on last column

        Dataset dataset = new Dataset(letterMatrix, CONTINUOUS);
        dataset.setName("Letter Data");

        return dataset;
    }

    public static Dataset loadMushroomData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/mushroom.data"));
        toIntegers(data);
        Matrix mushroomMatrix = new Matrix(data);

        mushroomMatrix.swapCols(0, mushroomMatrix.colCount()-1); // Putting classifications on last column

        Dataset dataset = new Dataset(mushroomMatrix, DISCRETE);
        dataset.setName("Mushroom Data");

        return dataset;
    }

    public static Dataset loadEColiData(Class c) throws Throwable {
        String[][] data = Util.readCSV(c.getResourceAsStream("/data/ecoli.data"), "\\s+");
        toIntegers(data);
        Matrix mat = new Matrix(data);

        mat.dropCol(4); //removing 0.48s
        mat.dropCol(3); //removing 0.50's
        mat.dropCol(0); // removing sequence ids

        LOG.debug("mat dimensions: {} x {}", mat.rowCount(), mat.colCount());

        Dataset dataset = new Dataset(mat, CONTINUOUS);
        dataset.setName("E. Coli Data");

        return dataset;
    }

}
