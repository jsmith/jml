package ca.jacob.jml.ensemble;

import ca.jacob.jml.Model;
import ca.jacob.jml.math.Tuple;
import ca.jacob.jml.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MultiAdaboostModel extends Model {
    private static final Logger LOG = LoggerFactory.getLogger(MultiAdaboostModel.class);

    List<Tuple<Integer, AdaboostModel>> adaboostModels;

    public MultiAdaboostModel(List<Tuple<Integer, AdaboostModel>> adaboostModels) {
        this.adaboostModels = adaboostModels;
    }

    public List<Tuple<Integer, AdaboostModel>> getModels() {
        return adaboostModels;
    }

    @Override
    public int predict(Vector e) {
        Tuple<Double, Integer> prediction = null;
        for(Tuple<Integer, AdaboostModel> adaboostModel : adaboostModels) {
            int c = adaboostModel.first();
            AdaboostModel model = adaboostModel.last();
            double predictionValue = model.prediction(e);
            if(prediction == null || prediction.first() < predictionValue) {
                LOG.debug("highest prediction value is now {} corresponding to class {}", predictionValue, c);
                prediction = new Tuple<>(predictionValue, c);
            }
        }
        return prediction.last();
    }
}
