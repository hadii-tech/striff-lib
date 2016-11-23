package com.clarity.binary.diagram;

/**
 *
 * @author Muntazir Fadhel
 *
 */
public final class DiagramConstants {

    /**
     *
     * @author Muntazir Fadhel
     *
     */
    public enum BinaryClassAssociation {
        /**
         * Composition Association.
         */
        AGGREGATION(5, "Aggregates", "--", "o", "o"),
        /**
         * Weak Binary Association.
         */
        COMPOSITION(3, "Contains", "--", "*", "*"), DEPENDANCY(1, "Depends On", "-.-", "", ""),
        /**
         * Generalization Association.
         */
        GENERALISATION(7, "Extends", "--", "|>", "<|"),
        /**
         * Dependency association.
         */
        NONE(0, "", "-", "", ""), REALIZATION(6, "Implements", "-.-", "|>", "<|"),
        /**
        *
        */
        WEAK_ASSOCIATION(4, "Uses", "-", ">", "<"), ASSOCIATION(2, "Uses", "-", ">", "<");

        /**
         * label that appears at the top of class box in the class diagram.
         */
        private final String associationLabel;
        private final String forwardLinkEndingType;
        private final String backwardLinkEndingType;
        /**
         * type of link (dotted line, solid, etc....).
         */
        private final String yumlLinkType;
        private final int    strength;

        BinaryClassAssociation(final int strength, final String label, final String linkType,
                final String forwardLinkEndingType, String backwardLinkEndingType) {
            associationLabel = label;
            yumlLinkType = linkType;
            this.forwardLinkEndingType = forwardLinkEndingType;
            this.backwardLinkEndingType = backwardLinkEndingType;
            this.strength = strength;
        }

        public int getStrength() {
            return strength;
        }

        public String getAssociationLabel() {
            return associationLabel;
        }

        public String getyumlLinkType() {
            return yumlLinkType;
        }

        public String getForwardLinkEndingType() {
            return forwardLinkEndingType;
        }

        public String getBackwardLinkEndingType() {
            return backwardLinkEndingType;
        }
    }

    public static final String MULTIPLICITY_STRING_SEPERATOR = "..";

    /**
     * Represents the default class diagram multiplicities.
     *
     * @author Muntazir Fadhel
     */
    public enum DefaultClassMultiplicities {

        NONE(""), ONETOONE("1" + MULTIPLICITY_STRING_SEPERATOR + "1"), ZEROTOMANY(
                "0" + MULTIPLICITY_STRING_SEPERATOR + "*"), ZEROTOONE("0" + MULTIPLICITY_STRING_SEPERATOR + "1");

        private final String value;

        DefaultClassMultiplicities(final String sValue) {
            value = sValue;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * private constructor for utility class.
     */
    private DiagramConstants() {

    }

    public static final String PLANT_UML_DARK_THEME_STRING = "skinparam defaultFontName Consolas\n"
            + "skinparam backgroundColor #1d1f21\n" + "skinparam classArrowFontName  Consolas\n"
            + "skinparam classArrowColor #c5c8c6\n" + "skinparam classBackgroundColor #1d1f21\n"
            + "skinparam classArrowFontColor #c5c8c6\n" + "skinparam classArrowFontSize 16\n"
            + "skinparam classFontColor #22df80\n" + "skinparam classFontSize 16\n" + "skinparam classFontName Roboto\n"
            + "skinparam classStereotypeFontColor White\n" + "skinparam classAttributeFontColor #c5c8c6\n"
            + "skinparam classAttributeFontSize 14\n" + "skinparam classFontName Consolas\n"
            + "skinparam classAttributeFontName Consolas\n" + "skinparam titleFontColor White\n"   + "skinparam packageBackgroundColor #161719\n"
            + "skinparam titleFontName Copperplate Gothic Light\n" + "skinparam packageBorderColor #7e7e7e\n"
            + "skinparam packageFontColor  #7e7e7e\n" + "skinparam packageFontName Consolas\n"
            + "skinparam packageFontStyle plain\n" + "skinparam packageFontSize 16\n"
            + "skinparam classBorderColor #bbbbbb\n";

}
