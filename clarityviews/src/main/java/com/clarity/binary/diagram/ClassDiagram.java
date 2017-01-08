package com.clarity.binary.diagram;

/**
 * A Class Diagram.
 */
public interface ClassDiagram {

    /**
     * Returns a string representing the svg makeup of a class diagram.
     */
    String svgText() throws Exception;
}