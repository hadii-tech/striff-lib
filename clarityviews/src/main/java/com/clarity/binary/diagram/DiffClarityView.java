package com.clarity.binary.diagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clarity.binary.ComponentSet;
import com.clarity.binary.display.DiagramClassDisplayName;
import com.clarity.binary.display.DiagramMethodDisplayName;
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
	private String diagramStr;

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
		// base but
		// do exist in the older code base.
		List<String> deletedComponents = new ArrayList<String>();
		for (final Map.Entry<String, Component> entry : olderModel.getComponents().entrySet()) {
			if (!newerModel.getComponents().containsKey(entry.getKey())) {
				deletedComponents.add(entry.getKey());
			}
		}

		// form a list of all binary relationships that exist in the newer code
		// base but
		// not in the older code base.
		List<BinaryClassRelationship> addedRelationships = new ArrayList<BinaryClassRelationship>();
		for (final Map.Entry<String, BinaryClassRelationship> entry : newBinaryRelationships.entrySet()) {
			if (!oldBinaryRelationships.containsKey(entry.getKey())) {
				addedRelationships.add(entry.getValue());
			}
		}

		// form a list of all binary relationships that do not exist in the
		// newer
		// code
		// base but do exist in the older code base.
		List<BinaryClassRelationship> deletedRelationships = new ArrayList<BinaryClassRelationship>();
		for (final Map.Entry<String, BinaryClassRelationship> entry : oldBinaryRelationships.entrySet()) {
			if (!newBinaryRelationships.containsKey(entry.getKey())) {
				deletedRelationships.add(entry.getValue());
			}
		}

		// generate list of components that are needed to draw a class diagram
		// for the added components
		Set<Component> keyAddedComponents = new RelatedComponentsGroup(newerModel.getComponents(),
				newBinaryRelationships, addedComponents).components();
		// generate list of components that are needed to draw a class diagram
		// for the deleted components
		Set<Component> keyDeletedComponents = new RelatedComponentsGroup(olderModel.getComponents(),
				oldBinaryRelationships, deletedComponents).components();
		// generate a list of components needed to draw the entire diff diagram
		@SuppressWarnings("unchecked")
		Set<Component> diagramComponents = new ComponentSet(keyAddedComponents, keyDeletedComponents).set();

		// generate a list of binary relationships needed to draw the entire
		// diff diagram
		Map<String, BinaryClassRelationship> allRelationships = new HashMap<String, BinaryClassRelationship>();
		allRelationships.putAll(newBinaryRelationships);
		allRelationships.putAll(oldBinaryRelationships);

		for (Component cmp : diagramComponents) {
			if (addedComponents.contains(cmp.uniqueName())) {
				// mark all the newly added components green
				if (cmp.componentType().isBaseComponent()) {
					SvgGraphics.displayComponents
							.add(new ComponentDisplayInfo(new DiagramClassDisplayName(cmp.uniqueName()).value(),
									cmp.uniqueName(), "#22df80", cmp.componentType().getValue()));

				} else if (cmp.componentType().isMethodComponent()) {
					SvgGraphics.displayComponents
							.add(new ComponentDisplayInfo(new DiagramMethodDisplayName(cmp.uniqueName()).value(),
									cmp.uniqueName(), "#22df80", cmp.componentType().getValue()));
				} else {
					SvgGraphics.displayComponents.add(new ComponentDisplayInfo(cmp.name(), cmp.uniqueName(), "#22df80",
							cmp.componentType().getValue()));
				}
			} else if (deletedComponents.contains(cmp.uniqueName())) {
				// mark all the delted components red
				if (cmp.componentType().isBaseComponent()) {
					SvgGraphics.displayComponents
							.add(new ComponentDisplayInfo(new DiagramClassDisplayName(cmp.uniqueName()).value(),
									cmp.uniqueName(), "#F97D7D", cmp.componentType().getValue()));

				} else if (cmp.componentType().isMethodComponent()) {
					SvgGraphics.displayComponents
							.add(new ComponentDisplayInfo(new DiagramMethodDisplayName(cmp.uniqueName()).value(),
									cmp.uniqueName(), "#F97D7D", cmp.componentType().getValue()));
				} else {
					SvgGraphics.displayComponents.add(new ComponentDisplayInfo(cmp.name(), cmp.uniqueName(), "#F97D7D",
							cmp.componentType().getValue()));
				}
			} else {
				// mark all the unchanged components gray
				if (cmp.componentType().isBaseComponent()) {
					SvgGraphics.displayComponents
							.add(new ComponentDisplayInfo(new DiagramClassDisplayName(cmp.uniqueName()).value(),
									cmp.uniqueName(), "#C5C8C6", cmp.componentType().getValue()));

				} else if (cmp.componentType().isMethodComponent()) {
					SvgGraphics.displayComponents
							.add(new ComponentDisplayInfo(new DiagramMethodDisplayName(cmp.uniqueName()).value(),
									cmp.uniqueName(), "#C5C8C6", cmp.componentType().getValue()));
				} else {
					SvgGraphics.displayComponents.add(new ComponentDisplayInfo(cmp.name(), cmp.uniqueName(), "#C5C8C6",
							cmp.componentType().getValue()));
				}

			}
		}

		OOPSourceCodeModel mergedCodeBase = olderModel;
		mergedCodeBase.merge(newerModel);
		PlantUMLClassDiagramDesciption diffClarityView = new DiffPlantUMLDiagramDesciption(diagramComponents,
				allRelationships, deletedRelationships, addedRelationships, deletedComponents, addedComponents,
				mergedCodeBase.getComponents());
		SvgGraphics.componentCallBack = callback;
		this.diagramStr = new PlantUMLClassDiagram(diffClarityView, colorScheme).svgText();
	}

	public DiffClarityView(Map<String, BinaryClassRelationship> oldbinaryRelationships,
			Map<String, BinaryClassRelationship> newbinaryRelationships, OOPSourceCodeModel olderModel,
			OOPSourceCodeModel newerModel, boolean callback) throws Exception {

		this(new ClarityDarkClassDiagramColorScheme(), oldbinaryRelationships, newbinaryRelationships, olderModel,
				newerModel, callback);
	}

	@Override
	public String view() {
		return this.diagramStr;
	}
}
