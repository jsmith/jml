package ca.jacob.cs6735;

import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.FileException;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.jacob.jml.util.DataSet.CONTINUOUS;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static ca.jacob.jml.util.ML.range;
import static ca.jacob.jml.util.ML.removeSamplesWith;

public class DataUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DataUtil.class);

    public static DataSet loadBreastCancerData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/breast-cancer-wisconsin.data"));
        data = removeSamplesWith("?", data);
        Matrix breastCancerMatrix = new Matrix(data);
        Vector v = breastCancerMatrix.col(breastCancerMatrix.colCount()-1);
        v = v.replace(2, -1);
        v = v.replace(4, 1);
        breastCancerMatrix.setCol(breastCancerMatrix.colCount()-1, v);
        breastCancerMatrix.dropCol(0);

        breastCancerMatrix.dropCol(0); // removing id
        DataSet dataset = new DataSet(breastCancerMatrix, DISCRETE);
        dataset.setName("Breast Cancer Data");

        return dataset;
    }

    public static DataSet loadCarData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/car.data"));
        toIntegers(data);
        Matrix carMatrix = new Matrix(data);
        DataSet dataset = new DataSet(carMatrix, DISCRETE);
        dataset.setName("Car Data");
        return dataset;
    }

    public static DataSet loadLetterData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/letter-recognition.data"));
        toIntegers(data, new Vector(new int[]{0}));
        Matrix letterMatrix = new Matrix(data);
        letterMatrix.swapCols(0, letterMatrix.colCount()-1);
        DataSet dataset = new DataSet(letterMatrix, CONTINUOUS);
        dataset.setName("Letter Data");
        return dataset;
    }

    public static DataSet loadMushroomData(Class c) throws Throwable {
        String[][] data = readCSV(c.getResourceAsStream("/data/mushroom.data"));
        toIntegers(data);
        Matrix mushroomMatrix = new Matrix(data);
        mushroomMatrix.swapCols(0, mushroomMatrix.colCount()-1);
        DataSet dataset = new DataSet(mushroomMatrix, DISCRETE);
        dataset.setName("Mushroom Data");
        return dataset;
    }

    public static DataSet loadEcoliData(Class c) throws Throwable {
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
        LOG.warn("initial string data[][] dimensions: {} x {}", data.length, data[0].length);
        Matrix mat = new Matrix(data);
        LOG.warn("initial matrix dimensions: {} x {}", mat.rowCount(), mat.colCount());

        mat.dropCol(3); //removing 0.50's
        mat.dropCol(4); //removing 0.48s
        mat.dropCol(0); // removing sequence ids

        LOG.debug("mat dimensions: {} x {}", mat.rowCount(), mat.colCount());

        Vector attributeTypes = new Vector(new int[]{CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS,});
        DataSet dataset = new DataSet(mat, attributeTypes);
        dataset.setName("E. Coli Data");
        return dataset;
    }

    public static String[][] readCSV(InputStream inputStream) throws Throwable {
        return readCSV(inputStream, ",");
    }

    public static String[][] readCSV(InputStream inputStream, String deliminator) throws Throwable {
        String line;
        String[][] data;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> lines = new ArrayList<String>();

            int rows = 0;
            while ((line = br.readLine()) != null) {
                //line = line.replace(" ", "");
                lines.add(line);
                rows++;
            }
            LOG.debug("number of rows: {}", rows);

            int cols = lines.get(0).split(deliminator).length;
            LOG.debug("number of columns: {}", cols);

            data = new String[rows][cols];

            for(int i = 0; i < lines.size(); i++) {
                data[i] = lines.get(i).split(deliminator);
            }

        } catch (IOException e) {
            throw new FileException("error reading csv file").initCause(e);
        }
        return data;
    }

    public static void toIntegers(String[][] data) {
        toIntegers(data, range(0, data[0].length));
    }

    public static void toIntegers(String[][] data, Vector colIndices) {
        for(int k = 0; k < colIndices.length(); k++) {
            int count = 0;
            Map<String, Integer> values = new HashMap<>();
            for(int i = 0; i < data.length; i++) {
                Integer value = values.get(data[i][colIndices.intAt(k)]);
                if(value == null) {
                    value = count;
                    values.put(data[i][colIndices.intAt(k)], value);
                    count++;
                }
                data[i][colIndices.intAt(k)] = String.valueOf(value);
            }
        }
    }
}
