package com.clarity.binary.diagram.plantuml;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clarity.binary.DefaultText;
import com.clarity.binary.HtmlTagsStrippedText;
import com.clarity.binary.JavaDocSymbolStrippedText;
import com.clarity.binary.LineBreakedText;
import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.diagram.DiagramConstants.DefaultClassMultiplicities;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.binary.extractor.ColoredBinaryClassAssociation;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants;
import com.clarity.sourcemodel.OOPSourceModelConstants.AccessModifiers;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentType;

public class StructureDiffPlantUMLDiagramDesciption implements PlantUMLDiagramDesciption {

    private Set<Component> diagramComponents;
    private Map<String, Component> allComponents;
    private Set<BinaryClassRelationship> binaryRelationships;
    private List<String> deletedComponents;
    private List<String> addedComponents;
    private List<BinaryClassRelationship> deletedRelationships;
    private List<BinaryClassRelationship> addedRelationships;

    public StructureDiffPlantUMLDiagramDesciption(Set<Component> diagramComponents,
            Set<BinaryClassRelationship> allRelationships, List<BinaryClassRelationship> deletedRelationships,
            List<BinaryClassRelationship> addedRelationships, List<String> deletedComponents,
            List<String> addedComponents, Map<String, Component> allComponents) {
        this.diagramComponents = diagramComponents;
        this.allComponents = allComponents;
        this.addedComponents = addedComponents;
        this.deletedComponents = deletedComponents;
        this.binaryRelationships = allRelationships;
        this.addedRelationships = addedRelationships;
        this.deletedRelationships = deletedRelationships;
    }

    @Override
    public String description() {
        return classDesciptionString() + relationsDesciptionString();
    }

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
                            lines[i] = diffComponentColor(component, addedComponents, deletedComponents)
                                    + lines[i].trim();
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
                    if (childCmp.componentType() == ComponentType.METHOD
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
                            tempStrBuilder.append(childCmp.uniqueName());
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
            }

        }
        return tempStrBuilder.toString();
    }

    public String relationsDesciptionString() {
        final StringBuilder tempStrBuilder = new StringBuilder();
        for (BinaryClassRelationship relationship : binaryRelationships) {

            String classAName = relationship.getClassA().name();
            String classBName = relationship.getClassB().name();

            if ((classAName != null) && ((classBName != null) && diagramComponents.contains(relationship.getClassA())
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
                    tempStrBuilder.append(new ColoredBinaryClassAssociation(classAAssociation,
                            diffRelationsColor(relationship, addedRelationships, deletedRelationships))
                            .getyumlLinkType());
                } else {
                    tempStrBuilder.append(new ColoredBinaryClassAssociation(classBAssociation,
                            diffRelationsColor(relationship, addedRelationships, deletedRelationships))
                            .getyumlLinkType());
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

    private String diffComponentColor(Component cmp, List<String> addedComponents2, List<String> deletedComponents2) {
        if (addedComponents2.contains(cmp.uniqueName())) {
            return "greenify";
        } else if (deletedComponents2.contains(cmp.uniqueName())) {
            return "redify";
        } else {
            return "";
        }
    }

    private String diffRelationsColor(BinaryClassRelationship relation,
            List<BinaryClassRelationship> addedRelationships, List<BinaryClassRelationship> deletedRelationships) {
        for (BinaryClassRelationship bCR : addedRelationships) {
            if (bCR.equals(relation)) {
                return "22DF80";
            }
        }
        for (BinaryClassRelationship bCR : deletedRelationships) {
            if (bCR.equals(relation)) {
                return "F97D7D";
            }
        }
        return "";
    }
}
