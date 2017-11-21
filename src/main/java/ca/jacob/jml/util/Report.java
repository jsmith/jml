package ca.jacob.jml.util;

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

    public void setAccuracies(Vector accuracies) {
        this.accuracies = accuracies;
    }

    public void combine(Report r) {
        accuracies.concat(r.getAccuracies());
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Mean Accuracy: ").append(String.format("%.3f",accuracies.mean())).append("\n")
                .append("Standard Devi: ").append(String.format("%.3f",accuracies.stdev())).append("\n")
                .toString();
    }

    public double mean() {
        return accuracies.mean();
    }
}
