package ca.jacob.jml;

import ca.jacob.jml.math.MathException;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.lang.Math.random;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    public static String[][] removeSamplesWith(String key, String[][] data) {
        ArrayList<String[]> modified = new ArrayList<String[]>();
        for(int i = 0; i < data.length; i++) {
            boolean containsKey = false;
            for(int j = 0; j < data[0].length; j++) {
                if(data[i][j].contains(key)) {
                   containsKey = true;
                }
            }
            if(!containsKey) {
                modified.add(data[i]);
            }
        }

        return modified.toArray(new String[modified.get(0).length][modified.get(0).length]);
    }

    public static int sign(double n) {
        if(n >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static Vector generateIndices(Vector weights, int numberOfIndices) {
        Vector probabilities = new Vector(new double[weights.length()]);
        double sum = weights.sum();
        for(int i = 0; i < weights.length(); i++) {
            probabilities.set(i, weights.at(i)/sum);
        }
        LOG.debug("probabilities sum is {}", probabilities.sum());

        Vector indices = new Vector(new int[numberOfIndices]);
        for(int i = 0; i < numberOfIndices; i++) {
            double rand = random();
            LOG.trace("rand for index {} is {}", i, rand);
            double cumulativeProbability = 0.0;
            for (int j = 0; j < probabilities.length(); j++) {
                cumulativeProbability += probabilities.at(j);
                if (rand <= cumulativeProbability) {
                    LOG.trace("setting index {} to {}", i, j);
                    indices.set(i, j);
                    break;
                }
            }
        }
        return indices;
    }

    public static Vector generateIndices(int from, int to, int numberOfIndices) {
        Vector weights = new Vector(new double[to-from]);
        weights.fill(1./(to-from));
        return generateIndices(weights, numberOfIndices);
    }

    public static Vector generateIndicesWithoutReplacement(Vector weights, int numberOfIndices) {
        Vector probabilities = new Vector(new double[weights.length()]);
        double sum = weights.sum();
        for(int i = 0; i < weights.length(); i++) {
            probabilities.set(i, weights.at(i)/sum);
        }
        LOG.debug("probabilities sum is {}", probabilities.sum());

        Vector indices = new Vector(new int[numberOfIndices]);
        for(int i = 0; i < numberOfIndices; i++) {
            double rand = random();
            LOG.trace("rand for index {} is {}", i, rand);
            double cumulativeProbability = 0.0;
            for (int j = 0; j < probabilities.length(); j++) {
                cumulativeProbability += probabilities.at(j);
                if (rand <= cumulativeProbability) {
                    if(indices.contains(j)) {
                        i--;
                    } else {
                        LOG.trace("setting index {} to {}", i, j);
                        indices.set(i, j);
                    }
                    break;
                }
            }
        }
        return indices;
    }

    public static Vector range(int from, int to) {
        int[] range = new int[to-from];
        for(int i = 0; i < to-from; i++) {
            range[i] = from+i;
        }
        return new Vector(range);
    }

    public static void shuffle(Vector v) {
        Random random = new Random();

        for (int i = v.length() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            v.swap(i, index);
        }
    }

    public static void shuffle(Vector v, Long seed) {
        if(seed == null) {
            shuffle(v);
            return;
        }

        Random random = new Random(seed);
        for (int i = v.length() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            v.swap(i, index);
        }
    }

    public static Vector error(Vector one, Vector two) {
        if(one.length() != two.length()) {
            throw new MathException("vector lengths must match");
        }

        Vector error = new Vector(new double[one.length()]);
        for(int i = 0; i < one.length(); i++) {
            if(one.at(i) != two.at(i)) {
                error.set(i, 1.);
            } else {
                error.set(i, 0.);
            }
        }
        return error;
    }

    public static double[] toPrimitiveArray(List<Double> list) {
        if(list == null) {
            return null;
        }
        double[] result = new double[list.size()];
        for(int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static List<Double> arrayAsList(double[] array) {
        List<Double> result = new ArrayList<Double>();
        for(int i = 0; i < array.length; i++) {
            result.add(array[i]);
        }
        return result;
    }


    public static double calculateWeightedEntropy(List<DataSet> subsets) {
        double entropy = 0;
        int sum = 0;
        for(DataSet subset : subsets) {
            entropy += subset.entropy() * subset.sampleCount();
            sum += subset.sampleCount();
        }

        entropy /= (sum * subsets.size());

        LOG.trace("the total weighted entropy is {}", entropy);
        return entropy;
    }

    public static double calculateWeightedEntropy(Collection<DataSet> subsets) {
        return calculateWeightedEntropy(new ArrayList<DataSet>(subsets));
    }

    public static double calculateWeightedEntropy(Tuple<DataSet, DataSet> subsets) {
        double entropy = 0;
        entropy += subsets.first().entropy() * subsets.first().sampleCount();
        entropy += subsets.last().entropy() * subsets.last().sampleCount();

        entropy /= ((subsets.first().sampleCount() + subsets.last().sampleCount()) * 2);

        LOG.trace("the total weighted entropy is {}", entropy);
        return entropy;
    }
}