package ca.jacob.jml.bayes;

import ca.jacob.jml.Model;
import ca.jacob.jml.math.Vector;
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
        for(ClassSummary c : summaries) {
            double probability = c.probability(e);
            if(maxSummary == null || probability > maxProbability) {
                LOG.debug("max probability is now class {}: {}", c.getClassValue(), probability);
                maxSummary = c;
                maxProbability = probability;
            }
        }
        LOG.debug("most likely class is {}", maxSummary.getClassValue());
        return maxSummary.getClassValue();
    }

    public List<ClassSummary> getSummaries() {
        return summaries;
    }
}
