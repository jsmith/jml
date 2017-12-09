package ca.jacob.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.KFold;
import ca.jacob.jml.ensemble.AdaBoost;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.distance.Euclidean;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.neighbors.KNN;
import ca.jacob.jml.DataSet;
import ca.jacob.jml.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ca.jacob.cs6735.DataUtil.*;

public class Assignment {
    private static final int NUMBER_OF_K_FOLD_ITERATIONS = 10;
    private static final KFold K_FOLD = new KFold(5);
    private static final boolean CONCURRENT = false;

    public static void main(String[] args) throws Throwable {
        List<Tuple<DataSet, List<Algorithm>>> datasetsAndAlgorithms  = new ArrayList<>();

        // init
        List<Algorithm> algorithms;


        /*List<DataSet> datasets = new ArrayList<>();
        datasets.add(loadBreastCancerData(Assignment.class));
        datasets.add(loadCarData(Assignment.class));
        datasets.add(loadEColiData(Assignment.class));
        datasets.add(loadLetterData(Assignment.class));
        datasets.add(loadMushroomData(Assignment.class));
        int[] minimumAmountOfSamples = new int[]{1,2,3,5};
        int[] numberOfLearners = new int[]{1};
        double[] proportions = new double[]{1, 0.5};
        int[] ks = new int[]{1, 2, 5, 10};
        for(DataSet dataset : datasets) {
            System.out.println(dataset);
            testID3(dataset, minimumAmountOfSamples);
            test(dataset, new NaiveBayes(new GaussianDistribution()));
            testAdaboost(dataset, new NaiveBayes(new GaussianDistribution()), numberOfLearners, proportions);
            testAdaboost(dataset, new ID3(ID3.MAX_LEVEL_NONE, 1), numberOfLearners, proportions);
            testRandomForest(dataset, numberOfLearners, proportions);
            testKNN(dataset, ks);
        }*/

        // Breast Cancer Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes());
        //algorithms.add(new AdaBoost(new ID3(1), 100, 0.5));
        //algorithms.add(new AdaBoost(new NaiveBayes(), 100, 0.7));
        //algorithms.add(new RandomForest(new ID3(), 120, 0.7));
        //algorithms.add(new KNN(1, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadBreastCancerData(Assignment.class), algorithms));

        // Car Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes());
        //algorithms.add(new AdaBoost(new ID3(2), 250, 0.3));
        //algorithms.add(new AdaBoost(new NaiveBayes(), 100, 0.4));
        //algorithms.add(new RandomForest(new ID3(), 200, 0.6));
        //algorithms.add(new KNN(1, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadCarData(Assignment.class), algorithms));

        // E Coli. Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 2));
        //algorithms.add(new NaiveBayes(new GaussianDistribution()));
        //algorithms.add(new AdaBoost(new ID3(2), 200, 0.2));
        //algorithms.add(new AdaBoost(new NaiveBayes(new GaussianDistribution()), 250, 0.10));
        //algorithms.add(new RandomForest(new ID3(), 250, 0.6));
        //algorithms.add(new KNN(1, false, new Euclidean()));
        datasetsAndAlgorithms.add(new Tuple<>(loadEColiData(Assignment.class), algorithms));

        // Letter Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes(new GaussianDistribution()));
        algorithms.add(new AdaBoost(new ID3(3), 7, 0.7));
        //algorithms.add(new AdaBoost(new NaiveBayes(new GaussianDistribution()), 8, 0.7));
        //algorithms.add(new RandomForest(new ID3(), 10, 0.6));
        //algorithms.add(new KNN(1, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadLetterData(Assignment.class), algorithms));

        // Mushroom Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes());
        //algorithms.add(new AdaBoost(new ID3(2), 10, 0.3));
        //algorithms.add(new AdaBoost(new NaiveBayes(), 10, 0.5));
        //algorithms.add(new RandomForest(new ID3(), 10, 0.5));
        //algorithms.add(new KNN(1, false, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadMushroomData(Assignment.class), algorithms));


        for(Tuple<DataSet, List<Algorithm>> datasetAndAlgorithm : datasetsAndAlgorithms) {
            DataSet d = datasetAndAlgorithm.first();
            algorithms = datasetAndAlgorithm.last();

            System.out.println(d);
            System.out.println("=======================");
            for(Algorithm a : algorithms) {
                test(d, a);
            }
        }
    }

    private static void test(DataSet d, Algorithm a) {
        Report report = new Report();

        if(CONCURRENT) {
            final ExecutorService service;
            final List<Future<Report>> tasks = new ArrayList<>();
            service = Executors.newFixedThreadPool(NUMBER_OF_K_FOLD_ITERATIONS);
            K_FOLD.init(a, d);
            for (int i = 0; i < NUMBER_OF_K_FOLD_ITERATIONS; i++) {
                System.out.print(i + 1);
                tasks.add(service.submit(K_FOLD));
            }

            for (int i = 0; i < NUMBER_OF_K_FOLD_ITERATIONS; i++) {
                try {
                    Report r = tasks.get(i).get();
                    report.combine(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //for (int i = 0; i < NUMBER_OF_K_FOLD_ITERATIONS; i++) {
                //System.out.print(i + 1);d
                Report r = K_FOLD.generateReport(a, d);
                report.combine(r);
            //}
        }
        System.out.println();
        System.out.println(a);
        System.out.println(report);
        System.out.println();
    }

    private static void testID3(DataSet dataset, int[] minimumNumberOfSamples) {
        double bestMinimumAmount = -1;
        for(int i = 0; i < minimumNumberOfSamples.length; i++) {
            test(dataset, new ID3(ID3.MAX_LEVEL_NONE, minimumNumberOfSamples[i]));

        }
    }

    private static void testAdaboost(DataSet dataset, Algorithm base, int[] numberOfLearners, double[] proportions) {
        for(int i = 0; i < numberOfLearners.length; i++) {
            for(int j = 0; j < proportions.length; j++) {
                test(dataset, new AdaBoost(base, numberOfLearners[i], proportions[j]));
            }
        }
    }

    private static void testRandomForest(DataSet dataSet, int[] numberOfLearners, double[] proportions) {
        for(int i = 0; i < numberOfLearners.length; i++) {
            for(int j = 0; j < proportions.length; j++) {
                test(dataSet, new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), numberOfLearners[i], proportions[j]));
            }
        }
    }

    private static void testKNN(DataSet dataSet, int[] ks) {
        for(int i = 0; i < ks.length; i++) {
            test(dataSet, new KNN(ks[i], false, new Euclidean()));
        }
    }
}
