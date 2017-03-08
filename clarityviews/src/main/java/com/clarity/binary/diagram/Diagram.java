package com.clarity.binary.diagram;

/**
 * A Class Diagram.
 */
public interface Diagram {

    /**
     * Returns a string representing the svg makeup of a class diagram.
     */
    String svgText() throws Exception;
}