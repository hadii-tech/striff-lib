package com.clarity.binary.diagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clarity.binary.ComponentSet;
import com.clarity.binary.diagram.display.DiagramClassDisplayName;
import com.clarity.binary.diagram.display.DiagramMethodDisplayName;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;

import net.sourceforge.plantuml.svg.ComponentDisplayInfo;
import net.sourceforge.plantuml.svg.SvgGraphics;

/**
 * Generates a Clarity View demonstrating the differences between the two given
 * code bases.
 */
public class DiffClarityView implements ClarityView, Serializable {

    private static final long serialVersionUID = -3125810981280395679L;
    private ClassDiagram diagram;

    public DiffClarityView(ClassDiagramColorScheme colorScheme,
            Map<String, BinaryClassRelationship> oldBinaryRelationships,
            Map<String, BinaryClassRelationship> newBinaryRelationships, OOPSourceCodeModel olderModel,
            OOPSourceCodeModel newerModel, boolean callback) throws Exception {

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

        // generate a list of components that are needed to draw a class diagram
        // for the added components
        Set<Component> keyAddedComponents = new RelatedComponentsGroup(newerModel.getComponents(),
                newBinaryRelationships, addedComponents).components();

        // generate list of components that are needed to draw a class diagram
        // for the deleted components
        Set<Component> keyDeletedComponents = new RelatedComponentsGroup(olderModel.getComponents(),
                oldBinaryRelationships, deletedComponents).components();

        // generate a list of components needed to draw the entire diff diagram
        Set<Component> diagramComponents = new ComponentSet(keyAddedComponents, keyDeletedComponents).set();

        // generate a list of binary relationships needed to draw the entire
        // diff diagram
        Set<BinaryClassRelationship> allRelationships = new HashSet<BinaryClassRelationship>();
        allRelationships.addAll(newBinaryRelationships.values());
        allRelationships.addAll(oldBinaryRelationships.values());

        // source code model representing the merging of the old and new code
        // base
        OOPSourceCodeModel mergedCodeBase = olderModel;
        mergedCodeBase.merge(newerModel);

        for (final Map.Entry<String, Component> entry : mergedCodeBase.getComponents().entrySet()) {
            if (addedComponents.contains(entry.getValue().uniqueName())) {
                // mark all the newly added components green
                if (entry.getValue().componentType().isBaseComponent()) {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(
                            new DiagramClassDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), "#22df80", entry.getValue().componentType().getValue()));

                } else if (entry.getValue().componentType().isMethodComponent()) {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(
                            new DiagramMethodDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), "#22df80", entry.getValue().componentType().getValue()));
                } else {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(entry.getValue().name(),
                            entry.getValue().uniqueName(), "#22df80", entry.getValue().componentType().getValue()));
                }
            } else if (deletedComponents.contains(entry.getValue().uniqueName())) {
                // mark all the deleted components red
                if (entry.getValue().componentType().isBaseComponent()) {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(
                            new DiagramClassDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), "#F97D7D", entry.getValue().componentType().getValue()));

                } else if (entry.getValue().componentType().isMethodComponent()) {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(
                            new DiagramMethodDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), "#F97D7D", entry.getValue().componentType().getValue()));
                } else {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(entry.getValue().name(),
                            entry.getValue().uniqueName(), "#F97D7D", entry.getValue().componentType().getValue()));
                }
            } else {
                // mark all the unchanged components gray
                if (entry.getValue().componentType().isBaseComponent()) {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(
                            new DiagramClassDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), "#C5C8C6", entry.getValue().componentType().getValue()));

                } else if (entry.getValue().componentType().isMethodComponent()) {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(
                            new DiagramMethodDisplayName(entry.getValue().uniqueName()).value(),
                            entry.getValue().uniqueName(), "#C5C8C6", entry.getValue().componentType().getValue()));
                } else {
                    SvgGraphics.displayComponents.add(new ComponentDisplayInfo(entry.getValue().name(),
                            entry.getValue().uniqueName(), "#C5C8C6", entry.getValue().componentType().getValue()));
                }

            }
        }
        PlantUMLClassDiagramDesciption diffClarityView = new DiffPlantUMLDiagramDesciption(diagramComponents,
                allRelationships, deletedRelationships, addedRelationships, deletedComponents, addedComponents,
                mergedCodeBase.getComponents());
        SvgGraphics.componentCallBack = callback;
        this.diagram = new PlantUMLClassDiagram(diffClarityView, colorScheme);
    }

    public DiffClarityView(Map<String, BinaryClassRelationship> oldbinaryRelationships,
            Map<String, BinaryClassRelationship> newbinaryRelationships, OOPSourceCodeModel olderModel,
            OOPSourceCodeModel newerModel, boolean callback) throws Exception {

        this(new ClarityDarkClassDiagramColorScheme(), oldbinaryRelationships, newbinaryRelationships, olderModel,
                newerModel, callback);
    }

    @Override
    public ClassDiagram view() {
        return this.diagram;
    }
}
