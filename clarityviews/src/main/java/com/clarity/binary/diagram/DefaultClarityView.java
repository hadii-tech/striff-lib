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
     * @param desiredSize
     *            Desired size of the diagram.
     */
    public DefaultClarityView(ClassDiagramColorScheme colorScheme,
            Map<String, BinaryClassRelationship> binaryRelationships, OOPSourceCodeModel model,
            Component diagramComponent, int desiredSize) throws Exception {

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
        this.diagram = new PlantUMLClassDiagram(plantUMLClassDescription, colorScheme, displayComponents, diagramComponent.name());
    }

    public DefaultClarityView(Map<String, BinaryClassRelationship> binaryRelationships, OOPSourceCodeModel model,
            Component diagramComponent) throws Exception {
        this(new ClarityDarkClassDiagramColorScheme(), binaryRelationships, model, diagramComponent);
    }

    public DefaultClarityView(ClassDiagramColorScheme colorScheme,
            Map<String, BinaryClassRelationship> binaryRelationships, OOPSourceCodeModel model,
            Component diagramComponent) throws Exception {
        this(colorScheme, binaryRelationships, model, diagramComponent, 4);
    }

    @Override
    public ClassDiagram view() {
        return this.diagram;
    }
}
