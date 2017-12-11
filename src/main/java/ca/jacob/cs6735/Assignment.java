package ca.jacob.cs6735;

import ca.jacob.jml.Algorithm;
import ca.jacob.jml.Dataset;
import ca.jacob.jml.KFold;
import ca.jacob.jml.bayes.NaiveBayes;
import ca.jacob.jml.ensemble.AdaBoost;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.distance.Euclidean;
import ca.jacob.jml.math.distribution.Gaussian;
import ca.jacob.jml.tree.ID3;
import ca.jacob.jml.ensemble.RandomForest;
import ca.jacob.jml.neighbors.KNN;
import ca.jacob.jml.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ca.jacob.cs6735.DataUtil.*;

public class Assignment {
    private static final int NUMBER_OF_K_FOLD_ITERATIONS = 1;
    private static final KFold K_FOLD = new KFold(5);
    private static final boolean MULTITHREADED = false;

    public static void main(String[] args) throws Throwable {
        List<Tuple<Dataset, List<Algorithm>>> datasetsAndAlgorithms  = new ArrayList<>();

        // Only for testing purposes
        /*List<Dataset> datasets = new ArrayList<>();
        datasets.add(loadBreastCancerData(Assignment.class));
        datasets.add(loadCarData(Assignment.class));
        datasets.add(loadEColiData(Assignment.class));
        datasets.add(loadLetterData(Assignment.class));
        datasets.add(loadMushroomData(Assignment.class));
        int[] minimumAmountOfSamples = new int[]{1,2,3,5};
        int[] numberOfLearners = new int[]{1};
        double[] proportions = new double[]{1, 0.5};
        int[] ks = new int[]{1, 2, 5, 10};
        for(Dataset dataset : datasets) {
            System.out.println(dataset);
            testID3(dataset, minimumAmountOfSamples);
            test(dataset, new NaiveBayes(new Gaussian()));
            testAdaboost(dataset, new NaiveBayes(new Gaussian()), numberOfLearners, proportions);
            testAdaboost(dataset, new ID3(ID3.MAX_LEVEL_NONE, 1), numberOfLearners, proportions);
            testRandomForest(dataset, numberOfLearners, proportions);
            testKNN(dataset, ks);
        }*/

        // init
        List<Algorithm> algorithms;

        // Breast Cancer Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes());
        //algorithms.add(new AdaBoost(new ID3(1), 100, 0.5));
        //algorithms.add(new AdaBoost(new NaiveBayes(), 100, 0.7));
        //algorithms.add(new RandomForest(new ID3(), 120, 0.7));
        //algorithms.add(new KNN(1, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadBreastCancerData(Assignment.class), algorithms));

        // Car Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes());
        algorithms.add(new AdaBoost(new ID3(3), 200, 0.3)); // TODO: test this with 4
        //algorithms.add(new AdaBoost(new NaiveBayes(), 100, 0.4));
        //algorithms.add(new RandomForest(new ID3(), 200, 0.6));
        //algorithms.add(new KNN(1, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadCarData(Assignment.class), algorithms));

        // Letter Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes(new Gaussian()));
        //algorithms.add(new AdaBoost(new ID3(8), 300, 0.25));
        //algorithms.add(new AdaBoost(new NaiveBayes(new Gaussian()), 10, 0.3));
        //algorithms.add(new RandomForest(new ID3(), 50, 0.6));
        //algorithms.add(new KNN(1, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadLetterData(Assignment.class), algorithms));

        // E Coli. Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 2));
        //algorithms.add(new NaiveBayes(new Gaussian()));
        //algorithms.add(new AdaBoost(new ID3(2), 200, 0.2)); // TODO: test this with 3
        //algorithms.add(new AdaBoost(new NaiveBayes(new Gaussian()), 250, 0.10));
        //algorithms.add(new RandomForest(new ID3(), 250, 0.6));
        //algorithms.add(new KNN(1, new Euclidean()));
        datasetsAndAlgorithms.add(new Tuple<>(loadEColiData(Assignment.class), algorithms));

        // Mushroom Data
        algorithms = new ArrayList<>();
        //algorithms.add(new ID3(ID3.MAX_LEVEL_NONE, 1));
        //algorithms.add(new NaiveBayes());
        //algorithms.add(new AdaBoost(new ID3(2), 10, 0.3));
        //algorithms.add(new AdaBoost(new NaiveBayes(), 10, 0.5));
        //algorithms.add(new RandomForest(new ID3(), 10, 0.5));
        //algorithms.add(new KNN(1, new Hamming()));
        datasetsAndAlgorithms.add(new Tuple<>(loadMushroomData(Assignment.class), algorithms));


        for(Tuple<Dataset, List<Algorithm>> datasetAndAlgorithm : datasetsAndAlgorithms) {
            algorithms = datasetAndAlgorithm.last();
            if(algorithms.size() == 0) {
                continue;
            }

            Dataset d = datasetAndAlgorithm.first();
            System.out.println(d);
            System.out.println("==========================");

            for(Algorithm a : algorithms) {
                test(d, a);
            }
        }
    }

    private static void test(Dataset d, Algorithm a) {
        Report report = new Report();

        if(MULTITHREADED) {
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
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < NUMBER_OF_K_FOLD_ITERATIONS; i++) {
                //System.out.print(i + 1);d
                Report r = K_FOLD.generateReport(a, d);
                report.combine(r);
            }
        }
        System.out.println();
        System.out.println(a);
        System.out.println(report);
        System.out.println();
    }

    private static void testID3(Dataset dataset, int[] minimumNumberOfSamples) {
        for (int minimumNumberOfSample : minimumNumberOfSamples) {
            test(dataset, new ID3(ID3.MAX_LEVEL_NONE, minimumNumberOfSample));

        }
    }

    private static void testAdaboost(Dataset dataset, Algorithm base, int[] numberOfLearners, double[] proportions) {
        for (int numberOfLearner : numberOfLearners) {
            for (double proportion : proportions) {
                test(dataset, new AdaBoost(base, numberOfLearner, proportion));
            }
        }
    }

    private static void testRandomForest(Dataset dataset, int[] numberOfLearners, double[] proportions) {
        for (int numberOfLearner : numberOfLearners) {
            for (double proportion : proportions) {
                test(dataset, new RandomForest(new ID3(ID3.MAX_LEVEL_NONE), numberOfLearner, proportion));
            }
        }
    }

    private static void testKNN(Dataset dataset, int[] ks) {
        for (int k : ks) {
            test(dataset, new KNN(k, new Euclidean()));
        }
    }
}
