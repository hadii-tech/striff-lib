package com.clarity.rest.core.component.diagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import com.clarity.rest.extractor.BinaryClassRelationship;
import com.clarity.rest.extractor.ClassRelationshipsExtractor;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;

import net.sourceforge.plantuml.svg.SvgGraphics;

/**
 * A Clarity Views Diagram representation.
 *
 * @author Muntazir Fadhel
 */
public class ClarityView implements Serializable {

    private static final long serialVersionUID = -3125810981280395679L;
    private String            diagramStr;
    private String            fileName;

    public ClarityView(OOPSourceCodeModel model, Component diagramComponent, int desiredSize, boolean callback)
            throws Exception {

        this.setFileName(diagramComponent.sourceFile());
        // get the list of components in the code base
        final Map<String, Component> componentList = model.getComponents();
        // determine class relationships that exists within the code base
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS.generateBinaryClassRelationships(model);
        // initialize diagram generator and feed all base (class, interface,
        // etc..) components
        final PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator();
        final ArrayList<String> baseComponentUniqueNames = new ArrayList<String>();
        for (final Map.Entry<String, Component> entry : componentList.entrySet()) {
            if (entry.getValue().componentType().isBaseComponent()) {
                baseComponentUniqueNames.add(entry.getValue().uniqueName());
            }
        }

        SvgGraphics.keyClass = diagramComponent.uniqueName();
        SvgGraphics.currentComponents = baseComponentUniqueNames;
        SvgGraphics.componentCallBack = callback;
        this.diagramStr = generator.generateDiagram(diagramComponent, binaryRelationships, componentList, desiredSize);
    }

    public String getdiagramStr() {
        return diagramStr;
    }

    public void setDiagramStr(String diagramString) {
        diagramStr = diagramString;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
