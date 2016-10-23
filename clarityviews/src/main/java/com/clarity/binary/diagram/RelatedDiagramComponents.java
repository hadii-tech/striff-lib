package com.clarity.binary.diagram;

import java.util.ArrayList;
import java.util.Map;

import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentInvocations;

public class RelatedDiagramComponents {

    private Map<String, Component>               diagramComponents;
    private Map<String, BinaryClassRelationship> diagramRelationships;
    private Component                            diagramComponent;
    private int                                  resultSetSize;

    /**
     * Representation of the related components in a diagram.
     *
     * @param diagramComponents
     *            All the components to be considered in the diagram.
     * @param diagramRelationships
     *            All the binary relationships in the diagram.
     * @param diagramComponent
     *            The key component being displayed in the diagram.
     * @param resultSetSize
     *            Desired result set size of the related components.
     */
    public RelatedDiagramComponents(final Map<String, Component> diagramComponents,
            final Map<String, BinaryClassRelationship> diagramRelationships, final Component diagramComponent,
            final int resultSetSize) {
        this.diagramComponents = diagramComponents;
        this.diagramRelationships = diagramRelationships;
        this.diagramComponent = diagramComponent;
        this.resultSetSize = resultSetSize;
    }

    /**
     * Creates a list of components who are closely related to the given component.
     */
    public ArrayList<Component> components() {
        final ArrayList<Component> diagramGroup = new ArrayList<Component>();
        diagramGroup.add(diagramComponent);
        int i = 0;
        // Filter stage 1: get all the components the key component extends/implements...
        while (i < diagramGroup.size()) {
            for (final ComponentInvocation extension : diagramGroup.get(i)
                    .componentInvocations(ComponentInvocations.EXTENSION)) {
                if (diagramComponents.containsKey(extension.invokedComponent())) {
                    diagramGroup.add(diagramComponents.get(extension.invokedComponent()));
                }
            }
            for (final ComponentInvocation implementation : diagramGroup.get(i)
                    .componentInvocations(ComponentInvocations.IMPLEMENTATION)) {
                if (diagramComponents.containsKey(implementation.invokedComponent())) {
                    diagramGroup.add(diagramComponents.get(implementation.invokedComponent()));
                }
            }
            i++;
        }
        // Filter stage 2: Get all the components that compose the key component...
        for (int j = diagramGroup.size() - 1; j > 0; j--) {
            for (final Map.Entry<String, BinaryClassRelationship> entry : diagramRelationships.entrySet()) {
                final BinaryClassRelationship bCR = entry.getValue();
                if (bCR.getClassA().uniqueName().equals(diagramGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.COMPOSITION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.COMPOSITION)) {
                    if (!diagramGroup.contains(bCR.getClassB())) {
                        diagramGroup.add(0, bCR.getClassB());
                    }
                }
                if (bCR.getClassB().uniqueName().equals(diagramGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.COMPOSITION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.COMPOSITION)) {
                    if (!diagramGroup.contains(bCR.getClassA())) {
                        diagramGroup.add(0, bCR.getClassA());
                    }
                }
            }
        }
        // Filter stage 3: get extends/implemented components of the newly added
        // components  from stage 2.
        i = 0;
        while (i < diagramGroup.size() && diagramGroup.size() <= resultSetSize) {

            for (final ComponentInvocation superClass : diagramGroup.get(i)
                    .componentInvocations(ComponentInvocations.EXTENSION)) {
                if (!diagramGroup.contains(diagramComponents.get(superClass.invokedComponent()))
                        && diagramComponents.containsKey(superClass.invokedComponent())) {
                    diagramGroup.add(diagramComponents.get(superClass.invokedComponent()));
                }
            }
            for (final ComponentInvocation implementClass : diagramGroup.get(i)
                    .componentInvocations(ComponentInvocations.IMPLEMENTATION)) {
                if (!diagramGroup.contains(diagramComponents.get(implementClass.invokedComponent()))
                        && diagramComponents.containsKey(implementClass.invokedComponent())) {
                    diagramGroup.add(diagramComponents.get(implementClass.invokedComponent()));
                }
            }
            i++;
        }
        // Filter stage 4: if there is space left, get plain aggregation relationships
        for (int j = diagramGroup.size() - 1; j > 0; j--) {
            for (final Map.Entry<String, BinaryClassRelationship> entry : diagramRelationships.entrySet()) {
                if (diagramGroup.size() >= resultSetSize) {
                    break;
                }
                final BinaryClassRelationship bCR = entry.getValue();
                if (bCR.getClassA().uniqueName().equals(diagramGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                    if (!diagramGroup.contains(bCR.getClassB())) {
                        diagramGroup.add(0, bCR.getClassB());
                    }
                }
                if (bCR.getClassB().uniqueName().equals(diagramGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                    if (!diagramGroup.contains(bCR.getClassA())) {
                        diagramGroup.add(0, bCR.getClassA());
                    }
                }
            }
        }
        // Filter stage 5: if there is space left, get any remaining even weaker relationships!
        for (int j = diagramGroup.size() - 1; j > 0; j--) {
            for (final Map.Entry<String, BinaryClassRelationship> entry : diagramRelationships.entrySet()) {
                if (diagramGroup.size() >= resultSetSize) {
                    break;
                }
                final BinaryClassRelationship bCR = entry.getValue();
                if (bCR.getClassA().uniqueName().equals(diagramGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                || bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                    if (!diagramGroup.contains(bCR.getClassB())) {
                        diagramGroup.add(bCR.getClassB());
                    }
                }
                if (bCR.getClassB().uniqueName().equals(diagramGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION
                                || bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                    if (!diagramGroup.contains(bCR.getClassA())) {
                        diagramGroup.add(bCR.getClassA());
                    }
                }
            }
        }
        for (final Map.Entry<String, BinaryClassRelationship> entry : diagramRelationships.entrySet()) {
            final BinaryClassRelationship relationship = entry.getValue();
            final Component cmpA = relationship.getClassA();
            final Component cmpB = relationship.getClassB();
            if (cmpA.uniqueName().equals(diagramComponent.uniqueName()) && !cmpB.uniqueName().equals(cmpA.uniqueName())
                    && ((relationship.getbSideAssociation() == BinaryClassAssociation.GENERALISATION)
                            || (relationship.getbSideAssociation() == BinaryClassAssociation.REALIZATION)
                            || (relationship.getbSideAssociation() == BinaryClassAssociation.COMPOSITION))) {
                diagramGroup.add(cmpB);
            } else if (cmpB.uniqueName().equals(diagramComponent.uniqueName())
                    && !cmpA.uniqueName().equals(cmpB.uniqueName())
                    && ((relationship.getaSideAssociation() == BinaryClassAssociation.GENERALISATION)
                            || (relationship.getaSideAssociation() == BinaryClassAssociation.COMPOSITION)
                            || (relationship.getaSideAssociation() == BinaryClassAssociation.REALIZATION))) {
                if (!diagramGroup.contains(cmpA)) {
                    diagramGroup.add(cmpA);
                }
            }
        }
        for (final Map.Entry<String, BinaryClassRelationship> entry : diagramRelationships.entrySet()) {
            if (diagramGroup.size() >= resultSetSize) {
                break;
            }
            final BinaryClassRelationship relationship = entry.getValue();
            final Component cmpA = relationship.getClassA();
            final Component cmpB = relationship.getClassB();
            if (cmpA.uniqueName().equals(diagramComponent.uniqueName()) && !cmpB.uniqueName().equals(cmpA.uniqueName())
                    && !diagramGroup.contains(cmpB)) {
                diagramGroup.add(cmpB);
            } else if (cmpB.uniqueName().equals(diagramComponent.uniqueName())
                    && !cmpB.uniqueName().equals(cmpA.uniqueName()) && !diagramGroup.contains(cmpA)) {
                diagramGroup.add(cmpA);
            }
            if (diagramGroup.size() >= resultSetSize) {
                return diagramGroup;
            }
        }
        return diagramGroup;
    }
}
