package ca.jacob.jml;

import ca.jacob.jml.math.Vector;

public class Report {
    private Vector accuracies;

    public Report(Vector accuracies) {
        this.accuracies = accuracies;
    }

    public Report() {
        this.accuracies = new Vector();
    }

    public Vector getAccuracies() {
        return accuracies;
    }

    public void combine(Report r) {
        accuracies.concat(r.getAccuracies());
    }

    @Override
    public String toString() {
        return ""
                .concat("Mean Accuracy: ").concat(String.format("%.3f",this.accuracy())).concat("\n")
                .concat("Standard Devi: ").concat(String.format("%.3f",this.stdev())).concat("\n");
    }

    public double accuracy() {
        return accuracies.mean();
    }

    public double stdev() {
        return accuracies.stdev();
    }
}
