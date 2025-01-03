package com.hadii.striff.metrics;

import com.hadii.clarpse.reference.ComponentReference;
import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.clarpse.sourcemodel.OOPSourceModelConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a Number of Children metric, which is a measure of the the number
 * of direct subclasses of a class.
 */
public class NOCMetric implements Metric {

    private final Map<String, Double> subclasses = new HashMap<>();

    public NOCMetric(OOPSourceCodeModel srcModel) {
        calculate(srcModel);
    }

    private void calculate(OOPSourceCodeModel srcModel) {
        for (Component tempCmp : srcModel.components().collect(Collectors.toList())) {
            if (tempCmp.componentType().isBaseComponent()) {
                for (ComponentReference cmpRef : tempCmp
                        .references(OOPSourceModelConstants.TypeReferences.EXTENSION)) {
                    subclasses.put(cmpRef.invokedComponent(),
                            subclasses.getOrDefault(cmpRef.invokedComponent(), 0.0) + 1);
                }
            }
        }
    }

    public double value(String cmpId) {
        return this.subclasses.getOrDefault(cmpId, 0.0);
    }

    @Override
    public String acronym() {
        return "NOC";
    }
}
