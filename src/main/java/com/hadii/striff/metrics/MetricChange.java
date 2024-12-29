package com.hadii.striff.metrics;

public class MetricChange {
    private final String className;
    private final double oldNOC;
    private final double updatedNOC;
    private final double oldDIT;
    private final double updatedDIT;
    private final double oldWMC;
    private final double updatedWMC;
    private final double oldAC;
    private final double updatedAC;
    private final double oldEC;
    private final double updatedEC;
    private final double oldEncapsulation;
    private final double updatedEncapsulation;

    public MetricChange(String className,
            double oldNOC, double updatedNOC,
            double oldDIT, double updatedDIT,
            double oldWMC, double updatedWMC,
            double oldAC, double updatedAC,
            double oldEC, double updatedEC,
            double oldEncapsulation, double updatedEncapsulation) {
        this.className = className;
        this.oldNOC = oldNOC;
        this.updatedNOC = updatedNOC;
        this.oldDIT = oldDIT;
        this.updatedDIT = updatedDIT;
        this.oldWMC = oldWMC;
        this.updatedWMC = updatedWMC;
        this.oldAC = oldAC;
        this.updatedAC = updatedAC;
        this.oldEC = oldEC;
        this.updatedEC = updatedEC;
        this.oldEncapsulation = oldEncapsulation;
        this.updatedEncapsulation = updatedEncapsulation;
    }

    @Override
    public String toString() {
        return String.format("Metric changes for class '%s':\n"
                + "- NOC: %.2f -> %.2f\n"
                + "- DIT: %.2f -> %.2f\n"
                + "- WMC: %.2f -> %.2f\n"
                + "- Afferent Coupling: %.2f -> %.2f\n"
                + "- Efferent Coupling: %.2f -> %.2f\n"
                + "- Encapsulation: %.2f -> %.2f\n",
                className, oldNOC, updatedNOC, oldDIT, updatedDIT, oldWMC, updatedWMC,
                oldAC, updatedAC, oldEC, updatedEC, oldEncapsulation, updatedEncapsulation);
    }

    public String getClassName() {
        return className;
    }

    public double getOldNOC() {
        return oldNOC;
    }

    public double getUpdatedNOC() {
        return updatedNOC;
    }

    public double getOldDIT() {
        return oldDIT;
    }

    public double getUpdatedDIT() {
        return updatedDIT;
    }

    public double getOldWMC() {
        return oldWMC;
    }

    public double getUpdatedWMC() {
        return updatedWMC;
    }

    public double getOldAC() {
        return oldAC;
    }

    public double getUpdatedAC() {
        return updatedAC;
    }

    public double getOldEC() {
        return oldEC;
    }

    public double getUpdatedEC() {
        return updatedEC;
    }

    public double getOldEncapsulation() {
        return oldEncapsulation;
    }

    public double getUpdatedEncapsulation() {
        return updatedEncapsulation;
    }
}
