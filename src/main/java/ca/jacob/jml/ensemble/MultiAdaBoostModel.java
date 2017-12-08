package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.exceptions.DataException;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MultiAdaBoostModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(MultiAdaBoostModel.class);

    List<Tuple<Integer, AdaBoostModel>> adaboostModels;

    public MultiAdaBoostModel(List<Tuple<Integer, AdaBoostModel>> adaboostModels) {
        this.adaboostModels = adaboostModels;
    }

    public List<Tuple<Integer, AdaBoostModel>> getModels() {
        return adaboostModels;
    }

    @Override
    public int predict(Vector e) {
        Tuple<Double, Integer> prediction = null;
        for(Tuple<Integer, AdaBoostModel> adaboostModel : adaboostModels) {
            int c = adaboostModel.first();
            AdaBoostModel model = adaboostModel.last();
            double predictionValue = model.prediction(e);
            if(Double.isNaN(predictionValue)) {
                model.prediction(e);
                throw new DataException("prediction value for class " + c + " is NaN");
            }

            if(prediction == null || prediction.first() < predictionValue) {
                LOG.trace("highest prediction value is now {} corresponding to class {}", predictionValue, c);
                prediction = new Tuple<>(predictionValue, c);
            }
        }

        LOG.debug("highest prediction value is {} corresponding to class {}", prediction.first(), prediction.last());
        return prediction.last();
    }
}
