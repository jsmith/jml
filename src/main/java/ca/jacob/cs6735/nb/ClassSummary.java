package ca.jacob.cs6735.nb;

import ca.jacob.cs6735.util.Vector;

public class ClassSummary {
    private int classValue;
    private double classProbability;
    private Vector means;
    private Vector stdevs;

    public ClassSummary(int classValue, double classProbability, Vector means, Vector stdevs) {
        this.classValue = classValue;
        this.classProbability = classProbability;
        this.means = means;
        this.stdevs = stdevs;
    }

    public int getClassValue() {
        return classValue;
    }

    public void setClassValue(int classValue) {
        this.classValue = classValue;
    }

    public Vector getMeans() {
        return means;
    }

    public void setMeans(Vector means) {
        this.means = means;
    }

    public Vector getStdevs() {
        return stdevs;
    }

    public void setStdevs(Vector stdevs) {
        this.stdevs = stdevs;
    }

    public double getClassProbability() {
        return classProbability;
    }

    public void setClassProbability(double classProbability) {
        this.classProbability = classProbability;
    }

    @Override
    public String toString() {
        return "{"+classValue+": probability: "+classProbability+";means -> "+means+"; stdevs -> "+stdevs+"}";
    }
}
