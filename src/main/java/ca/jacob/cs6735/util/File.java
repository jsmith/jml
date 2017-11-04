package ca.jacob.cs6735.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class File {
    private static final Logger LOG = LoggerFactory.getLogger(File.class);

    public static String[][] readCSV(InputStream inputStream) throws Throwable {
        return readCSV(inputStream, ",");
    }

    public static String[][] readCSV(InputStream inputStream, String deliminator) throws Throwable {
        String line;
        String cvsSplitBy = deliminator;
        String[][] data;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> lines = new ArrayList<String>();

            int rows = 0;
            while ((line = br.readLine()) != null) {
                line.replace(" ", "");
                lines.add(line);
                rows++;
            }
            LOG.debug("number of rows: {}", rows);

            int cols = lines.get(0).split(cvsSplitBy).length;
            LOG.debug("number of columns: {}", cols);

            data = new String[rows][cols];

            for(int i = 0; i < lines.size(); i++) {
                data[i] = lines.get(i).split(cvsSplitBy);
            }

        } catch (IOException e) {
            throw new FileException("error reading csv file").initCause(e);
        }
        return data;
    }
}
