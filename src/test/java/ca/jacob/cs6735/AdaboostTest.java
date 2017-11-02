package ca.jacob.cs6735;

import ca.jacob.cs6735.dt.ID3;
import ca.jacob.cs6735.ensemble.Adaboost;
import org.junit.Test;

public class AdaboostTest {
    @Test
    public void testTrain() {
        ID3 id3 = new ID3(1); // stumps
        Adaboost adaboost = new Adaboost(id3, 10);
        adaboost.
    }
}
