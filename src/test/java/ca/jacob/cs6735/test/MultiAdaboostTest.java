package ca.jacob.cs6735.test;

import ca.jacob.jml.KFold;
import ca.jacob.jml.dt.ID3;
import ca.jacob.jml.ensemble.AdaboostModel;
import ca.jacob.jml.ensemble.MultiAdaboost;
import ca.jacob.jml.ensemble.MultiAdaboostModel;
import ca.jacob.jml.util.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.jacob.cs6735.DataUtil.loadCarData;
import static ca.jacob.jml.util.DataSet.DISCRETE;
import static org.junit.Assert.assertEquals;

public class MultiAdaboostTest {
    private static final Logger LOG = LoggerFactory.getLogger(MultiAdaboostTest.class);

    @Test
    public void testFit() {
        ID3 id3 = new ID3(1);
        MultiAdaboost multiAdaboost = new MultiAdaboost(id3, 100, 0.5);

        Matrix data = new Matrix(new int[][]{
            {1, 1, 1, 1},
            {1, 0, 1, 2},
            {1, 0, 0, 3},
        });
        DataSet d = new DataSet(data, DISCRETE);

        MultiAdaboostModel m = (MultiAdaboostModel) multiAdaboost.fit(d);
        List<Tuple<Integer, AdaboostModel>> models = m.getModels();

        assertEquals(3, models.size());
    }

    @Test
    public void testMultiAdaboost() throws Throwable {
        ID3 id3 = new ID3(1);
        MultiAdaboost multiAdaboost = new MultiAdaboost(id3, 300, 0.3);

        DataSet d = loadCarData(MultiAdaboostTest.class);

        KFold kFold = new KFold(5, 23l);
        Report r = kFold.generateReport(multiAdaboost, d);

        double mean = r.mean();
        LOG.info("Multi Adaboost Mean for Car Data: {}", mean);
    }
}
