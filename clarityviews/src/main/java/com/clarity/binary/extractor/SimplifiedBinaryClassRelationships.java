package com.clarity.binary.extractor;

import java.util.HashSet;
import java.util.Set;

import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.sourcemodel.Component;

public class SimplifiedBinaryClassRelationships {

    private Set<BinaryClassRelationship> relationships;
    private Set<Component> importantComponents;

    public SimplifiedBinaryClassRelationships(Set<Component> importantComponents,
            Set<BinaryClassRelationship> relationships) {
        this.relationships = relationships;
        this.importantComponents = importantComponents;
    }

    public Set<BinaryClassRelationship> relationships() {
        final Set<BinaryClassRelationship> newRelations = new HashSet<BinaryClassRelationship>();
        for (final BinaryClassRelationship tmpRelation : relationships) {
            if ((tmpRelation.getaSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION

                    || tmpRelation.getbSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION)
                    && (!importantComponents.contains(tmpRelation.getClassA())
                            && !importantComponents.contains(tmpRelation.getClassB()))) {
                continue;
            } else {
                newRelations.add(tmpRelation);
            }
        }
        return newRelations;
    }
}
