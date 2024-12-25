package striff.test;

import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.clarpse.sourcemodel.OOPSourceModelConstants;
import com.hadii.striff.extractor.DiagramConstants.ComponentAssociation;
import com.hadii.striff.extractor.ComponentRelation;
import com.hadii.striff.extractor.RelationsMap;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests to ensure component relations are being extracted correctly.
 */
public class RelationsMapTest {

    private static Component classA;
    private static Component classB;


    @BeforeClass
    public static void setup()  {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        classA = new Component();
        classA.setName("ClassA");
        classA.setComponentName("ClassA");
        classA.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        codeModel.insertComponent(classA);
        classB = new Component();
        classB.setName("ClassB");
        classB.setComponentName("ClassB");
        classB.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        codeModel.insertComponent(classB);
    }


    @Test
    public void testAllRels()  {
        RelationsMap relMap = new RelationsMap();
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION));
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertEquals(2, relMap.allRels().size());
    }

    @Test
    public void testComponentRels()  {
        RelationsMap relMap = new RelationsMap();
        relMap.insertRelation(new ComponentRelation(
                                  classA, classB, null, ComponentAssociation.ASSOCIATION));
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertEquals(2, relMap.rels(classA).size());
    }

    @Test
    public void testContainRels()  {
        RelationsMap relMap = new RelationsMap();
        ComponentRelation rel1 = new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION);
        relMap.insertRelation(rel1);
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertTrue(relMap.contains(rel1));
    }

    @Test
    public void testHasRelsForCmp()  {
        RelationsMap relMap = new RelationsMap();
        ComponentRelation rel1 = new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION);
        relMap.insertRelation(rel1);
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertTrue(relMap.hasRels(classA.uniqueName()));
    }

    @Test
    public void testRelsByType()  {
        RelationsMap relMap = new RelationsMap();
        ComponentRelation rel1 = new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION);
        relMap.insertRelation(rel1);
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertEquals(1, relMap.relsByType(ComponentAssociation.AGGREGATION).size());
    }

    @Test
    public void testSignificantRels() {
        RelationsMap relMap = new RelationsMap();
        ComponentRelation rel1 = new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION);
        relMap.insertRelation(rel1);
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertSame(relMap.significantRels(classA).stream().findFirst().get().associationType(),
                   ComponentAssociation.AGGREGATION);
        assertEquals(1, relMap.significantRels(classA).size());
    }

    @Test
    public void testMostSignificantRel() {
        RelationsMap relMap = new RelationsMap();
        ComponentRelation rel1 = new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION);
        relMap.insertRelation(rel1);
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertSame(relMap.mostSignificantRelation(classA, classB).associationType(),
                   ComponentAssociation.AGGREGATION);
    }

    @Test
    public void testRelSize() {
        RelationsMap relMap = new RelationsMap();
        ComponentRelation rel1 = new ComponentRelation(
            classA, classB, null, ComponentAssociation.ASSOCIATION);
        relMap.insertRelation(rel1);
        relMap.insertRelation(new ComponentRelation(
            classA, classB, null, ComponentAssociation.AGGREGATION));
        assertEquals(2, relMap.size());
        assertEquals(2, relMap.allRels().size());
    }
}
