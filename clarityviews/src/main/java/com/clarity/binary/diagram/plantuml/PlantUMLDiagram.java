package com.clarity.binary.diagram.plantuml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.scheme.DiagramColorScheme;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.svg.ComponentDisplayInfo;

public class PlantUMLDiagram implements Diagram {

    private static final String PLANT_UML_BEGIN_STRING = "@startuml\nskinparam linetype ortho\n";
    private static final String PLANT_UML_END_STRING = "\n@enduml";
    private PlantUMLDiagramDesciption plantUMLClassDiagramDescription;
    private DiagramColorScheme colorScheme;
    private List<ComponentDisplayInfo> displayComponents;
    private String keyClassName;

    public PlantUMLDiagram(final PlantUMLDiagramDesciption plantUMLClassDescription,
            DiagramColorScheme colorScheme, List<ComponentDisplayInfo> displayComponents) {
        this.plantUMLClassDiagramDescription = plantUMLClassDescription;
        this.colorScheme = colorScheme;
        this.displayComponents = displayComponents;
    }

    public PlantUMLDiagram(final PlantUMLDiagramDesciption plantUMLClassDescription,
            DiagramColorScheme colorScheme, List<ComponentDisplayInfo> displayComponents,
            String keyClassName) {
        this.plantUMLClassDiagramDescription = plantUMLClassDescription;
        this.colorScheme = colorScheme;
        this.displayComponents = displayComponents;
        this.keyClassName = keyClassName;
    }

    @Override
    public final String svgText() throws InterruptedException, IOException {

        final long startTime = new Date().getTime();
        final String plantUMLString = genPlantUMLString();
        final byte[] diagram = generateDiagram(plantUMLString);
        final String diagramStr = new String(diagram);
        System.out.println(
                " Clarity View diagram SVG text generated in " + (new Date().getTime() - startTime) + " milliseconds.");
        return diagramStr;
    }

    /**
     * Forms a PlantUML compliant String representing the class diagram.
     */
    private String genPlantUMLString() throws IOException {

        final String diagramSkin = formPlantUMLSkinString(colorScheme);

        final String source = PLANT_UML_BEGIN_STRING + diagramSkin
                + plantUMLClassDiagramDescription.description() + PLANT_UML_END_STRING;
        return source;
    }

    private String formPlantUMLSkinString(DiagramColorScheme colorScheme) {
        return "skinparam defaultFontName " + colorScheme.defaultFontName() + "\n" + "skinparam backgroundColor  "
                + colorScheme.backgroundColor() + "\n" + "skinparam classArrowFontName "
                + colorScheme.classArrowFontName() + "\n" + "skinparam classArrowColor " + colorScheme.classArrowColor()
                + "\n" + "skinparam classBackgroundColor " + colorScheme.classBackgroundColor() + "\n"
                + "skinparam classArrowFontColor " + colorScheme.classArrowFontColor() + "\n"
                + "skinparam classArrowFontSize " + colorScheme.classArrowFontSize() + "\n"
                + "skinparam classFontColor " + colorScheme.classArrowFontColor() + "\n" + "skinparam classFontSize "
                + colorScheme.classFontSize() + "\n" + "skinparam classStereotypeFontColor "
                + colorScheme.classStereotypeFontColor() + "\n" + "skinparam classAttributeFontColor "
                + colorScheme.classAttributeFontColor() + "\n" + "skinparam classAttributeFontSize "
                + colorScheme.classAttributeFontSize() + "\n" + "skinparam classFontName " + colorScheme.classFontName()
                + "\n" + "skinparam classAttributeFontName " + colorScheme.classAttributeFontName() + "\n"
                + "skinparam titleFontColor " + colorScheme.titleFontColor() + "\n"
                + "skinparam packageBackgroundColor " + colorScheme.packageBackgroundColor() + "\n"
                + "skinparam titleFontName " + colorScheme.titleFontName() + "\n" + "skinparam packageBorderColor "
                + colorScheme.packageBorderColor() + "\n" + "skinparam packageFontColor "
                + colorScheme.packageFontColor() + "\n" + "skinparam packageFontName " + colorScheme.packageFontName()
                + "\n" + "skinparam packageFontStyle " + colorScheme.packageFontStyle() + "\n"
                + "skinparam packageFontSize " + colorScheme.packageFontSize() + "\n" + "skinparam classBorderColor "
                + colorScheme.classBorderColor() + "\n";
    }

    /**
     * Invokes PlantUML to draw the class diagram based on the source string
     * representing a PlantUML compliant class diagram description.
     */
    public byte[] generateDiagram(String source) {
        final SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            if (keyClassName != null) {
                reader.generateImage(keyClassName, displayComponents, os, new FileFormatOption(FileFormat.SVG));
            } else {
                reader.generateImage(displayComponents, os, new FileFormatOption(FileFormat.SVG));
            }
        } catch (final IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            os.close();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // SvgGraphics.displayComponents.clear();
        return os.toByteArray();
    }
}
