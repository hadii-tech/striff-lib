package com.clarity.binary.diagram;

/**
 * For generating diagrams.
 */
public interface ClassDiagram {

    /**
     * Draws a PlantUML SVG class diagram.
     */
    String svgText() throws Exception;
}