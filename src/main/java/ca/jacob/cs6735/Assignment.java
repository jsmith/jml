package ca.jacob.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.distance.Hamming;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.ensemble.Adaboost;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.nb.NaiveBayes;
import ca.jacob.jml.neighbors.KNN;
import ca.jacob.jml.util.DataSet;
import ca.jacob.jml.util.Matrix;
import ca.jacob.jml.util.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static ca.jacob.cs6735.DataUtil.*;
import static ca.jacob.jml.util.DataSet.DISCRETE;

public class Assignment {
    private static final Logger LOG = LoggerFactory.getLogger(Assignment.class);

    public static void main(String[] args) throws Throwable {
        // Creating data
        List<DataSet> data = new ArrayList<>();
        data.add(loadBreastCancerData(Assignment.class));
        data.add(loadCarData(Assignment.class));
        data.add(loadLetterData(Assignment.class));
        data.add(loadEcoliData(Assignment.class));
        data.add(loadMushroomData(Assignment.class));


        // Creating algorithms
        List<Algorithm> algorithms = new ArrayList<>();

        Algorithm id3 = new ID3(ID3.MAX_LEVEL_NONE);
        algorithms.add(id3);

        id3 = new ID3(3, 200);
        algorithms.add(id3);

        Algorithm nb = new NaiveBayes();
        algorithms.add(nb);

        Algorithm adaboost = new Adaboost(new ID3(1), 50, 0.3);
        algorithms.add(adaboost);

        adaboost = new Adaboost(new NaiveBayes(), 50, 0.3);
        algorithms.add(adaboost);

        RandomForest rf = new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), 1000, 0.6);
        algorithms.add(rf);

        Algorithm knn = new KNN(3, false, new Hamming());
        algorithms.add(knn);

        int numberOfKFoldIterations = 10;
        KFold kFold = new KFold(5);
        for(DataSet d : data) {
            System.out.println(d);
            System.out.println("=======================");
                for(Algorithm a : algorithms) {
                Report report = new Report();
                for(int i = 0; i < numberOfKFoldIterations; i++) {
                    Report r = kFold.generateReport(a, d);
                    report.combine(r);
                }
                System.out.println(a);
                System.out.println(report);
                System.out.println();
            }
        }
    }
}
