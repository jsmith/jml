package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.Model;
import ca.jacob.cs6735.util.Matrix;
import ca.jacob.cs6735.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NaiveBayesModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesModel.class);

    private List<ClassSummary> summaries;

    public NaiveBayesModel(List<ClassSummary> summaries) {
        this.summaries = summaries;
    }

    @Override
    public int predict(Vector e) {
        LOG.debug("predicting for e -> {}", e);

        ClassSummary maxSummary = null;
        double maxProbability = -1;
        for(ClassSummary s : summaries) {
            Vector means = s.getMeans();
            Vector stdevs = s.getStdevs();
            Vector conditionalProbabilities = probability(e, means, stdevs);
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

    public List<ClassSummary> getSummaries() {
        return summaries;
    }
}
