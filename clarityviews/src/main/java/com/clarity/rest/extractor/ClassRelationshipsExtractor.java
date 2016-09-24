/**
 * Extracts and stores all the binary class relationships from a group of Component Objects.
 */

package com.clarity.rest.extractor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clarity.ClarpseUtil;
import com.clarity.invocation.ComponentInvocation;
import com.clarity.rest.core.component.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.rest.core.component.diagram.DiagramConstants.DefaultClassMultiplicities;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;
import com.clarity.sourcemodel.OOPSourceModelConstants;
import com.clarity.sourcemodel.OOPSourceModelConstants.AccessModifiers;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentInvocations;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentType;

/**
 * Returns a list of binary class relationships from a given source code model.
 *
 * @author Muntazir Fadhel
 * @param <T>
 *
 */
public class ClassRelationshipsExtractor<T> implements Serializable {

    public static enum InvocationSiteProperty {

        FIELD, LOCAL, NONE, METHOD_PARAMETER, CONSTRUCTOR_PARAMETER;
    }

    private static final long serialVersionUID = -8777271960106904851L;

    /**
     * Finds external class links from all the field, method, method param and
     * local var components in the given component.
     *
     * @param currentComponent
     *            the class that is being analyzed for associations.
     * @param components
     *            list of all components in the code base
     */
    private void genAssociations(final Component currentComponent, final Map<String, Component> components,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        // only consider non-base components (eg: methods, fields, etc ..)
        if (!currentComponent.componentType().isBaseComponent()) {
            // get the class the current component we are analyzing represents
            final Component currentClass = ClarpseUtil.getParentBaseComponent(currentComponent, components);

            if (currentClass != null) {
                // get a list of all the external class references this current
                // component has..
                final List<ComponentInvocation> externalClassTypeReferences = currentComponent.componentInvocations();

                for (final ComponentInvocation externalClassTypeRef : externalClassTypeReferences) {
                    final String externalClassType = externalClassTypeRef.invokedComponent();
                    if (components.containsKey(externalClassType)) {
                        final Component targetClass = components.get(externalClassType);
                        BinaryClassMultiplicity bCM = null;
                        BinaryClassAssociation bCA = null;
                        ExternalClassLink externalClassLink;
                        // check the component external types to see if we have
                        // some sort of array
                        for (final ComponentInvocation externalTypeRef : currentComponent
                                .componentInvocations(ComponentInvocations.DECLARATION)) {
                            final String externalType = externalTypeRef.invokedComponent();
                            if (OOPSourceModelConstants.getJavaCollections().containsKey(externalType)) {
                                bCM = new BinaryClassMultiplicity(DefaultClassMultiplicities.ZEROTOMANY);
                            }
                        }
                        // create external class link based on calling component
                        // type
                        // --> IF INVOCATION SITE IS CLASS FIELD:
                        if (currentComponent.componentType() == ComponentType.FIELD_COMPONENT) {
                            if (bCM == null) {
                                bCM = new BinaryClassMultiplicity(DefaultClassMultiplicities.ZEROTOONE);
                            }
                            if (currentComponent.modifiers()
                                    .contains(OOPSourceModelConstants.getJavaAccessModifierMap()
                                            .get(AccessModifiers.PRIVATE))
                                    || currentComponent.modifiers().contains(OOPSourceModelConstants
                                            .getJavaAccessModifierMap().get(AccessModifiers.PROTECTED))) {
                                bCA = BinaryClassAssociation.COMPOSITION;
                            } else {
                                bCA = BinaryClassAssociation.AGGREGATION;
                            }
                            externalClassLink = new ExternalClassLink(currentClass, targetClass, bCM,
                                    com.clarity.rest.ClarityUtil.InvocationSiteProperty.FIELD,
                                    currentComponent.modifiers(), bCA);

                            // --> IF INVOCATION SITE IS METHOD
                        } else if ((currentComponent.componentType() == ComponentType.METHOD_COMPONENT)
                                && !currentClass.uniqueName().equals(targetClass.uniqueName())) {
                            if (bCM == null) {
                                bCM = new BinaryClassMultiplicity(DefaultClassMultiplicities.ZEROTOONE);
                            }
                            if (currentComponent.modifiers()
                                    .contains(OOPSourceModelConstants.getJavaAccessModifierMap()
                                            .get(AccessModifiers.PRIVATE))
                                    || currentComponent.modifiers().contains(OOPSourceModelConstants
                                            .getJavaAccessModifierMap().get(AccessModifiers.PROTECTED))) {
                                bCA = BinaryClassAssociation.ASSOCIATION;
                            } else {
                                bCA = BinaryClassAssociation.WEAK_ASSOCIATION;
                            }
                            externalClassLink = new ExternalClassLink(currentClass, targetClass, bCM,
                                    com.clarity.rest.ClarityUtil.InvocationSiteProperty.METHOD_PARAMETER,
                                    currentComponent.modifiers(), bCA);

                            // --> IF INVOCATION SITE IS CONSTRUCTOR
                        } else if ((currentComponent.componentType() == ComponentType.CONSTRUCTOR_COMPONENT)
                                && !currentClass.uniqueName().equals(targetClass.uniqueName())) {
                            if (bCM == null) {
                                bCM = new BinaryClassMultiplicity(DefaultClassMultiplicities.ZEROTOONE);
                            }
                            bCA = BinaryClassAssociation.ASSOCIATION;
                            externalClassLink = new ExternalClassLink(currentClass, targetClass, bCM,
                                    com.clarity.rest.ClarityUtil.InvocationSiteProperty.CONSTRUCTOR_PARAMETER,
                                    currentComponent.modifiers(), bCA);

                            // --> IF INVOCATION SITE IS LOCAL VARIABLE
                        } else if ((currentComponent.componentType() == ComponentType.LOCAL_VARIABLE_COMPONENT)
                                && !currentClass.uniqueName().equals(targetClass.uniqueName())) {
                            if (bCM == null) {
                                bCM = new BinaryClassMultiplicity(DefaultClassMultiplicities.ZEROTOONE);
                            }
                            bCA = BinaryClassAssociation.WEAK_ASSOCIATION;
                            externalClassLink = new ExternalClassLink(currentClass, targetClass, bCM,
                                    com.clarity.rest.ClarityUtil.InvocationSiteProperty.CONSTRUCTOR_PARAMETER,
                                    currentComponent.modifiers(), bCA);
                        } else {
                            continue;
                        }

                        generateBinaryClassRelationship(externalClassLink, binaryRelationships);
                    }
                }
            }
        }
    }

    /**
     * Generates binary class relationships from the given external class link.
     *
     * @param externalClassLink
     *            external class link from which to generate binary class
     *            relationships
     */
    private void generateBinaryClassRelationship(final ExternalClassLink externalClassLink,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        // determine if we need to merge the incoming External Class Link with
        // an existing binary class relationship
        // between two classes
        final String possibleNameA = BinaryClassRelationship
                .generateRelationshipName(externalClassLink.getOrignalClass(), externalClassLink.getTargetClass());
        final String possibleNameB = BinaryClassRelationship
                .generateRelationshipName(externalClassLink.getTargetClass(), externalClassLink.getOrignalClass());

        if (binaryRelationships.containsKey(possibleNameA)) {
            binaryRelationships.get(possibleNameA).resolveExtClassLink(true, externalClassLink);
        } else if (binaryRelationships.containsKey(possibleNameB)) {
            binaryRelationships.get(possibleNameB).resolveExtClassLink(false, externalClassLink);
        } else {
            // there is no existing binary class relationship b/w the two
            // classes, create a new one
            binaryRelationships.put(BinaryClassRelationship
                    .generateRelationshipName(externalClassLink.getOrignalClass(), externalClassLink.getTargetClass()),
                    new BinaryClassRelationship(externalClassLink));
        }
    }

    /**
     * Analyzes the generalization relationships for the given class.
     */
    private void genClassGeneralizations(final Component sourceClass, final Map<String, Component> classes,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        final List<ComponentInvocation> superClasses = sourceClass.componentInvocations(ComponentInvocations.EXTENSION);
        if (!superClasses.isEmpty()) {
            for (final ComponentInvocation superClass : superClasses) {
                if (classes.containsKey(superClass.invokedComponent())) {
                    final Component targetClass = classes.get(superClass.invokedComponent());
                    final ExternalClassLink generalizationExternalClassLink = new ExternalClassLink(sourceClass,
                            targetClass, new BinaryClassMultiplicity(DefaultClassMultiplicities.NONE),
                            com.clarity.rest.ClarityUtil.InvocationSiteProperty.NONE, sourceClass.modifiers(),
                            BinaryClassAssociation.GENERALISATION);

                    generateBinaryClassRelationship(generalizationExternalClassLink, binaryRelationships);
                }
            }
        }
    }

    /**
     * @param starClass
     *            class to be analyzed for relationships.
     * @param codeBaseComponents
     *            list of all classes in the code base.
     */
    private void genClassifierRelationships(final Component starClass, final Map<String, Component> codeBaseComponents,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        // Scan class signature for any classes that have been extended
        genClassGeneralizations(starClass, codeBaseComponents, binaryRelationships);
        // Scan class signature for any classes that have been implemented
        genClassRealizations(starClass, codeBaseComponents, binaryRelationships);
        // Scan class fields for associations
        genAssociations(starClass, codeBaseComponents, binaryRelationships);
    }

    /**
     * Analyzes the realization relationships for the given class.
     */
    private void genClassRealizations(final Component sourceClass, final Map<String, Component> classes,
            final Map<String, BinaryClassRelationship> binaryRelationships) {

        final List<ComponentInvocation> implementedClasses = sourceClass
                .componentInvocations(ComponentInvocations.IMPLEMENTATION);
        if (!implementedClasses.isEmpty()) {
            for (final ComponentInvocation implementedClass : implementedClasses) {
                if (classes.containsKey(implementedClass.invokedComponent())) {
                    final Component targetClass = classes.get(implementedClass.invokedComponent());
                    final ExternalClassLink realizationExternalClassLink = new ExternalClassLink(sourceClass,
                            targetClass, new BinaryClassMultiplicity(DefaultClassMultiplicities.NONE),
                            com.clarity.rest.ClarityUtil.InvocationSiteProperty.NONE, sourceClass.modifiers(),
                            BinaryClassAssociation.REALIZATION);
                    generateBinaryClassRelationship(realizationExternalClassLink, binaryRelationships);
                }
            }
        }
    }

    /**
     * Parse through all the classes and populate our collection of binary class
     * relationships.
     *
     * @param sourceCodeModel
     *            the polyglot representation of the codebase.
     * @return list of all the binary class relationships found in the given
     *         source code model
     * @throws Exception
     */
    public final Map<String, BinaryClassRelationship> generateBinaryClassRelationships(
            final OOPSourceCodeModel sourceCodeModel) throws Exception {

        final Map<String, BinaryClassRelationship> binaryRelationships = new ConcurrentHashMap<String, BinaryClassRelationship>();
        final Map<String, com.clarity.sourcemodel.Component> classes = sourceCodeModel.getComponents();
        for (final Map.Entry<String, Component> entry : classes.entrySet()) {
            // get the class
            if (entry.getValue().componentType().isMethodComponent()
                    || entry.getValue().componentType().isBaseComponent()
                    || entry.getValue().componentType() == ComponentType.FIELD_COMPONENT) {
                final Component tempClass = entry.getValue();
                // generate the binary class relationships..
                genClassifierRelationships(tempClass, classes, binaryRelationships);
            }
        }
        return binaryRelationships;
    }
}