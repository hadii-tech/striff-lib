package striff.test;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hadii.clarpse.compiler.Lang;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.striff.extractor.DiagramConstants;
import com.hadii.striff.extractor.ExtractedRelationships;

/**
 * Tests to ensure component relations are being extracted correctly.
 */
public class ExtractedRelationshipsIntegrationTest {

    private static ExtractedRelationships relations;
    private static OOPSourceCodeModel codeModel;

    @BeforeClass
    public static void setup() throws Exception {
        codeModel = TestUtil.sourceCodeModel("/yao-main.zip", Lang.GOLANG);
        relations = new ExtractedRelationships(codeModel);
    }

    @Test
    public void spotCheckChartStructAggregationRelationships() {
        relations.result().rels(
                codeModel.getComponent("chart.Chart").get());
        assertTrue(relations.result().rels(
                codeModel.getComponent("chart.Chart").get())
                .toString()
                .equals(
                        "[ComponentRelation{originalComponent=Chart, targetComponent=API," +
                                " targetMultiplicity=, associationType=AGGREGATION}, " +
                                "ComponentRelation{originalComponent=Chart, " +
                                "targetComponent=Filter, targetMultiplicity=, " +
                                "associationType=AGGREGATION}, " +
                                "ComponentRelation{originalComponent=Chart, " +
                                "targetComponent=Page, targetMultiplicity=, " +
                                "associationType=AGGREGATION}]"));
    }

    @Test
    public void spotCheckJwtClaimsStructNoRelationships() {
        assertTrue(relations.result().rels(codeModel.getComponent("helper.JwtClaims").get()).isEmpty());
    }

    @Test
    public void spotCheckPageStructAggregationRelationship() {
        assertTrue(relations.result().rels(codeModel.getComponent("share.Page").get())
                .stream().anyMatch(
                        componentRelation -> componentRelation
                                .toString()
                                .equals("ComponentRelation{originalComponent=Page, targetComponent=Render, " +
                                        "targetMultiplicity=, associationType=AGGREGATION}")));
    }

    @Test
    public void testSpecializationRelationship() {
        assertTrue(relations.result().relsByType(DiagramConstants.ComponentAssociation.SPECIALIZATION)
                .toString().equals("[ComponentRelation{originalComponent=Render, " +
                        "targetComponent=Importable, " +
                        "targetMultiplicity=, " +
                        "associationType=SPECIALIZATION}, " +
                        "ComponentRelation{originalComponent=Column, " +
                        "targetComponent=Importable, " +
                        "targetMultiplicity=, " +
                        "associationType=SPECIALIZATION}, " +
                        "ComponentRelation{originalComponent=Filter, " +
                        "targetComponent=Importable, " +
                        "targetMultiplicity=, " +
                        "associationType=SPECIALIZATION}, " +
                        "ComponentRelation{originalComponent=API, " +
                        "targetComponent=Importable, " +
                        "targetMultiplicity=, " +
                        "associationType=SPECIALIZATION}, " +
                        "ComponentRelation{originalComponent=Page, " +
                        "targetComponent=Importable, " +
                        "targetMultiplicity=, " +
                        "associationType=SPECIALIZATION}]"));
    }

    @Test
    public void testAssociationRelationship() {
        assertTrue(relations.result().relsByType(DiagramConstants.ComponentAssociation.WEAK_ASSOCIATION)
                .toString().equals("[ComponentRelation{originalComponent=Importer, " +
                        "targetComponent=Binding, targetMultiplicity=," +
                        " associationType=WEAK_ASSOCIATION}, " +
                        "ComponentRelation{originalComponent=WorkFlow," +
                        " targetComponent=Input, targetMultiplicity=, " +
                        "associationType=WEAK_ASSOCIATION}, " +
                        "ComponentRelation{originalComponent=Importer," +
                        " targetComponent=Source, targetMultiplicity=," +
                        " associationType=WEAK_ASSOCIATION}, " +
                        "ComponentRelation{originalComponent=Importer," +
                        " targetComponent=Mapping, " +
                        "targetMultiplicity=, " +
                        "associationType=WEAK_ASSOCIATION}, " +
                        "ComponentRelation{originalComponent=WorkFlow," +
                        " targetComponent=Condition, " +
                        "targetMultiplicity=, " +
                        "associationType=WEAK_ASSOCIATION}, " +
                        "ComponentRelation{originalComponent=WorkFlow," +
                        " targetComponent=Next, targetMultiplicity=, " +
                        "associationType=WEAK_ASSOCIATION}]"));
    }

    @Test
    public void testRealizationRelationship() {
        assertTrue(relations.result().relsByType(DiagramConstants.ComponentAssociation.REALIZATION)
                .toString().equals("[]"));
    }

    @Test
    public void testCompositionRelationship() {
        assertTrue(relations.result().relsByType(DiagramConstants.ComponentAssociation.COMPOSITION)
                .toString().equals("[]"));
    }
}
