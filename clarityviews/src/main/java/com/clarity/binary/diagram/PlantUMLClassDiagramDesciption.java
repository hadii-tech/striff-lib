package com.clarity.binary.diagram;

/**
 * A PlantUML compliant description of a SVG Class Diagram.
 */
public interface PlantUMLClassDiagramDesciption {

	/*
	 * Returns a PlantUML compliant string containing all the class body definitions
	 * of the classes in the diagram. Refer to http://plantuml.com/class-diagram.
	 */
	String classDesciptionString();

	/*
	 * Returns a PlantUML compliant string containing all the class relationship definitions
	 * between the classes in the class diagram. Refer to http://plantuml.com/class-diagram.
	 */
	String relationsDesciptionString();
}
