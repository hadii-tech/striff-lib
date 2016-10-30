package com.clarity.binary.diagram;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.clarity.binary.DefaultText;
import com.clarity.binary.HtmlTagsStrippedText;
import com.clarity.binary.JavaDocSymbolStrippedText;
import com.clarity.binary.LineBreakedText;
import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.diagram.DiagramConstants.DefaultClassMultiplicities;
import com.clarity.binary.display.DiagramMethodDisplayName;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants;
import com.clarity.sourcemodel.OOPSourceModelConstants.AccessModifiers;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentInvocations;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentType;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 *
 * @author Muntazir Fadhel
 *
 */
public class PlantUMLClassDiagramGenerator implements DiagramGenerator {

    private static final String PLANT_UML_BEGIN_STRING = "@startuml\n";
    private static final String PLANT_UML_END_STRING   = "\n@enduml";

    /**
     * Generates a plantUML String describing the details of the classes
     * contained in the 'activeComponents' list.
     *
     * @param activeComponents
     *            The components under consideration for the current diagram.
     * @param componentList
     *            list of all the components in the code base
     * @return PlantUML text description of the classes for the current diagram.
     */
    private static String genClassDetails(final ArrayList<Component> activeComponents,
            final Map<String, Component> componentList) {
        final StringBuilder tempStrBuilder = new StringBuilder();
        for (final Component component : activeComponents) {
            // insert package name
            tempStrBuilder.append("package " + component.packageName() + " {\n");
            // determine if we have base component type...
            if (component.componentType().isBaseComponent()) {
                if (component.modifiers().contains(
                        // if class is abstract...
                        OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.ABSTRACT))) {
                    tempStrBuilder.append(
                            OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.ABSTRACT) + " ");
                }
                // add component type name (eg: class, interface, etc...)
                tempStrBuilder
                        .append(OOPSourceModelConstants.getJavaComponentTypes().get(component.componentType()) + " ");
                // add the actual component short name
                tempStrBuilder.append(component.uniqueName());
                // add class generics if exist
                if (component.declarationTypeSnippet() != null) {
                    tempStrBuilder.append(component.declarationTypeSnippet());
                }
                // open the brackets
                tempStrBuilder.append(" {\n");
                // if abstract class or interface, add java doc
                if (component.componentType() == ComponentType.INTERFACE
                        || component.modifiers().contains("abstract")) {
                    if (component.comment() != null && !component.comment().isEmpty()) {
                        String str;
                        if (component.comment().length() < 800) {
                            str = new LineBreakedText(new JavaDocSymbolStrippedText(
                                    new HtmlTagsStrippedText(new DefaultText(component.comment().trim() + "..."))))
                                            .value()
                                    + "\n";

                        } else {
                            str = new LineBreakedText(new JavaDocSymbolStrippedText(new HtmlTagsStrippedText(
                                    new DefaultText(component.comment().substring(0, 800).trim() + "...")))).value()
                                    + "\n";
                        }
                        String[] lines = str.split("\n");
                        for (int i = 0; i < lines.length; i++) {
                            lines[i] = "$%" + lines[i].trim();
                        }
                        tempStrBuilder.append(org.apache.commons.lang.StringUtils.join(lines, "\n"));
                        tempStrBuilder.append("\n");
                    }
                }
                for (final String classChildCmpName : component.children()) {
                    final Component childCmp = componentList.get(classChildCmpName);
                    // only care about children of interface or abstract
                    // classes, or special children
                    // of regular classes
                    if ((component.componentType() == ComponentType.INTERFACE
                            || component.modifiers().contains("abstract"))
                            || (childCmp.componentType() == ComponentType.METHOD
                                    && diagramaticallyRelevantMethod(childCmp))
                            || childCmp.componentType().isVariableComponent()) {
                        // start entering the fields and methods...
                        if ((childCmp != null) && !childCmp.componentType().isBaseComponent()) {
                            if (childCmp.modifiers().contains(
                                    OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.PUBLIC))) {
                                tempStrBuilder.append(AccessModifiers.PUBLIC.getUMLClassDigramSymbol() + " ");
                            } else if (childCmp.modifiers().contains(
                                    OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.PRIVATE))) {
                                tempStrBuilder.append(AccessModifiers.PRIVATE.getUMLClassDigramSymbol() + " ");
                            } else if (childCmp.modifiers().contains(OOPSourceModelConstants.getJavaAccessModifierMap()
                                    .get(AccessModifiers.PROTECTED))) {
                                tempStrBuilder.append(AccessModifiers.PROTECTED.getUMLClassDigramSymbol() + " ");
                            } else {
                                tempStrBuilder.append(AccessModifiers.NONE.getUMLClassDigramSymbol() + " ");
                            }
                            // if the field/method is abstract or static, add
                            // the {abstract}/{static} prefix..
                            if (childCmp.modifiers().contains(
                                    OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.ABSTRACT))) {
                                tempStrBuilder.append("{");
                                tempStrBuilder.append(OOPSourceModelConstants.getJavaAccessModifierMap()
                                        .get(AccessModifiers.ABSTRACT));
                                tempStrBuilder.append("} ");
                            }
                            if (childCmp.modifiers().contains(
                                    OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.STATIC))) {
                                tempStrBuilder.append("{");
                                tempStrBuilder.append(
                                        OOPSourceModelConstants.getJavaAccessModifierMap().get(AccessModifiers.STATIC));
                                tempStrBuilder.append("} ");
                            }

                            if (childCmp.componentType().isMethodComponent()) {
                                tempStrBuilder.append(new DiagramMethodDisplayName(childCmp.uniqueName()).value());
                            } else {
                                tempStrBuilder.append(childCmp.name());
                            }

                            if (childCmp.componentType() == ComponentType.ENUM) {
                                break;
                            } else if (childCmp.declarationTypeSnippet() == null
                                    && (childCmp.componentType() == ComponentType.METHOD
                                            || childCmp.componentType() == ComponentType.CONSTRUCTOR)) {
                                // add the return/ field type
                                tempStrBuilder.append(" : ");
                                tempStrBuilder.append("void" + "\n");
                            } else {
                                // add the return/ field type
                                tempStrBuilder.append(" : ");
                                if (!childCmp.value().contains(".")) {
                                    tempStrBuilder.append(childCmp.value() + "\n");
                                } else {
                                    tempStrBuilder.append(
                                            childCmp.value().substring(childCmp.value().lastIndexOf(".") + 1) + "\n");
                                }
                            }
                        }
                    }
                }
                tempStrBuilder.append("}\n");
                // close package declaration
                tempStrBuilder.append("}\n");
            }

        }
        return tempStrBuilder.toString();

    }

    /**
     * Determine if the given method should be included in the diagram or not.
     */
    private static boolean diagramaticallyRelevantMethod(Component methodComponent) {

        // no getters or setters
        if (methodComponent.name().startsWith("get") || methodComponent.name().startsWith("set")) {
            return false;
        }
        // no overridden methods
        for (final ComponentInvocation invocation : methodComponent
                .componentInvocations(ComponentInvocations.ANNOTATION)) {
            if (invocation.invokedComponent().equals("Override")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a PlantUML string representing all the inter-class
     * relationships between the given activeComponents.
     *
     * @param activeComponents
     *            Components from which to search for class relationships.
     * @param binaryRelationships
     *            list of all the binary class relationships in the code base
     * @return PlantUML text description of the relations between the given
     *         class components.
     */
    private static String addClassRelations(final ArrayList<Component> activeComponents,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        final StringBuilder tempStrBuilder = new StringBuilder();

        for (final Map.Entry<String, BinaryClassRelationship> entry : binaryRelationships.entrySet()) {

            // [0] = first class name, [1] = second class name
            final String[] relationshipClassNames = entry.getKey()
                    .split(BinaryClassRelationship.getClassNameSplitter());
            final BinaryClassRelationship relationship = entry.getValue();

            if ((relationshipClassNames[0] != null)
                    && ((relationshipClassNames[1] != null) && activeComponents.contains(relationship.getClassA())
                            && activeComponents.contains(relationship.getClassB())
                            && relationship.getClassA().componentType().isBaseComponent()
                            && relationship.getClassB().componentType().isBaseComponent() && (relationship != null))) {
                final BinaryClassAssociation classAAssociation = relationship.getaSideAssociation();
                final BinaryClassAssociation classBAssociation = relationship.getbSideAssociation();
                // start building our string for side class A
                // insert class A short name
                tempStrBuilder.append(relationship.getClassA().uniqueName() + " ");
                // insert class B multiplicity if its not a zero to one
                // multiplicity..
                if (!relationship.getbSideMultiplicity().getValue().isEmpty() && !relationship.getbSideMultiplicity()
                        .getValue().equals(DefaultClassMultiplicities.ZEROTOONE.getValue())) {
                    tempStrBuilder.append("\"" + relationship.getbSideMultiplicity().getValue() + "\" ");
                }
                // insert class B association type
                tempStrBuilder.append(classBAssociation.getBackwardLinkEndingType());
                if (classAAssociation.getStrength() > classBAssociation.getStrength()) {
                    tempStrBuilder.append(classAAssociation.getyumlLinkType());
                } else {
                    tempStrBuilder.append(classBAssociation.getyumlLinkType());
                }
                // insert class A association type
                tempStrBuilder.append(classAAssociation.getForwardLinkEndingType());
                // insert class A multiplicity if its not a zero to one
                // multiplicity..
                if (!relationship.getaSideMultiplicity().getValue().isEmpty() && !relationship.getaSideMultiplicity()
                        .getValue().equals(DefaultClassMultiplicities.ZEROTOONE.getValue())) {
                    tempStrBuilder.append(" \"" + relationship.getaSideMultiplicity().getValue() + "\" ");
                }
                // insert class B name
                tempStrBuilder.append(" " + relationship.getClassB().uniqueName());
                tempStrBuilder.append("\n");
            }
        }

        return tempStrBuilder.toString();
    }

    /**
     * Generates a single diagram.
     *
     * @param componentGroup
     *            list of component groupings that form each diagram
     * @return String containing SVG diagram
     * @param binaryRelationships
     *            list of all the binary class relationships in the code base\
     * @param componentList
     *            list of all the components in the code base
     * @throws IOException
     *             Exception
     * @throws InterruptedException
     *             Exception
     */
    private byte[] genPlantUMLDiagrams(final ArrayList<Component> componentGroup,
            final Map<String, BinaryClassRelationship> binaryRelationships, final Map<String, Component> componentList)
            throws IOException, InterruptedException {

        final String classDefinitions = genClassDetails(componentGroup, componentList);
        final String classRelations = addClassRelations(componentGroup, binaryRelationships);
        final String plantUMLString = (generatePlantUMLString(classDefinitions, classRelations));
        final byte[] diagram = generateDiagram(plantUMLString);
        return diagram;
    }

    @Override
    public final String generateDiagram(final Component component,
            final Map<String, BinaryClassRelationship> binaryRelationships, final Map<String, Component> componentList,
            final int diagramSize) throws InterruptedException, IOException {

        final long startTime = new Date().getTime();
        final ArrayList<Component> componentGroups = new RelatedDiagramComponents(componentList, binaryRelationships,
                component, diagramSize).components();
        final byte[] diagramString = genPlantUMLDiagrams(componentGroups, binaryRelationships, componentList);
        final String diagram = new String(diagramString);
        System.out
                .println(" Clarity View diagram generated in " + (new Date().getTime() - startTime) + " milliseconds.");
        return diagram;
    }

    /**
     *
     * @param theme
     *            color theme of the diagram
     * @param classDefinitions
     *            PlantUML String containing class definitions
     * @param classRelations
     *            PlantUML String containing class relationships
     * @return PlantUML string representing the finished diagram
     * @throws IOException
     *             Exception
     */
    private static String generatePlantUMLString(final String classDefinitions, final String classRelations)
            throws IOException {

        final String diagramSkin = DiagramConstants.PLANT_UML_DARK_THEME_STRING;

        final String source = PLANT_UML_BEGIN_STRING + diagramSkin + classDefinitions + classRelations
                + PLANT_UML_END_STRING;
        return source;
    }

    public byte[] generateDiagram(String source) {
        final SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
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
        return os.toByteArray();
    }
}
