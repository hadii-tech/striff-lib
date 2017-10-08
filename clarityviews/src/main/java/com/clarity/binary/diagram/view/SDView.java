package com.clarity.binary.diagram.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clarity.binary.ComponentSet;
import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.RelatedBaseComponentsGroup;
import com.clarity.binary.diagram.display.DiagramClassDisplayName;
import com.clarity.binary.diagram.display.DiagramMethodDisplayName;
import com.clarity.binary.diagram.plantuml.PUMLDiagram;
import com.clarity.binary.diagram.plantuml.PUMLDiagramDesciption;
import com.clarity.binary.diagram.plantuml.StructureDiffPUMLDiagramDesciption;
import com.clarity.binary.diagram.scheme.DiagramColorScheme;
import com.clarity.binary.diagram.scheme.LightDiagramColorScheme;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.binary.extractor.ClassRelationshipsExtractor;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;

import net.sourceforge.plantuml.svg.SDComponentDisplayInfo;
import net.sourceforge.plantuml.svg.SvgGraphics;

/**
 * Represents a Structure-Diff demonstrating the structural differences between
 * the two given code bases.
 */
public class SDView implements ClarityView, Serializable {

    private static final long serialVersionUID = -3125810981280395679L;
    private Diagram diagram;

    public SDView(DiagramColorScheme colorScheme, OOPSourceCodeModel olderModel, OOPSourceCodeModel newerModel,
            boolean callback, int maxSDSize) throws Exception {

        Map<String, BinaryClassRelationship> oldBinaryRelationships = new ClassRelationshipsExtractor<Object>()
                .generateBinaryClassRelationships(olderModel);
        Map<String, BinaryClassRelationship> newBinaryRelationships = new ClassRelationshipsExtractor<Object>()
                .generateBinaryClassRelationships(newerModel);

        // form a list of all components that exist in the newer code base but
        // not in the older code base.
        List<String> addedComponents = new ArrayList<String>();
        for (final Map.Entry<String, Component> entry : newerModel.getComponents().entrySet()) {
            if (!olderModel.getComponents().containsKey(entry.getKey())) {
                addedComponents.add(entry.getKey());
            }
        }

        // form a list of all components that do not exist in the newer code
        // base but do exist in the older code base.
        List<String> deletedComponents = new ArrayList<String>();
        for (final Map.Entry<String, Component> entry : olderModel.getComponents().entrySet()) {
            if (!newerModel.getComponents().containsKey(entry.getKey())) {
                deletedComponents.add(entry.getKey());
            }
        }

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

        if (addedComponents.isEmpty() && addedRelationships.isEmpty() && deletedComponents.isEmpty()
                && deletedRelationships.isEmpty()) {
            return;
        }

        // generate a list of components that are needed to draw a class diagram
        // for the added components
        Set<Component> keyAddedComponents = new RelatedBaseComponentsGroup(newerModel.getComponents(),
                newBinaryRelationships, addedComponents).components();

        // generate list of components that are needed to draw a class diagram
        // for the deleted components
        Set<Component> keyDeletedComponents = new RelatedBaseComponentsGroup(olderModel.getComponents(),
                oldBinaryRelationships, deletedComponents).components();

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

        // source code model representing the merging of the old and new code
        // base
        OOPSourceCodeModel mergedCodeBase = olderModel;
        mergedCodeBase.merge(newerModel);
        List<SDComponentDisplayInfo> displayComponents = new ArrayList<SDComponentDisplayInfo>();
        for (final Map.Entry<String, Component> entry : mergedCodeBase.getComponents().entrySet()) {
            if (addedComponents.contains(entry.getValue().uniqueName())) {
                // mark all the added components
                if (entry.getValue().componentType().isBaseComponent()) {
                    displayComponents.add(new SDComponentDisplayInfo(
                            new DiagramClassDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), true, false));
                } else if (entry.getValue().componentType().isMethodComponent()) {
                    displayComponents.add(new SDComponentDisplayInfo(
                            new DiagramMethodDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), true, false));
                } else {
                    displayComponents.add(new SDComponentDisplayInfo(entry.getValue().name(),
                            entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), true, false));

                }
            }
            if (deletedComponents.contains(entry.getValue().uniqueName())) {
                // mark all the deleted components
                displayComponents.add(new SDComponentDisplayInfo(
                        new DiagramClassDisplayName(entry.getValue().uniqueName()).value(),
                        entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), false, true));
            } else {
                // mark all the unchanged components
                if (entry.getValue().componentType().isBaseComponent()) {
                    displayComponents.add(new SDComponentDisplayInfo(
                            new DiagramClassDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), false, false));
                } else if (entry.getValue().componentType().isMethodComponent()) {
                    displayComponents.add(new SDComponentDisplayInfo(
                            new DiagramMethodDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), false, true));
                } else {
                    displayComponents.add(new SDComponentDisplayInfo(entry.getValue().name(),
                            entry.getValue().uniqueName(), entry.getValue().componentType().getValue(), false, true));
                }
            }
        }

        PUMLDiagramDesciption diffClarityView = new StructureDiffPUMLDiagramDesciption(diagramComponents,
                allRelationships, deletedRelationships, addedRelationships, deletedComponents, addedComponents,
                mergedCodeBase.getComponents());
        SvgGraphics.componentCallBack = callback;
        this.diagram = new PUMLDiagram(diffClarityView, colorScheme, displayComponents);
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
}
