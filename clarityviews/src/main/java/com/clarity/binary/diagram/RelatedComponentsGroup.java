package com.clarity.binary.diagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.sourcemodel.Component;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;

/**
 * Represents a group of components that are related to each other.
 *
 */
public class RelatedComponentsGroup {

    @NotNull
    private Map<String, Component> allComponents;

    @NotNull
    private Map<String, BinaryClassRelationship> allRelationships;

    @NotNull
    @NotEmpty
    private List<String> mainComponents;

    @Size(min = 1)
    private int desiredResultSetSize;

    /**
     *
     * @param allComponents
     *            All the components to be considered.
     * @param allRelationships
     *            All the binary relationships between the components.
     * @param mainComponent
     *            The component all other components in the group must have a
     *            relation with.
     * @param desiredResultSetSize
     *            Desired result set size of the related component group.
     */
    public RelatedComponentsGroup(final Map<String, Component> allComponents,
            final Map<String, BinaryClassRelationship> allRelationships, final Component mainComponent,
            final int desiredResultSetSize) {
        this.allComponents = allComponents;
        this.allRelationships = allRelationships;
        this.mainComponents = new ArrayList<String>();
        this.mainComponents.add(mainComponent.uniqueName());
        this.desiredResultSetSize = desiredResultSetSize;
    }

    /**
     *
     * @param allComponents
     *            All the components to be considered.
     * @param allRelationships
     *            All the binary relationships between the components to be
     *            considered.
     * @param mainComponents
     *            A list of components that must be are the basis of and must be
     *            included in the result set.
     */
    public RelatedComponentsGroup(final Map<String, Component> allComponents,
            final Map<String, BinaryClassRelationship> allRelationships, final List<String> mainComponents) {
        this.allComponents = allComponents;
        this.allRelationships = allRelationships;
        this.mainComponents = mainComponents;
        this.desiredResultSetSize = 6;
    }

    /**
     * Creates a list of components who are closely related to the given
     * components.
     */
    public Set<Component> components() {

        final Set<Component> overallRelatedGroup = new HashSet<Component>();
        for (String cmpName : mainComponents) {
            Component cmp = allComponents.get(cmpName);

            // if one of the main components is not a base component (eg: a
            // method or variable), get its parent base component.
            while (!cmp.componentType().isBaseComponent() && cmp != null) {
                cmp = allComponents.get(cmp.parentUniqueName());
            }

            final List<Component> componentRelatedGroup = new ArrayList<Component>();
            componentRelatedGroup.add(cmp);

            // Filter stage 1: Get all the components that are involved in a
            // composition with the key elements up to the desired size
            for (int j = 0; j < componentRelatedGroup.size()
                    && componentRelatedGroup.size() < desiredResultSetSize; j++) {
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                    final BinaryClassRelationship bCR = entry.getValue();
                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.COMPOSITION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.COMPOSITION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            componentRelatedGroup.add(0, bCR.getClassB());
                            if (componentRelatedGroup.size() > desiredResultSetSize) {
                                break;
                            }
                        }
                    }
                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.COMPOSITION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.COMPOSITION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            componentRelatedGroup.add(0, bCR.getClassA());
                            if (componentRelatedGroup.size() > desiredResultSetSize) {
                                break;
                            }
                        }
                    }
                }
            }

            // Filter stage 2: get all the components that are involved in a
            // extends/implements with the key component
            for (int j = 0; j < componentRelatedGroup.size(); j++) {
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                    final BinaryClassRelationship bCR = entry.getValue();
                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.REALIZATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            componentRelatedGroup.add(0, bCR.getClassB());
                        }
                    }
                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getbSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.REALIZATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            componentRelatedGroup.add(0, bCR.getClassA());
                        }
                    }
                }
            }

            // Filter stage 3: if there is space left, get any remaining even
            // weaker relationships up to the desired size
            for (int j = 0; j < componentRelatedGroup.size()
                    && componentRelatedGroup.size() <= desiredResultSetSize; j++) {
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {

                    final BinaryClassRelationship bCR = entry.getValue();
                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            componentRelatedGroup.add(bCR.getClassB());
                        }
                    }
                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            componentRelatedGroup.add(bCR.getClassA());
                        }
                    }
                }
            }
            // add all the components related to the current component to the
            // overall component group
            overallRelatedGroup.addAll(componentRelatedGroup);
        }
        return overallRelatedGroup;
    }
}
