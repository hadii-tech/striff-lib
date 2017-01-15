package com.clarity.binary.diagram;

import java.util.Map;
import java.util.Set;

import com.clarity.binary.DefaultText;
import com.clarity.binary.HtmlTagsStrippedText;
import com.clarity.binary.JavaDocSymbolStrippedText;
import com.clarity.binary.LineBreakedText;
import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.diagram.DiagramConstants.DefaultClassMultiplicities;
import com.clarity.binary.diagram.display.DiagramMethodDisplayName;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants;
import com.clarity.sourcemodel.OOPSourceModelConstants.AccessModifiers;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentType;

public class DefaultPlantUMLDiagramDesciption implements PlantUMLClassDiagramDesciption {

    private Set<Component> diagramComponents;
    private Map<String, Component> allComponents;
    private Map<String, BinaryClassRelationship> binaryRelationships;

    /**
     * @param diagramComponents
     *            The components under consideration for the current diagram.
     * @param allComponents
     *            list of all the components in the code base
     */
    public DefaultPlantUMLDiagramDesciption(final Set<Component> diagramComponents,
            final Map<String, Component> allComponents,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        this.diagramComponents = diagramComponents;
        this.allComponents = allComponents;
        this.binaryRelationships = binaryRelationships;
    }

    @Override
    public String classDesciptionString() {
        final StringBuilder tempStrBuilder = new StringBuilder();
        for (final Component component : diagramComponents) {
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
                            lines[i] = "redify" + lines[i].trim();
                        }
                        tempStrBuilder.append(org.apache.commons.lang.StringUtils.join(lines, "\n"));
                        tempStrBuilder.append("\n");
                    }
                }
                for (final String classChildCmpName : component.children()) {
                    final Component childCmp = allComponents.get(classChildCmpName);
                    // only care about children of interface or abstract
                    // classes, or special children
                    // of regular classes
                    if ((component.componentType() == ComponentType.INTERFACE
                            || component.modifiers().contains("abstract"))
                            || (childCmp.componentType() == ComponentType.METHOD
                                    && new DiagramComponent(childCmp).diagramaticallyRelevantComponent())
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

                            if (childCmp.componentType().isVariableComponent()) {
                                tempStrBuilder.append(childCmp.uniqueName());
                            } else {
                                int desiredLength = new DiagramMethodDisplayName(childCmp.uniqueName()).value()
                                        .length();
                                String hashCode = String.valueOf(childCmp.uniqueName().hashCode()) + "$()";
                                // we want PlantUML to make enough space for the
                                // full method name when we draw it into the
                                // diagram later..
                                while (String.valueOf(hashCode).length() < desiredLength) {
                                    hashCode += "_";
                                }
                                tempStrBuilder.append(hashCode);
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
                                if (childCmp.value() != null) {
                                    tempStrBuilder.append(" : ");
                                    if (!childCmp.value().contains(".")) {
                                        tempStrBuilder.append(childCmp.value());
                                    } else {
                                        tempStrBuilder.append(
                                                childCmp.value().substring(childCmp.value().lastIndexOf(".") + 1));
                                    }
                                }
                                tempStrBuilder.append("\n");
                            }
                        }
                    }
                }
                tempStrBuilder.append("}\n");
            }

        }
        return tempStrBuilder.toString();
    }

    @Override
    public String relationsDesciptionString() {

        final StringBuilder tempStrBuilder = new StringBuilder();
        for (final Map.Entry<String, BinaryClassRelationship> entry : binaryRelationships.entrySet()) {

            // [0] = first class name, [1] = second class name
            final String[] relationshipClassNames = entry.getKey()
                    .split(BinaryClassRelationship.getClassNameSplitter());
            final BinaryClassRelationship relationship = entry.getValue();

            if ((relationshipClassNames[0] != null)
                    && ((relationshipClassNames[1] != null) && diagramComponents.contains(relationship.getClassA())
                            && diagramComponents.contains(relationship.getClassB())
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
}
