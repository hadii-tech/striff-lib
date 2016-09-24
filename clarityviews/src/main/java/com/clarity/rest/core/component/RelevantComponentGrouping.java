package com.clarity.rest.core.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;

/**
 * A group of components consisting of components referenced by the given star
 * component.
 *
 * @author Muntazir Fadhel
 */
public class RelevantComponentGrouping implements ComponentGrouping {

    private final Component starComponent;
    private final Map<String, Component> components;

    public RelevantComponentGrouping(Component starComponent, Map<String, Component> components) {
        this.starComponent = starComponent;
        this.components = components;
    }

    @Override
    public List<String> componentGroup() {

        Component cmp = starComponent;
        final List<Component> relevantComponents = new ArrayList<Component>();
        final List<String> involvedComponents = new ArrayList<String>();
        int i = 0;
        while (i <= relevantComponents.size()) {

            if (cmp == null) {
                i++;
                continue;
            }

            involvedComponents.add(cmp.uniqueName());

            for (final ComponentInvocation invocation : cmp.componentInvocations()) {
                involvedComponents.add(invocation.invokedComponent());
            }

            for (final String child : cmp.children()) {
                if (components.containsKey(child)) {
                    relevantComponents.add(components.get(child));
                }
            }
            if (i < relevantComponents.size()) {
                cmp = components.get(relevantComponents.get(i).uniqueName());
            }
            i++;
        }
        return involvedComponents;
    }
}