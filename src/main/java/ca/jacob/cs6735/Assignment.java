package ca.jacob.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.distance.Hamming;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.ensemble.AdaBoost;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.bayes.NaiveBayes;
import ca.jacob.jml.neighbors.KNN;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.Report;

import java.util.ArrayList;
import java.util.List;

import static ca.jacob.cs6735.DataUtil.*;

public class Assignment {

    public static void main(String[] args) throws Throwable {
        List<Tuple<DataSet, List<Algorithm>>> datasetsAndAlgorithms  = new ArrayList<>();

        // init
        List<Algorithm> algorithms;

        // Dataset 1
        algorithms = new ArrayList<>();
        algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 200));
        algorithms.add(new NaiveBayes());
        algorithms.add(new AdaBoost(new ID3(1), 50, 0.3));
        algorithms.add(new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), 500, 0.6));
        algorithms.add(new KNN(3, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadBreastCancerData(Assignment.class), algorithms));

        /*// Dataset 2
        algorithms = new ArrayList<>();
        algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 200));
        algorithms.add(new NaiveBayes());
        algorithms.add(new AdaBoost(new ID3(1), 50, 0.3));
        algorithms.add(new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), 500, 0.6));
        algorithms.add(new KNN(3, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadCarData(Assignment.class), algorithms));*/

        /*// Dataset 3
        algorithms = new ArrayList<>();
        algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 200));
        algorithms.add(new NaiveBayes());
        algorithms.add(new AdaBoost(new ID3(1), 50, 0.3));
        algorithms.add(new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), 500, 0.6));
        algorithms.add(new KNN(3, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadEColiData(Assignment.class), algorithms));*/

        /*// Dataset 4
        algorithms = new ArrayList<>();
        algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 200));
        algorithms.add(new NaiveBayes());
        algorithms.add(new AdaBoost(new ID3(1), 50, 0.3));
        algorithms.add(new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), 500, 0.6));
        algorithms.add(new KNN(3, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadLetterData(Assignment.class), algorithms));*/

        /*// Dataset 5
        algorithms = new ArrayList<>();
        algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 200));
        algorithms.add(new NaiveBayes());
        algorithms.add(new AdaBoost(new ID3(1), 50, 0.3));
        algorithms.add(new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), 500, 0.6));
        algorithms.add(new KNN(3, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadMushroomData(Assignment.class), algorithms));*/


        int numberOfKFoldIterations = 10;
        KFold kFold = new KFold(5);
        for(Tuple<DataSet, List<Algorithm>> datasetAndAlgorithm : datasetsAndAlgorithms) {
            DataSet d = datasetAndAlgorithm.first();
            algorithms = datasetAndAlgorithm.last();

            System.out.println(d);
            System.out.println("=======================");
                for(Algorithm a : algorithms) {
                Report report = new Report();
                for(int i = 0; i < numberOfKFoldIterations; i++) {
                    System.out.print(i+1);
                    Report r = kFold.generateReport(a, d);
                    report.combine(r);
                }
                System.out.println();
                System.out.println(a);
                System.out.println(report);
                System.out.println();
            }
        }
    }
}
