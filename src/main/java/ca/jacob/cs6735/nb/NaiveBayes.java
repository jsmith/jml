package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.Algorithm;
import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static ca.jacob.cs6735.util.ML.splitByColumn;

public class NaiveBayes implements Algorithm {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayes.class);

    @Override
    public Model fit(Matrix x, Vector y) {
        x.pushCol(y);
        Map<Integer, Matrix> separated = splitByColumn(x, x.colCount()-1);
        x.dropCol(x.colCount()-1);

        return new NaiveBayesModel(summaries);
    }
}
