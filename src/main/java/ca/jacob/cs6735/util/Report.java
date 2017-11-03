package ca.jacob.cs6735.util;

public class Report {
    private Vector accuracies;

    public Report(Vector accuracies) {
        this.accuracies = accuracies;
    }

    public Vector getAccuracies() {
        return accuracies;
    }

    public void setAccuracies(Vector accuracies) {
        this.accuracies = accuracies;
    }
}
