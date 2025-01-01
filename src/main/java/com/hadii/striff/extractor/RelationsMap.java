package com.hadii.striff.extractor;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.striff.diagram.DiagramComponent;

public class RelationsMap {

    /**
     * Map Structure.
     * Map < Original Component Unique Name, Map < Target Component, List[Relation
     * b/w Components] >>
     */
    private final Map<String, Map<Component, TreeSet<ComponentRelation>>> relMap = new HashMap<>();

    private int size = 0;

    public Set<ComponentRelation> rels(Component cmp) {
        if (hasRels(cmp.uniqueName())) {
            return this.relMap.get(cmp.uniqueName()).values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Returns the most significant relationship for every pair of the given
     * component and all other components it shares one or more relationships with.
     */
    public Set<ComponentRelation> significantRels(Component cmp) {
        return significantRels(cmp.uniqueName());
    }

    public Set<ComponentRelation> significantRels(String cmpUniqueName) {
        if (hasRels(cmpUniqueName)) {
            // Given the relations are stored in a Treeset, the most significant relation
            // is at the top of the list.
            HashSet<ComponentRelation> significantRels = new HashSet<>();
            this.relMap.get(cmpUniqueName).values()
                    .forEach(componentRelations -> significantRels.add(componentRelations.first()));
            return significantRels;
        } else {
            return Collections.emptySet();
        }
    }

    public Set<ComponentRelation> allRels() {
        Set<ComponentRelation> allRelations = new HashSet<>();
        this.relMap.values().forEach(diagramComponentSetMap -> allRelations
                .addAll(diagramComponentSetMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet())));
        return allRelations;
    }

    public void insertRelation(ComponentRelation rel) {
        if (!this.relMap.containsKey(rel.originalComponent().uniqueName())) {
            this.relMap.put(rel.originalComponent().uniqueName(), new HashMap<>());
        }
        if (!this.relMap.get(rel.originalComponent().uniqueName()).containsKey(rel.targetComponent())) {
            this.relMap.get(rel.originalComponent().uniqueName()).put(rel.targetComponent(), new TreeSet<>());
        }
        this.relMap.get(rel.originalComponent().uniqueName()).get(rel.targetComponent()).add(rel);
        this.size += 1;
    }

    public int size() {
        return this.size;
    }

    public boolean hasRels(String cmpUniqueName) {
        return this.relMap.containsKey(cmpUniqueName) && !this.relMap.get(cmpUniqueName).isEmpty();
    }

    public final boolean contains(ComponentRelation rel) {
        return this.relMap.containsKey(rel.originalComponent().uniqueName())
                && this.relMap.get(rel.originalComponent().uniqueName()).containsKey(rel.targetComponent())
                && this.relMap.get(rel.originalComponent().uniqueName()).get(rel.targetComponent()).contains(rel);
    }

    public final Set<ComponentRelation> relsByType(DiagramConstants.ComponentAssociation type) {
        return this.allRels().stream().filter(relation -> relation.associationType() == type)
                .collect(Collectors.toSet());
    }

    public final boolean isRelated(Component origin, Component target) {
        return this.relMap.containsKey(origin.uniqueName()) && this.relMap.get(origin.uniqueName()).containsKey(target)
                && !this.relMap.get(origin.uniqueName()).get(target).isEmpty();
    }

    /**
     * Returns the most significant relationship between the given original
     * and target component if it exists, otherwise and empty object is returned.
     */
    public ComponentRelation mostSignificantRelation(Component original,
            Component target) {
        ComponentRelation significantRel = new ComponentRelation();
        if (isRelated(original, target)) {
            significantRel = this.relMap.get(original.uniqueName()).get(target).stream()
                    .sorted(Comparator.comparing(ComponentRelation::strength).reversed()).collect(Collectors.toList())
                    .get(0);
        }
        return significantRel;
    }

    public Set<ComponentRelation> significantRels(Set<DiagramComponent> currPartition) {
        Set<ComponentRelation> relations = new HashSet<>();
        currPartition.forEach(cmp -> relations.addAll(this.significantRels(cmp.uniqueName())));
        return relations;
    }
}
