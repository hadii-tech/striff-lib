package com.clarity.binary.extractor;

import java.io.Serializable;

import com.clarity.binary.core.component.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.core.component.diagram.DiagramConstants.DefaultClassMultiplicities;
import com.clarity.sourcemodel.Component;

/**
 * Represents the relationship between two classes, implied context is a UML
 * Class Diagram.
 *
 * We maintain two arbitrary sides that exists on the ends of the relationship,
 * sides A and B. At each side in a binary class relationship, there exits a
 * class, association, multiplicity, etc.. - these are tracked and updated as
 * external link objects are received, see
 * resolveExtClassLinkWithBinaryRelationship().
 *
 * @author Muntazir Fadhel
 *
 */
public class BinaryClassRelationship implements Serializable {

    private static final long serialVersionUID = -9031833990867424972L;

    private static String classNameSplitter = "<<-->>";

    /**
     * @param originaClass
     *            original class
     * @param targetClass
     *            target class.
     * @return relationship name.
     */
    public static String generateRelationshipName(
            final Component originaClass,
            final Component targetClass) {

        return originaClass.name() + classNameSplitter + targetClass.name();
    }

    public static
    String getClassNameSplitter() {
        return classNameSplitter;
    }

    private final Component classA;
    private final Component classB;
    private BinaryClassMultiplicity aSideMultiplicity;
    private BinaryClassMultiplicity bSideMultiplicity;
    private BinaryClassAssociation aSideAssociation;
    private BinaryClassAssociation bSideAssociation;
    private final String name;
    private String aSideAction;
    private String bSideAction;

    /**
     * @param externalClassLink
     *            the external class link object from which to create the binary
     *            class relationship.
     */
    public BinaryClassRelationship(
            final ExternalClassLink externalClassLink) {

        classA = externalClassLink.getOrignalClass();
        classB = externalClassLink.getTargetClass();
        aSideMultiplicity = new BinaryClassMultiplicity(
                DefaultClassMultiplicities.NONE);
        bSideMultiplicity = externalClassLink
                .getTargetClassMultiplicity();
        aSideAssociation = externalClassLink.getAssociationType();
        bSideAssociation = BinaryClassAssociation.NONE;
        aSideAction = aSideAssociation.getAssociationLabel();
        bSideAction = "";
        name = (generateRelationshipName(classA, classB));
    }

    public final String getaSideAction() {
        return aSideAction;
    }

    public final BinaryClassAssociation getaSideAssociation() {
        return aSideAssociation;
    }

    public final BinaryClassMultiplicity getaSideMultiplicity() {
        return aSideMultiplicity;
    }

    /**
     * @param forwardDirection
     *            true if association goes from A to B (represents direction of
     *            arrow between to classes on a UML Class Diagram, false
     *            otherwise.
     * @return association type of the binary class relationship.
     */
    private BinaryClassAssociation getAssociation(
            final boolean forwardDirection) {

        if (forwardDirection) {
            return aSideAssociation;
        } else {
            return bSideAssociation;
        }
    }

    public final String getbSideAction() {
        return bSideAction;
    }

    public final BinaryClassAssociation getbSideAssociation() {
        return bSideAssociation;
    }

    public final BinaryClassMultiplicity getbSideMultiplicity() {
        return bSideMultiplicity;
    }

    public final Component getClassA() {
        return classA;
    }

    public final Component getClassB() {
        return classB;
    }

    public final String getName() {
        return name;
    }

    /**
     * @param forwardDir
     *            true if relationship association A -> B, false otherwise.
     * @param association
     *            association type.
     * @param targetMultiplicity
     *            multiplicity type on the target/end side.
     * @param code relevant code for the overwriting relationship
     */
    private void overwriteSideRelationship(final boolean forwardDir,
            final BinaryClassAssociation association,
            final BinaryClassMultiplicity targetMultiplicity) {
        if (forwardDir) {
            aSideAssociation = association;
            bSideMultiplicity = targetMultiplicity;
            aSideAction = association.getAssociationLabel();
        } else {
            bSideAssociation = association;
            aSideMultiplicity = targetMultiplicity;
            bSideAction = association.getAssociationLabel();
        }
    }

    /**
     * Updates the existing binary relationship b/w two classes to reflect the
     * newly found external class link.
     *
     * @param isDirForward
     *            The direction of the external class link
     * @param existingRelationship
     *            The existing binary class relationship
     * @param incomingLink
     *            the incoming external class link we need to incorporate into
     *            the binary class relationship
     * @return
     */
    public final void resolveExtClassLink(
            final boolean isDirForward,
            final ExternalClassLink incomingLink) {

        final BinaryClassAssociation currentSideAssociation =
                getAssociation(isDirForward);
        if (currentSideAssociation.getStrength() < incomingLink.getAssociationType()
                .getStrength()) {
            // if the incoming relationship is a weak association and the current relation is composition,
            // need special casing to make the relationship a composition relationship..
            if (((currentSideAssociation == BinaryClassAssociation.COMPOSITION)
                    && (incomingLink.getAssociationType() == BinaryClassAssociation.WEAK_ASSOCIATION))
                    || ((incomingLink.getAssociationType() == BinaryClassAssociation.COMPOSITION)
                            && (currentSideAssociation == BinaryClassAssociation.WEAK_ASSOCIATION))) {
                overwriteSideRelationship(isDirForward,
                        BinaryClassAssociation.AGGREGATION,
                        incomingLink.getTargetClassMultiplicity());
            } else {
                // otherwise, incoming association is stronger, need to edit current
                // relationship to reflect its properties
                overwriteSideRelationship(isDirForward,
                        incomingLink.getAssociationType(),
                        incomingLink.getTargetClassMultiplicity());
            }
        }
    }
}
