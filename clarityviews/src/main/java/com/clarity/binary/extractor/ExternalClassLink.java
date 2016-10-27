package com.clarity.binary.extractor;

import java.util.Set;

import com.clarity.binary.ClarityUtil;
import com.clarity.binary.ClarityUtil.InvocationSiteProperty;
import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.sourcemodel.Component;

/**
 * Within a source class, there are many references or 'links' to external
 * classes (uni-directional)- either through class fields declarations or method
 * invocations. This class represents such a type of link.
 *
 * @author Muntazir Fadhel
 *
 */
public class ExternalClassLink {

    private final Component               orignalClass;
    private final Component               targetClass;
    // linkTargetMultiplicity of the link..
    private final BinaryClassMultiplicity targetClassMultiplicity;
    // type of variable invocation
    private final InvocationSiteProperty  invocationType;
    private final Set<String>             modifierContexts;
    private final BinaryClassAssociation  associationType;

    /**
     * @param originalClass
     *            original class
     * @param linkTargetClass
     *            target class
     * @param linkTargetClassMultiplicity
     *            multiplicity from origin to target
     * @param linkInvocationType
     *            innovation type
     * @param callingComponentType
     * @param set
     *            modifiers
     * @param associationType
     *            association type
     * @param set
     */
    public ExternalClassLink(final Component originalClass, final Component linkTargetClass,
            final BinaryClassMultiplicity linkTargetClassMultiplicity, final InvocationSiteProperty linkInvocationType,
            final Set<String> set, final BinaryClassAssociation associationType) {

        targetClassMultiplicity = (linkTargetClassMultiplicity);
        invocationType = (linkInvocationType);
        orignalClass = (originalClass);
        targetClass = (linkTargetClass);
        this.modifierContexts = set;
        this.associationType = (associationType);
    }

    /**
     * @return the modifierContexts
     */
    public Set<String> getModifierContexts() {
        return modifierContexts;
    }

    /**
     * @return the targetClassMultiplicity
     */
    public BinaryClassMultiplicity getTargetClassMultiplicity() {
        return targetClassMultiplicity;
    }

    /**
     * @return the invocationType
     */
    public ClarityUtil.InvocationSiteProperty getInvocationType() {
        return invocationType;
    }

    /**
     * @return the targetClass
     */
    public Component getTargetClass() {
        return targetClass;
    }

    /**
     * @return the orignalClass
     */
    public Component getOrignalClass() {
        return orignalClass;
    }

    /**
     * @return the associationType
     */
    public BinaryClassAssociation getAssociationType() {
        return associationType;
    }
}
