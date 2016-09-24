package com.clarity.rest.core.component.diagram;

import java.util.Map;

import com.clarity.rest.extractor.BinaryClassRelationship;
import com.clarity.sourcemodel.Component;

/**
 * For generating diagrams.
 *
 * @author Muntazir Fadhel
 */
public interface DiagramGenerator {

    /**
     * Generates a list of diagrams for the given component.
     *
     * @param component
     *            component to generate diagrams for
     * @param binaryRelationships
     *            list of all the binary class relationships in the code base
     * @param componentList
     *            list of all the components in the code base
     * @param diagramSize
     *            average requested diagram size
     * @return diagram including the given component
     * @throws Exception
     *             Exception
     */
    String generateDiagram(Component component, Map<String, BinaryClassRelationship> binaryRelationships,
            Map<String, Component> componentList, int diagramSize) throws Exception;

}