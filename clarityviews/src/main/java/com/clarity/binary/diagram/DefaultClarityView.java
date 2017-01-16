package com.clarity.binary.diagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clarity.binary.diagram.display.DiagramClassDisplayName;
import com.clarity.binary.diagram.display.DiagramMethodDisplayName;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;

import net.sourceforge.plantuml.svg.ComponentDisplayInfo;
import net.sourceforge.plantuml.svg.SvgGraphics;

public class DefaultClarityView implements ClarityView, Serializable {

    private static final long serialVersionUID = -3125810981280395679L;
    private ClassDiagram diagram;

    /**
     * @param colorScheme
     *            Color scheme for the diagram.
     * @param binaryRelationships
     *            A list of all the binary relationships in the code base.
     * @param model
     *            Source code model representing the entire code base.
     * @param diagramComponent
     *            The component for which the Clarity View is being generated.
     * @param callback
     *            Whether or not the diagram should be interactive.
     */
    public DefaultClarityView(ClassDiagramColorScheme colorScheme,
            Map<String, BinaryClassRelationship> binaryRelationships, OOPSourceCodeModel model,
            Component diagramComponent, boolean callback) throws Exception {

        List<String> components = new ArrayList<String>();
        components.add(diagramComponent.uniqueName());
        PlantUMLClassDiagramDesciption plantUMLClassDescription = new DefaultPlantUMLDiagramDesciption(
                new RelatedComponentsGroup(model.getComponents(), binaryRelationships, components).components(),
                model.getComponents(), binaryRelationships);
        List<ComponentDisplayInfo> displayComponents = new ArrayList<ComponentDisplayInfo>();
        for (final Map.Entry<String, Component> entry : model.getComponents().entrySet()) {
            Component cmp = entry.getValue();
            if (cmp.componentType().isBaseComponent()) {
                displayComponents.add(new ComponentDisplayInfo(new DiagramClassDisplayName(cmp.uniqueName()).value(),
                        cmp.uniqueName(), "#22df80", cmp.componentType().getValue()));
            } else if (cmp.componentType().isMethodComponent()) {
                displayComponents.add(new ComponentDisplayInfo(new DiagramMethodDisplayName(cmp.uniqueName()).value(),
                        cmp.uniqueName(), "#C5C8C6", cmp.componentType().getValue()));
            } else if (cmp.componentType().isVariableComponent()) {
                displayComponents.add(new ComponentDisplayInfo(cmp.name(), cmp.uniqueName(), "#C5C8C6",
                        cmp.componentType().getValue()));
            }
        }
        SvgGraphics.componentCallBack = callback;
        this.diagram = new PlantUMLClassDiagram(plantUMLClassDescription, colorScheme, displayComponents);
    }

    public DefaultClarityView(Map<String, BinaryClassRelationship> binaryRelationships, OOPSourceCodeModel model,
            Component diagramComponent, boolean callback) throws Exception {
        this(new ClarityDarkClassDiagramColorScheme(), binaryRelationships, model, diagramComponent, callback);
    }

    @Override
    public ClassDiagram view() {
        return this.diagram;
    }
}
