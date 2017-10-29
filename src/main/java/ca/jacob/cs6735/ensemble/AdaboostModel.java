package ca.jacob.cs6735.ensemble;

import ca.jacob.cs6735.Model;

import java.util.ArrayList;
import java.util.List;

public class AdaboostModel implements Model {
    private List<Model> models;

    @Override
    public Integer predict(Integer[] e) {
        return 0;
    }

    @Override
    public Integer[] predict(Integer[][] data) {
        return new Integer[0];
    }

    public void add(Model model) {
        if(models == null) {
            models = new ArrayList<Model>();
        }
        models.add(model);
    }
}
