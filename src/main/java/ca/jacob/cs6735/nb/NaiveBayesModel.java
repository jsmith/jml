package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.dt.Node;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.util.Math.gaussianProbability;

public class NaiveBayesModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesModel.class);

    private List<Summary> summaries;

    public NaiveBayesModel(List<Summary> summaries) {
        this.summaries = summaries;
    }

    @Override
    public int predict(Vector e) {
        LOG.debug("predicting for e -> {}", e);

        Summary maxSummary = null;
        double maxProbability = -1;
        for(Summary s : summaries) {
            Vector means = s.getMeans();
            Vector stdevs = s.getStdevs();
            Vector conditionalProbabilities = gaussianProbability(e, means, stdevs);
            LOG.debug("\nmeans -> {};\nstdevs -> {};\nconditional probabilities -> {}", means, stdevs, conditionalProbabilities);
            double probability = conditionalProbabilities.prod();
            probability *= s.getClassProbability();
            if(maxSummary == null || probability > maxProbability) {
                LOG.debug("max probability is now: {}", probability);
                maxSummary = s;
                maxProbability = probability;
            }
        }
        LOG.debug("most likely class is {}", maxSummary.getClassValue());
        return maxSummary.getClassValue();
    }

    @Override
    public Vector predict(Matrix x) {
        Vector predictions = new Vector(new double[x.rowCount()]);
        for(int i = 0; i < x.rowCount(); i++) {
            predictions.set(i, this.predict(x.row(i)));
        }
        return predictions;
    }

    public List<Summary> getSummaries() {
        return summaries;
    }
}
