package ca.jacob.cs6735.util;

public class Data {
    private static final int DISCRETE = 0;
    private static final int CONTINUOUS = 1;

    private Matrix x;
    private Vector y;
    private Vector dataTypes;

    public Data(Matrix x, Vector y, Vector dataTypes) {
        if(x.colCount() != dataTypes.length()) {
            throw new DataException("data types length must match attribute count");
        }

        this.x = x;
        this.y = y;
        this.dataTypes = dataTypes;
    }
}
