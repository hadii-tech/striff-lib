package com.clarity.rest.extractor;

import com.clarity.rest.core.component.diagram.DiagramConstants;
import com.clarity.rest.core.component.diagram.DiagramConstants.DefaultClassMultiplicities;

/**
 * Represents the multiplicity that can exist between two classes, implied
 * context is a UML Class Diagram.
 *
 * @author Muntazir Fadhel
 *
 */
public class BinaryClassMultiplicity {

    /**
     *
     */
    private String value;

    /**
     * @param m DefaultClassMultiplicites.
     */
    public BinaryClassMultiplicity(final DefaultClassMultiplicities m) {
        this.value = m.getValue();
    }

    /**
     * @param start start multiplicity type.
     * @param end end multiplicity type.
     */
    public BinaryClassMultiplicity(final String start, final String end) {
        this.value = start
                + DiagramConstants.MULTIPLICITY_STRING_SEPERATOR + end;
    }

    public final String getValue() {
        return this.value;
    }

    /**
     * @param multiplicityValue value.
     */
    public final void setValue(final String multiplicityValue) {
        this.value = multiplicityValue;
    }
}
