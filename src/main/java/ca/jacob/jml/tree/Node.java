package ca.jacob.jml.tree;

import ca.jacob.jml.Dataset;
import ca.jacob.jml.exceptions.AttributeException;
import ca.jacob.jml.exceptions.PredictionException;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static ca.jacob.jml.Dataset.CONTINUOUS;
import static ca.jacob.jml.Dataset.DISCRETE;
import static ca.jacob.jml.Util.calculateWeightedEntropy;

public class Node {
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private int attribute;
    private boolean leaf;
    private int prediction;
    private int attributeType;
    private Children children;
    private int level;
    private int maxLevel;
    private int minNumberOfSamples;

    public Node(int maxLevel, int minNumberOfSamples) {
        this.init(0, maxLevel, minNumberOfSamples);
    }

    public Node(Node parent) {
        this.init(parent.level+1, parent.maxLevel, parent.minNumberOfSamples);
    }

    private void init(int level, int maxLevel, int minNumberOfSamples) {
        leaf = false;
        this.level = level;
        this.maxLevel = maxLevel;
        this.minNumberOfSamples = minNumberOfSamples;
        this.attribute = -1;
        this.prediction = -1;
        this.attributeType = -1;
    }

    public void split(Dataset dataset) {
        LOG.info("split - starting for level {}", level);
        this.prediction = dataset.classes().valueOfMaxOccurrence();

        if(level == maxLevel || dataset.entropy() == 0 || dataset.sampleCount() <= 1 || dataset.sampleCount() < minNumberOfSamples) {
            LOG.debug("found leaf - level: {}, entropy: {}, numOfSamples: {}", this.level, dataset.entropy(), dataset.sampleCount());
            this.leaf = true;
            return;
        }

        int numOfAttributes = dataset.attributeCount();

        double minEntropy = -1;
        int bestAttribute = -1;
        for(int j = 0; j < numOfAttributes; j++) {
            LOG.trace("checking attribute {} of type {}", j, dataset.attributeType(j));

            double entropy;
            if(dataset.attributeType(j) == DISCRETE) {
                Tuple<List<Integer>, List<Dataset>> subsets = dataset.splitByDiscreteAttribute(j);
                entropy = calculateWeightedEntropy(subsets.last());

            } else if(dataset.attributeType(j) == CONTINUOUS) {
                Tuple<Double, Tuple<Dataset, Dataset>> subsets = dataset.splitByContinuousAttribute(j);
                if(subsets == null) {
                    LOG.debug("no possible subsets for attribute {}", j);
                    continue;
                }
                entropy = calculateWeightedEntropy(subsets.last());
            } else {
                throw new AttributeException("unknown data type");
            }
            LOG.debug("the total entropy of the children when splitting on attribute {} is {}", j, entropy);

            if(bestAttribute < 0 || entropy < minEntropy) {
                LOG.trace("attribute {} is now the best attribute", j);
                minEntropy = entropy;
                bestAttribute = j;
            }
        }
        LOG.debug("the best attribute is {} for level {}", bestAttribute, level);

        if(bestAttribute < 0) {
            LOG.info("no possible subsets found -> {}", dataset.dataToString());
            this.leaf = true;
            return;
        }

        if(dataset.entropy() <= minEntropy) {
            LOG.debug("children have {} entropy while current model has {} entropy! Creating a leaf!", minEntropy, dataset.entropy());
            this.leaf = true;
            return;
        }

        attribute = bestAttribute;
        attributeType = dataset.attributeType(attribute);

        List<Dataset> subsets = new ArrayList<>(); // Subsets are given when splitting so that the data does not need to be stored
        if(dataset.attributeType(bestAttribute) == CONTINUOUS) {
            Tuple<Double, Tuple<Dataset, Dataset>> split = dataset.splitByContinuousAttribute(bestAttribute);
            LOG.debug("pivot is {}", split.first());

            Dataset under = split.last().first();
            Dataset over = split.last().last();
            under.dropAttribute(attribute);
            over.dropAttribute(attribute);
            subsets.add(under);
            subsets.add(over);

            children = new ContinuousChildren(this, split.first());
        } else if(dataset.attributeType(bestAttribute) == DISCRETE) {
            Tuple<List<Integer>, List<Dataset>> split = dataset.splitByDiscreteAttribute(bestAttribute);
            subsets = split.last();
            for(Dataset subset : subsets) {
                subset.dropAttribute(attribute);
            }
            children = new DiscreteChildren(this, split.first());
        }
        LOG.debug("there will be {} children", children.size());

        children.split(subsets);
    }

    public Children getChildren() {
        return children;
    }

    int classify(Vector e) {
        LOG.trace("classify - starting for level {} and attribute {}", level, attribute);

        if (this.leaf) {
            LOG.trace("a leaf was found, now classifying!");

            return this.predict();
        } else {
            try {
                return children.predict(e);
            } catch (PredictionException ex) {
                LOG.trace("children unable to predict sample");
                return this.predict();
            }
        }
    }

    public int predict() {
        LOG.trace("predicting starting on level {}", level);
        return prediction;
    }

    public int getAttribute() {
        return attribute;
    }

    public boolean isLeaf() {
        return leaf;
    }

    int getAttributeType() {
        return attributeType;
    }

    public int depth() {
        if(children == null) {
            return 0;
        }

        int max = 0;
        for(Node node : children) {
            int depth = node.depth();
            if(depth > max) {
                max = depth;
            }
        }
        return max + 1;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "Node Level "+level;
    }
}
