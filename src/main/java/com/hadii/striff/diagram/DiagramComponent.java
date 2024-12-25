package com.hadii.striff.diagram;

import com.hadii.clarpse.reference.ComponentReference;
import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.clarpse.sourcemodel.OOPSourceModelConstants;
import com.hadii.clarpse.sourcemodel.Package;
import com.hadii.striff.metrics.MetricChange;

import java.util.*;

/**
 * Represents the building blocks of a Striff diagram.
 */
public class DiagramComponent {

    private final Component cmp;
    private final List<String> children = new ArrayList<>();
    private MetricChange metricChange;

    /**
     * Creates a DiagramComponent from a Component and optionally populates its children.
     *
     * @param cmp      Underlying component
     * @param srcModel Source code model (may be null)
     */
    public DiagramComponent(Component cmp, OOPSourceCodeModel srcModel) {
        this.cmp = cmp == null ? new Component() : cmp;
        if (srcModel != null) {
            this.cmp.children().stream()
                    .filter(child -> srcModel.getComponent(child).isPresent())
                    .forEach(children::add);
        }
    }

    /**
     * Creates a DiagramComponent with a dummy underlying Component whose name is set
     * to {@code componentName}. For testing or placeholder usage.
     *
     * @param componentName Name of the dummy component.
     */
    public DiagramComponent(String componentName) {
        this(new Component(), null);
        this.cmp.setComponentName(componentName);
    }

    /**
     * Creates a DiagramComponent with a placeholder Component name and an optional MetricChange.
     *
     * @param cmpName       Name of the placeholder component
     * @param metricChange  Metric change data 
     * @param srcModel      Source code model (may be null)
     */
    public DiagramComponent(String cmpName, MetricChange metricChange, OOPSourceCodeModel srcModel) {
        this(srcModel.getComponent(cmpName).get(), srcModel);
        this.metricChange = metricChange;
    }

    /**
     * @return The MetricChange object associated with this DiagramComponent (may be null).
     */
    public MetricChange getMetricChange() {
        return metricChange;
    }

    /**
     * Checks whether this DiagramComponent has a non-null MetricChange.
     *
     * @return true if metricChange is not null, false otherwise
     */
    public boolean hasMetricChange() {
        return metricChange != null;
    }

    public List<String> children() {
        return Collections.unmodifiableList(this.children);
    }

    public String uniqueName() {
        return cmp.uniqueName();
    }

    public List<ComponentReference> references(OOPSourceModelConstants.TypeReferences implementation) {
        return cmp.references(implementation);
    }

    public Set<String> modifiers() {
        return this.cmp.modifiers();
    }

    public OOPSourceModelConstants.ComponentType componentType() {
        return this.cmp.componentType();
    }

    public String parentUniqueName() {
        return this.cmp.parentUniqueName();
    }

    public Set<ComponentReference> references() {
        return this.cmp.references();
    }

    public String name() {
        return this.cmp.name();
    }

    public String codeFragment() {
        return this.cmp.codeFragment();
    }

    public int componentHashCode() {
        return this.cmp.codeHash();
    }

    public String comment() {
        return this.cmp.comment();
    }

    public String sourceFile() {
        return this.cmp.sourceFile();
    }

    public void setName(String name) {
        this.cmp.setName(name);
    }

    public Package pkg() {
        return this.cmp.pkg();
    }

    public String componentName() {
        return this.cmp.componentName();
    }

    /**
     * Fetches the current component's parent base component if it exists, returning null otherwise.
     *
     * @param codeBase A map of uniqueName -> DiagramComponent representing the entire code base
     * @return The parent base DiagramComponent, or null if none is found.
     */
    public DiagramComponent parentBaseCmp(Map<String, DiagramComponent> codeBase) {
        String currParentClassName = this.cmp.parentUniqueName();
        DiagramComponent parent = codeBase.get(currParentClassName);
        while (parent != null && !parent.componentType().isBaseComponent()) {
            currParentClassName = parent.parentUniqueName();
            parent = codeBase.get(currParentClassName);
        }
        return parent;
    }

    @Override
    public int hashCode() {
        return this.uniqueName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DiagramComponent)) {
            return false;
        }
        DiagramComponent other = (DiagramComponent) obj;
        return Objects.equals(this.uniqueName(), other.uniqueName());
    }

    @Override
    public String toString() {
        return this.uniqueName();
    }
}
