package com.hadii.striff;

import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.striff.extractor.ExtractedRelationships;
import com.hadii.striff.extractor.RelationsMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the differences between a primary and secondary code base.
 */
public final class ChangeSet {

    private final RelationsMap deletedRelations = new RelationsMap();
    private final RelationsMap addedRelations = new RelationsMap();
    private final Set<String> addedComponents = new HashSet<>();
    private final Set<String> deletedComponents = new HashSet<>();
    private final Set<String> keyRelationsComponents = new HashSet<>();
    private final Set<String> modifiedComponents = new HashSet<>();
    private static final Logger LOGGER = LogManager.getLogger(ChangeSet.class);

    /**
     * A representation of the changes between an original and final code base.
     *
     * @param oldModel The original source code model
     * @param newModel The final source code model
     */
    public ChangeSet(OOPSourceCodeModel oldModel, OOPSourceCodeModel newModel) {
        LOGGER.info("Generating changeset between old and new code models..");
        RelationsMap oldExtractedRels = new ExtractedRelationships(oldModel).result();
        RelationsMap newExtractedRels = new ExtractedRelationships(newModel).result();

        // Form a list of all newly created components.
        newModel.components().filter(cmp -> !oldModel.containsComponent(cmp.uniqueName()))
                .forEach(cmp -> this.addedComponents.add(cmp.uniqueName()));
        LOGGER.info("Found " + this.addedComponents.size() + " added components.");

        // Form a list of all deleted components.
        oldModel.components().filter(cmp -> !newModel.containsComponent(cmp.uniqueName()))
                .forEach(cmp -> this.deletedComponents.add(cmp.uniqueName()));
        LOGGER.info("Found " + this.addedComponents.size() + " deleted components.");

        // Form a list of all the new component relationships.
        newExtractedRels.allRels().forEach(relation -> {
            if (!oldExtractedRels.contains(relation)) {
                this.addedRelations.insertRelation(relation);
                this.addKeyRelComponents(relation.originalComponent(), relation.targetComponent());
            }
        });
        LOGGER.info("Found " + this.addedRelations.size() + " added relations.");

        // Form a list of all relationships that do not exist anymore.
        oldExtractedRels.allRels().forEach(relation -> {
            if (!newExtractedRels.contains(relation)) {
                this.deletedRelations.insertRelation(relation);
                this.addKeyRelComponents(relation.originalComponent(), relation.targetComponent());
            }
        });
        LOGGER.info("Found " + this.deletedRelations.size() + " deleted relations.");

        // Form a list of all components whose implementations have changed.
        newModel.components().filter(cmp -> oldModel.containsComponent(cmp.uniqueName())).forEach(cmp -> {
            if (cmp.hashCode() != oldModel.getComponent(cmp.uniqueName()).get().hashCode()) {
                this.modifiedComponents.add(cmp.uniqueName());
            }
        });
        LOGGER.info("Found " + this.modifiedComponents.size() + " modified components.");
    }

    private void addKeyRelComponents(Component... keyRelCmps) {
        for (Component keyRelCmp : keyRelCmps) {
            if (!this.addedComponents.contains(keyRelCmp.uniqueName())
                    && !this.deletedComponents.contains(keyRelCmp.uniqueName())) {
                this.keyRelationsComponents.add(keyRelCmp.uniqueName());
            }
        }
    }

    public Set<String> addedComponents() {
        return addedComponents;
    }

    public boolean inAddedComponents(String cmpUniqueName) {
        return this.addedComponents.stream().filter(cmp -> cmp.equals(cmpUniqueName))
                .collect(Collectors.toList()).size() > 0;
    }

    public boolean inDeletedComponents(String cmpUniqueName) {
        return this.deletedComponents.stream().filter(cmp -> cmp.equals(cmpUniqueName))
                .collect(Collectors.toList()).size() > 0;
    }

    public boolean inKeyRelationComponents(String cmpUniqueName) {
        return this.keyRelationsComponents.stream().filter(cmp -> cmp.equals(cmpUniqueName))
                .collect(Collectors.toList()).size() > 0;
    }

    public Set<String> deletedComponents() {
        return deletedComponents;
    }

    public Set<String> keyRelationsComponents() {
        return this.keyRelationsComponents;
    }

    public RelationsMap addedRelations() {
        return this.addedRelations;
    }

    public RelationsMap deletedRelations() {
        return this.deletedRelations;
    }

    public Set<String> modifiedComponents() {
        return this.modifiedComponents;
    }
}
