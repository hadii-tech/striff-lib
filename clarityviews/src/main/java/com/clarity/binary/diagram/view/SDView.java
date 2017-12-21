package com.clarity.binary.diagram.view;

import com.clarity.binary.ComponentSet;
import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.diagram.RelatedBaseComponentsGroup;
import com.clarity.binary.diagram.plantuml.PUMLDiagram;
import com.clarity.binary.diagram.plantuml.PUMLDiagramDesciption;
import com.clarity.binary.diagram.plantuml.StructureDiffPUMLDiagramDesciption;
import com.clarity.binary.diagram.scheme.DiagramColorScheme;
import com.clarity.binary.diagram.scheme.LightDiagramColorScheme;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.binary.extractor.BinaryClassRelationshipExtractor;
import com.clarity.binary.extractor.SimplifiedBinaryClassRelationships;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;
import net.sourceforge.plantuml.Log;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a Structure-Diff demonstrating the structural differences between
 * the two given code bases.
 */
public class SDView implements ClarityView, Serializable {

    private static final long serialVersionUID = -3125810981280395679L;
    private Diagram diagram;

    public SDView(DiagramColorScheme colorScheme, OOPSourceCodeModel olderModel, OOPSourceCodeModel newerModel,
            boolean callback, int maxSDSize) throws Exception {

        Map<String, BinaryClassRelationship> oldBinaryRelationships = new BinaryClassRelationshipExtractor<Object>()
                .generateBinaryClassRelationships(olderModel);
        Map<String, BinaryClassRelationship> newBinaryRelationships = new BinaryClassRelationshipExtractor<Object>()
                .generateBinaryClassRelationships(newerModel);

        // form a list of all components that exist in the newer code base but
        // not in the older code base.
        List<String> addedComponents = new ArrayList<String>();
        for (final Map.Entry<String, Component> entry : newerModel.getComponents().entrySet()) {
            if (!olderModel.containsComponent(entry.getKey())) {
                addedComponents.add(entry.getKey());
            }
        }

        // form a list of all base components that exist in the newer code base
        // but not in the older code base.
        Set<String> addedBaseComponents = new HashSet<String>();
        addedComponents.forEach(s -> {
            Component cmp = newerModel.getComponent(s);
            while (cmp != null && !cmp.componentType().isBaseComponent()) {
                cmp = newerModel.getComponent(cmp.parentUniqueName());
            }
            if (cmp != null && cmp.componentType().isBaseComponent()) {
                addedBaseComponents.add(cmp.uniqueName());
            }
        });

        // form a list of all components that do not exist in the newer code
        // base but do exist in the older code base.
        List<String> deletedComponents = new ArrayList<String>();
        for (final Map.Entry<String, Component> entry : olderModel.getComponents().entrySet()) {
            if (!newerModel.containsComponent(entry.getKey())) {
                deletedComponents.add(entry.getKey());
            }
        }

        // form a list of all base components that do not exist in the newer
        // code
        // base but do exist in the older code base.
        Set<String> deletedBaseComponents = new HashSet<String>();
        deletedBaseComponents.forEach(s -> {
            Component cmp = olderModel.getComponent(s);
            while (cmp != null && !cmp.componentType().isBaseComponent()) {
                cmp = olderModel.getComponent(cmp.parentUniqueName());
            }
            if (cmp != null && cmp.componentType().isBaseComponent()) {
                deletedBaseComponents.add(cmp.uniqueName());

            }
        });

        // form a list of all binary relationships that exist in the newer code
        // base but not in the older code base.
        List<BinaryClassRelationship> addedRelationships = new ArrayList<BinaryClassRelationship>();
        for (final Map.Entry<String, BinaryClassRelationship> entry : newBinaryRelationships.entrySet()) {
            if (!oldBinaryRelationships.containsValue(entry.getValue())) {
                addedRelationships.add(entry.getValue());
            }
        }

        // form a list of all binary relationships that do not exist in the
        // newer code base but do exist in the older code base.
        List<BinaryClassRelationship> deletedRelationships = new ArrayList<BinaryClassRelationship>();
        for (final Map.Entry<String, BinaryClassRelationship> entry : oldBinaryRelationships.entrySet()) {
            if (!(newBinaryRelationships.containsValue(entry.getValue()))) {
                deletedRelationships.add(entry.getValue());
            }
        }

        if (((addedComponents.size() + deletedComponents.size()) < 3)
                && ((noStrongRelations(addedRelationships) + noStrongRelations(deletedRelationships)) < 3)) {
            Log.info("Source models are equivalent, returning..");
            return;
        }

        // generate a list of components that are needed to draw a class diagram
        // for the added components
        Set<Component> keyAddedComponents = new RelatedBaseComponentsGroup(newerModel.getComponents(),
                newBinaryRelationships, addedBaseComponents).components();

        // generate list of components that are needed to draw a class diagram
        // for the deleted components
        Set<Component> keyDeletedComponents = new RelatedBaseComponentsGroup(olderModel.getComponents(),
                oldBinaryRelationships, deletedBaseComponents).components();

        // generate a list of components needed to draw the entire diff diagram
        Set<Component> diagramComponents = new ComponentSet(keyAddedComponents, keyDeletedComponents).set();

        if (diagramComponents.size() > maxSDSize) {
            throw new Exception("Clarity-bot could not draw this diagram because it is too large!");
        }

        // generate a list of binary relationships needed to draw the entire
        // diff diagram
        Set<BinaryClassRelationship> allRelationships = new HashSet<BinaryClassRelationship>();
        allRelationships.addAll(newBinaryRelationships.values());
        allRelationships.addAll(oldBinaryRelationships.values());
        allRelationships = new SimplifiedBinaryClassRelationships(diagramComponents, allRelationships).relationships();

        // source code model representing the merging of the old and new code
        // base
        OOPSourceCodeModel mergedCodeBase = olderModel;
        mergedCodeBase.merge(newerModel);

        PUMLDiagramDesciption diffClarityView = new StructureDiffPUMLDiagramDesciption(diagramComponents,
                allRelationships, deletedRelationships, addedRelationships, deletedComponents, addedComponents,
                mergedCodeBase.getComponents(), colorScheme);
        this.diagram = new PUMLDiagram(diffClarityView, colorScheme);
    }

    public SDView(OOPSourceCodeModel olderModel, OOPSourceCodeModel newerModel, boolean callback) throws Exception {
        this(new LightDiagramColorScheme(), olderModel, newerModel, callback, 75);
    }

    public SDView(OOPSourceCodeModel olderModel, OOPSourceCodeModel newerModel, boolean callback, int maxSDSize)
            throws Exception {
        this(new LightDiagramColorScheme(), olderModel, newerModel, callback, maxSDSize);
    }

    @Override
    public Diagram view() {
        return this.diagram;
    }

    private int noStrongRelations(List<BinaryClassRelationship> relations) {
        int count = 0;
        for (BinaryClassRelationship relation : relations) {
            if (relation.getaSideAssociation().getStrength() > BinaryClassAssociation.WEAK_ASSOCIATION.getStrength()) {
                count++;
            }
            if (relation.getbSideAssociation().getStrength() > BinaryClassAssociation.WEAK_ASSOCIATION.getStrength()) {
                count++;
            }
        }
        return count;
    }
}
