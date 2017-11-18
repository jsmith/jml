package ca.jacob.jml;

import ca.jacob.jml.util.DataSet;

public interface Algorithm {
    Model fit(DataSet d);
}
