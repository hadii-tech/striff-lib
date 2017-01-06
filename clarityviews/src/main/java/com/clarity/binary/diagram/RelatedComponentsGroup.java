package com.clarity.binary.diagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;

import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentInvocations;

/**
 * Represents a group of components that are related to each other.
 *
 */
public class RelatedComponentsGroup {

	@NotNull
	private Map<String, Component>               allComponents;
	
	@NotNull
	private Map<String, BinaryClassRelationship> allRelationships;
	
	@NotNull
	@NotEmpty
	private List<String>                            mainComponents;
	
	@Size(min=1)
	private int                                  desiredResultSetSize;

	/**
     *
     * @param allComponents
     *            All the components to be considered.
     * @param allRelationships
     *            All the binary relationships between the components.
     * @param mainComponent
     *           The component all other components in the group must have a relation with.
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
    *            All the binary relationships between the components.
    * @param mainComponents
    *           A list of components all other components in the group must have a relation with.
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
			final List<Component> componentRelatedGroup = new ArrayList<Component>();
			componentRelatedGroup.add(cmp);
			int i = 0;
			// Filter stage 1: get all the components the key component
			// extends/implements...
			while (i < componentRelatedGroup.size()) {
				for (final ComponentInvocation extension : componentRelatedGroup.get(i)
						.componentInvocations(ComponentInvocations.EXTENSION)) {
					if (allComponents.containsKey(extension.invokedComponent())) {
						componentRelatedGroup.add(allComponents.get(extension.invokedComponent()));
					}
            }
            for (final ComponentInvocation implementation : componentRelatedGroup.get(i)
                    .componentInvocations(ComponentInvocations.IMPLEMENTATION)) {
                if (allComponents.containsKey(implementation.invokedComponent())) {
                    componentRelatedGroup.add(allComponents.get(implementation.invokedComponent()));
                }
            }
            i++;
        }
        // Filter stage 2: Get all the components that compose the key
        // component...
        for (int j = componentRelatedGroup.size() - 1; j > 0; j--) {
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
        // Filter stage 3: get extends/implemented components of the newly added
        // components from stage 2.
        i = 0;
        while (i < componentRelatedGroup.size() && componentRelatedGroup.size() <= desiredResultSetSize) {

            for (final ComponentInvocation superClass : componentRelatedGroup.get(i)
                    .componentInvocations(ComponentInvocations.EXTENSION)) {
                if (!componentRelatedGroup.contains(allComponents.get(superClass.invokedComponent()))
                        && allComponents.containsKey(superClass.invokedComponent())) {
                    componentRelatedGroup.add(allComponents.get(superClass.invokedComponent()));
                }
            }
            for (final ComponentInvocation implementClass : componentRelatedGroup.get(i)
                    .componentInvocations(ComponentInvocations.IMPLEMENTATION)) {
                if (!componentRelatedGroup.contains(allComponents.get(implementClass.invokedComponent()))
                        && allComponents.containsKey(implementClass.invokedComponent())) {
                    componentRelatedGroup.add(allComponents.get(implementClass.invokedComponent()));
                }
            }
            i++;
        }
        // Filter stage 4: if there is space left, get plain aggregation
        // relationships
        for (int j = componentRelatedGroup.size() - 1; j > 0; j--) {
            for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                if (componentRelatedGroup.size() >= desiredResultSetSize) {
                    break;
                }
                final BinaryClassRelationship bCR = entry.getValue();
                if (bCR.getClassA().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                    if (!componentRelatedGroup.contains(bCR.getClassB())) {
                        componentRelatedGroup.add(0, bCR.getClassB());
                    }
                }
                if (bCR.getClassB().uniqueName().equals(componentRelatedGroup.get(j).uniqueName())
                        && (bCR.getaSideAssociation() == BinaryClassAssociation.AGGREGATION
                                || bCR.getbSideAssociation() == BinaryClassAssociation.AGGREGATION)) {
                    if (!componentRelatedGroup.contains(bCR.getClassA())) {
                        componentRelatedGroup.add(0, bCR.getClassA());
                    }
                }
            }
        }
        
        // Filter stage 5: if there is space left, get any remaining even weaker
        // relationships!
        for (int j = componentRelatedGroup.size() - 1; j > 0; j--) {
            for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
                if (componentRelatedGroup.size() >= desiredResultSetSize) {
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
        for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
        	final BinaryClassRelationship relationship = entry.getValue();
        	final Component cmpA = relationship.getClassA();
        	final Component cmpB = relationship.getClassB();
        	if (cmpA.uniqueName().equals(cmp.uniqueName()) && !cmpB.uniqueName().equals(cmpA.uniqueName())
        			&& ((relationship.getbSideAssociation() == BinaryClassAssociation.GENERALISATION)
        					|| (relationship.getbSideAssociation() == BinaryClassAssociation.REALIZATION)
        					|| (relationship.getbSideAssociation() == BinaryClassAssociation.COMPOSITION))) {
        		componentRelatedGroup.add(cmpB);
        	} else if (cmpB.uniqueName().equals(cmp.uniqueName())
        			&& !cmpA.uniqueName().equals(cmpB.uniqueName())
        			&& ((relationship.getaSideAssociation() == BinaryClassAssociation.GENERALISATION)
        					|| (relationship.getaSideAssociation() == BinaryClassAssociation.COMPOSITION)
        					|| (relationship.getaSideAssociation() == BinaryClassAssociation.REALIZATION))) {
        		if (!componentRelatedGroup.contains(cmpA)) {
        			componentRelatedGroup.add(cmpA);
        		}
        	}
        }
        for (final Map.Entry<String, BinaryClassRelationship> entry : allRelationships.entrySet()) {
        	if (componentRelatedGroup.size() >= desiredResultSetSize) {
        		break;
        	}
        	final BinaryClassRelationship relationship = entry.getValue();
        	final Component cmpA = relationship.getClassA();
        	final Component cmpB = relationship.getClassB();
        	if (cmpA.uniqueName().equals(cmp.uniqueName()) && !cmpB.uniqueName().equals(cmpA.uniqueName())
        			&& !componentRelatedGroup.contains(cmpB)) {
        		componentRelatedGroup.add(cmpB);
        	} else if (cmpB.uniqueName().equals(cmp.uniqueName())
        			&& !cmpB.uniqueName().equals(cmpA.uniqueName()) && !componentRelatedGroup.contains(cmpA)) {
        		componentRelatedGroup.add(cmpA);
        	}
        }
        
        // add all the components related to the current component to the overall
        // component group
        overallRelatedGroup.addAll(componentRelatedGroup);
    	}

    	return overallRelatedGroup;
    }
}
