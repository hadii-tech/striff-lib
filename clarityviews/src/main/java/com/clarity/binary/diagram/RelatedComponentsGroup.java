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

    private static final int MAX_MATCHES_PER_COMPONENT = 3;

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
        this.desiredResultSetSize = 5;
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

            /**
             * Filter stage 1: get all the components that are involved in a
             * extends/implements with the key component. All super classes will
             * be place at the beginning of the list and all implementing/sub
             * classes will be placed at the end of the resulting list.
             */
            for (int j = 0; j < componentRelatedGroup.size(); j++) {
                int matches = 0;
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                    if (matches >= MAX_MATCHES_PER_COMPONENT) {
                        break;
                    }

                    final BinaryClassRelationship bCR = entry.getValue();
                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            // super class, place at beginning of list.
                            componentRelatedGroup.add(0, bCR.getClassB());
                            matches++;
                        }
                    }

                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getbSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            // implementing/sub class, place at beginning of
                            // list.
                            componentRelatedGroup.add(bCR.getClassB());
                            matches++;
                        }
                    }

                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getbSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            // super class, place at beginning of list.
                            componentRelatedGroup.add(0, bCR.getClassA());
                            matches++;
                        }
                    }

                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            // implementing/sub class, place at beginning of
                            // list.
                            componentRelatedGroup.add(bCR.getClassA());
                            matches++;
                        }
                    }
                }
            }

            /**
             * Filter stage 2: Start with the beginning of the current result
             * list and look for composition/aggregation relationships.
             */
            int count = componentRelatedGroup.size();
            for (int j = 0; j < count && componentRelatedGroup.size() < desiredResultSetSize; j++) {
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                    final BinaryClassRelationship bCR = entry.getValue();
                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.COMPOSITION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.COMPOSITION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            componentRelatedGroup.add(bCR.getClassB());
                            if (componentRelatedGroup.size() > desiredResultSetSize) {
                                break;
                            }
                        }
                    }
                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.COMPOSITION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.COMPOSITION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            componentRelatedGroup.add(bCR.getClassA());
                            if (componentRelatedGroup.size() > desiredResultSetSize) {
                                break;
                            }
                        }
                    }
                }
            }

            /**
             * Filter stage 3: If there is space, Repeat filter stage 1 on the
             * newly added components representing composition/aggregation
             * relationships.
             */
            for (int j = count; j < componentRelatedGroup.size()
                    && componentRelatedGroup.size() <= desiredResultSetSize; j++) {
                int matches = 0;
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                    if (matches >= MAX_MATCHES_PER_COMPONENT) {
                        break;
                    }

                    final BinaryClassRelationship bCR = entry.getValue();
                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            componentRelatedGroup.add(count, bCR.getClassB());
                            matches++;
                        }
                    }

                    if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getbSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassB())) {
                            componentRelatedGroup.add(bCR.getClassB());
                            matches++;
                        }
                    }

                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getbSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getbSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            componentRelatedGroup.add(count, bCR.getClassA());
                            matches++;
                        }
                    }

                    if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                            && (bCR.getaSideAssociation() == BinaryClassAssociation.GENERALISATION
                                    || bCR.getaSideAssociation() == BinaryClassAssociation.REALIZATION)) {
                        if (!componentRelatedGroup.contains(bCR.getClassA())) {
                            componentRelatedGroup.add(bCR.getClassA());
                            matches++;
                        }
                    }
                }
            }

            /**
             * Filter stage 4: If there is space, find any remaining weak
             * relationships.
             */
            for (int j = 0; j < componentRelatedGroup.size()
                    && componentRelatedGroup.size() <= desiredResultSetSize; j++) {
                for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {

                    if (componentRelatedGroup.size() > desiredResultSetSize) {
                        break;
                    }

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
