package striff.test;

import com.hadii.clarpse.compiler.ClarpseProject;
import com.hadii.clarpse.compiler.Lang;
import com.hadii.clarpse.compiler.ProjectFile;
import com.hadii.clarpse.compiler.ProjectFiles;
import com.hadii.clarpse.reference.SimpleTypeReference;
import com.hadii.clarpse.reference.TypeExtensionReference;
import com.hadii.clarpse.reference.TypeImplementationReference;
import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.clarpse.sourcemodel.OOPSourceModelConstants;
import com.hadii.striff.extractor.ComponentRelation;
import com.hadii.striff.extractor.DiagramConstants.ComponentAssociation;
import com.hadii.striff.extractor.DiagramConstants.DefaultClassMultiplicities;
import com.hadii.striff.extractor.ExtractedRelationships;
import com.hadii.striff.extractor.RelationsMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests to ensure component relations are being extracted correctly.
 */
public class ExtractedRelationshipsTest {

    @Test
    public void testJavaCompositionRelationExists() throws Exception {
        final ProjectFile fileA = new ProjectFile("/ClassA.java", "package com.sample;" + " import" +
                " java.util.ArrayList;"
                + " public class ClassA {  private ArrayList<ClassB> b;}");
        final ProjectFile fileB = new ProjectFile("/ClassB.java", "package com.sample; public " +
                "class ClassB {}");
        final ProjectFiles pfs = new ProjectFiles();
        pfs.insertFile(fileA);
        pfs.insertFile(fileB);
        final OOPSourceCodeModel codeModel = new ClarpseProject(pfs, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertTrue(classARelations.size() == 1 &&
                classARelations.stream().findFirst().get().originalComponent().name().equals(
                        "ClassA")
                &&
                classARelations.stream().findFirst().get().targetComponent().name().equals("ClassB") &&
                classARelations.stream().findFirst().get().associationType() == ComponentAssociation.COMPOSITION);
    }

    @Test
    public void testJavaCompositionRelationMultiplicities() throws Exception {
        final ProjectFile fileA = new ProjectFile("/ClassA.java", "package com.sample;" + " import" +
                " java.util.ArrayList;"
                + " public class ClassA {  private ArrayList<ClassB> b;}");
        final ProjectFile fileB = new ProjectFile("/ClassB.java", "package com.sample; public " +
                "class ClassB {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(fileA);
        ProjectFiles.insertFile(fileB);
        final OOPSourceCodeModel codeModel = 
                new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponentRelationMultiplicity().value(),
                DefaultClassMultiplicities.NONE.value());
    }

    @Test
    public void testJavaCompositionRelationTypes() throws Exception {
        final ProjectFile fileA = new ProjectFile("/ClassA.java", "package com.sample;" + " import" +
                " java.util.ArrayList;"
                + " public class ClassA {  private ArrayList<ClassB> b;}");
        final ProjectFile fileB = new ProjectFile("/ClassB.java", "package com.sample; public " +
                "class ClassB {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(fileA);
        ProjectFiles.insertFile(fileB);
        final OOPSourceCodeModel codeModel = 
                new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Component componentB = codeModel.getComponent("com.sample.ClassB").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().originalComponent(), componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponent(), componentB);
    }

    @Test
    public void testJavaRelationDoesNotExist() throws Exception {
        final ProjectFile fileA = new ProjectFile("/ClassA.java", "package com.sample;" + " import" +
                " java.util.ArrayList;"
                + " public class ClassA {  private ArrayList<ClassB> b;}");
        final ProjectFile fileB = new ProjectFile("/ClassB.java", "package com.sample; public " +
                "class ClassB {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(fileA);
        ProjectFiles.insertFile(fileB);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentB = codeModel.getComponent("com.sample.ClassB").get();
        Set<ComponentRelation> classBRelations = relations.result().rels(componentB);
        assertTrue(classBRelations.isEmpty());
    }

    @Test
    public void testJavaRealizationRelationExists() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + " public class ClassA " +
                        "implements ClassC {}");
        final ProjectFile file2 = new ProjectFile("/ClassC.java", "package com.sample; public " +
                "interface ClassC {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        Assert.assertSame(classARelations.stream().findFirst().get().associationType(),
                ComponentAssociation.REALIZATION);
    }

    @Test
    public void testJavaRealizationRelationMultiplicities() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + " public class ClassA " +
                        "implements ClassC {}");
        final ProjectFile file2 = new ProjectFile("/ClassC.java", "package com.sample; public " +
                "interface ClassC {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponentRelationMultiplicity().value(),
                DefaultClassMultiplicities.NONE.value());
    }

    @Test
    public void testJavaRealizationRelationTypes() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + " public class ClassA " +
                        "implements ClassC {}");
        final ProjectFile file2 = new ProjectFile("/ClassC.java", "package com.sample; public " +
                "interface ClassC {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Component componentC = codeModel.getComponent("com.sample.ClassC").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().originalComponent(), componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponent(), componentC);
    }

    @Test
    public void testJavaSpecializationRelationExists() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + "public class ClassA extends" +
                        " ClassD {}");
        final ProjectFile file2 = new ProjectFile("/ClassD.java", "package com.sample; public " +
                "class ClassD {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        Assert.assertSame(classARelations.stream().findFirst().get().associationType(),
                ComponentAssociation.SPECIALIZATION);
        assertEquals("com.sample.ClassD",
                classARelations.stream().findFirst().get().targetComponent().uniqueName());
    }

    @Test
    public void testJavaSpecializationRelationMultiplicity() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + "public class ClassA extends" +
                        " ClassD {}");
        final ProjectFile file2 = new ProjectFile("/ClassD.java", "package com.sample; public " +
                "class ClassD {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponentRelationMultiplicity().value(),
                DefaultClassMultiplicities.NONE.value());
    }

    @Test
    public void testJavaSpecializationRelationTypes() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + "public class ClassA extends" +
                        " ClassD {}");
        final ProjectFile file2 = new ProjectFile("/ClassD.java", "package com.sample; public " +
                "class ClassD {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Component componentD = codeModel.getComponent("com.sample.ClassD").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().originalComponent(), componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponent(), componentD);
    }

    @Test
    public void testJavaAssociationRelationExists() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + "import java.util" +
                        ".ArrayList;" + "public class ClassA { " +
                        "ClassE aMethod() {}}");
        final ProjectFile file2 = new ProjectFile("/ClassE.java", "package com.sample; public " +
                "class ClassE {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertTrue(classARelations.size() == 1 && classARelations.stream().findFirst().get()
                .associationType() == ComponentAssociation.WEAK_ASSOCIATION);
    }

    @Test
    public void testJavaAssociationRelationTypes() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + "import java.util" +
                        ".ArrayList;" + "public class ClassA { " +
                        "ClassE aMethod() {}}");
        final ProjectFile file2 = new ProjectFile("/ClassE.java", "package com.sample; public " +
                "class ClassE {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Component componentE = codeModel.getComponent("com.sample.ClassE").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().originalComponent(), componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponent(), componentE);
    }

    @Test
    public void testJavaLocalVarAssociationRelationExists() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + "class ClassA { void test ()" +
                        " { ClassD d; } }");
        final ProjectFile file2 = new ProjectFile("/ClassD.java", "package com.sample; public " +
                "class ClassD {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        Assert.assertSame(classARelations.stream().findFirst().get().associationType(),
                ComponentAssociation.WEAK_ASSOCIATION);
        assertEquals("com.sample.ClassD",
                classARelations.stream().findFirst().get().targetComponent().uniqueName());
    }

    @Test
    public void testJavaLocalVarAssociationRelationMultiplicity() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + "class ClassA { void test ()" +
                        " { ClassD d; } }");
        final ProjectFile file2 = new ProjectFile("/ClassD.java", "package com.sample; public " +
                "class ClassD {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponentRelationMultiplicity().value(),
                DefaultClassMultiplicities.NONE.value());
    }

    @Test
    public void testJavaLocalVarAssociationRelationTypes() throws Exception {
        final ProjectFile file = new ProjectFile("/ClassA.java",
                "package com.sample;" + " import java.util" +
                        ".ArrayList;" + "class ClassA { void test ()" +
                        " { ClassD d; } }");
        final ProjectFile file2 = new ProjectFile("/ClassD.java", "package com.sample; public " +
                "class ClassD {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("com.sample.ClassA").get();
        Component componentD = codeModel.getComponent("com.sample.ClassD").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertEquals(classARelations.stream().findFirst().get().originalComponent(), componentA);
        assertEquals(classARelations.stream().findFirst().get().targetComponent(), componentD);
    }

    @Test
    public void testComponentRelationWithSameComponentsAreEqual() {
        Component cmpA = new Component();
        cmpA.setName("ClassA");
        cmpA.setComponentName("ClassA");
        cmpA.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        Component cmpB = new Component();
        cmpB.setName("classB");
        cmpB.setComponentName("classB");
        cmpB.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        ComponentRelation linkA = new ComponentRelation(
                cmpA,
                cmpB, null,
                ComponentAssociation.COMPOSITION);
        ComponentRelation linkB = new ComponentRelation(
                cmpA,
                cmpB, null,
                ComponentAssociation.COMPOSITION);
        assert (linkA.hashCode() == linkB.hashCode());
    }

    @Test
    public void testComponentRelationWithSameComponentsDifferentAssociationsAreEqual() {
        Component cmpA = new Component();
        cmpA.setName("ClassA");
        cmpA.setComponentName("ClassA");
        cmpA.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        Component cmpB = new Component();
        cmpB.setName("classB");
        cmpB.setComponentName("classB");
        cmpB.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        ComponentRelation linkA = new ComponentRelation(
                cmpA,
                cmpB, null,
                ComponentAssociation.REALIZATION);
        ComponentRelation linkB = new ComponentRelation(
                cmpA,
                cmpB, null,
                ComponentAssociation.ASSOCIATION);
        assert (linkA.hashCode() == linkB.hashCode());
    }

    @Test
    public void testComponentRelationWithDifferentComponentsAreUnequal() {
        Component cmpA = new Component();
        cmpA.setName("ClassA");
        cmpA.setComponentName("ClassA");
        cmpA.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        Component cmpB = new Component();
        cmpB.setName("classB");
        cmpB.setComponentName("classB");
        cmpB.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        Component cmpC = new Component();
        cmpC.setName("classC");
        cmpC.setComponentName("classC");
        cmpC.setComponentType(OOPSourceModelConstants.ComponentType.CLASS);
        ComponentRelation linkA = new ComponentRelation(
                cmpB,
                cmpA, null,
                ComponentAssociation.COMPOSITION);
        ComponentRelation linkB = new ComponentRelation(
                cmpB,
                cmpC, null,
                ComponentAssociation.COMPOSITION);
        assert (linkA.hashCode() != linkB.hashCode());
    }

    /**
     * Tests that even in the presence of multiple references between a pair of
     * components, there
     * is a single relation that represents the relationship between them in any
     * given direction.
     */
    @Test
    public void testRelationOverridingBetweenComponentPairs() throws Exception {
        // Class A uses Class B in two areas (Return type and method param). There
        // however should
        // only be ONE ASSOCIATION relation from ClassA -> ClassE
        final ProjectFile file = new ProjectFile("/ClassA.java", "public class ClassA { ClassB " +
                "classB1;\n void aMethod(ClassB classB2) {}}");
        final ProjectFile file2 = new ProjectFile("/ClassB.java", "public class ClassB {}");
        final ProjectFiles ProjectFiles = new ProjectFiles();
        ProjectFiles.insertFile(file);
        ProjectFiles.insertFile(file2);
        final OOPSourceCodeModel codeModel = new ClarpseProject(ProjectFiles, Lang.JAVA).result().model();
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        Component componentA = codeModel.getComponent("ClassA").get();
        Set<ComponentRelation> classARelations = relations.result().rels(componentA);
        assertTrue(classARelations.size() == 2
                && classARelations.stream().findFirst().get().associationType() == ComponentAssociation.AGGREGATION);
    }

    @Test
    public void testRelationsGeneratedFromNonBaseComponentsOnly() {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        Component structA = setupComponent("structA", "structA", codeModel,
                OOPSourceModelConstants.ComponentType.STRUCT);
        structA.insertCmpRef(new SimpleTypeReference("structB"));
        structA.insertAccessModifier(OOPSourceModelConstants.AccessModifiers.PRIVATE.name());
        Component structB = setupComponent("structB", "structB", codeModel,
                OOPSourceModelConstants.ComponentType.STRUCT);
        new ComponentRelation(
                structA,
                structB, null,
                ComponentAssociation.COMPOSITION);
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        assertTrue(relations.result().allRels().isEmpty());
    }

    @Test
    public void testGenericCompositionRelationThroughFieldVar() {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        Component structA = setupComponent("structA", "structA", codeModel,
                OOPSourceModelConstants.ComponentType.STRUCT);
        Component structAField = setupComponent("fieldVar", "structA.fieldVar", codeModel,
                OOPSourceModelConstants.ComponentType.FIELD);
        structA.insertChildComponent("structA.fieldVar");
        structAField.insertCmpRef(new SimpleTypeReference("structB"));
        structAField.insertAccessModifier(
                OOPSourceModelConstants.AccessModifiers.PRIVATE.name().toLowerCase());
        Component structB = setupComponent("structB", "structB", codeModel,
                OOPSourceModelConstants.ComponentType.STRUCT);
        ComponentRelation expectedRelation = new ComponentRelation(
                structA,
                structB, null,
                ComponentAssociation.COMPOSITION);
        final RelationsMap relations = new ExtractedRelationships(codeModel).result();
        assertTrue(relations.contains(expectedRelation));
    }

    @Test
    public void testGenericAggregationRelationThroughFieldVar() {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        Component structA = setupComponent("structA", "structA", codeModel,
                OOPSourceModelConstants.ComponentType.STRUCT);
        Component structAField = setupComponent("fieldVar", "structA.fieldVar", codeModel,
                OOPSourceModelConstants.ComponentType.FIELD);
        structA.insertChildComponent("structA.fieldVar");
        structAField.insertCmpRef(new SimpleTypeReference("structB"));
        structAField.insertAccessModifier(
                OOPSourceModelConstants.AccessModifiers.PUBLIC.name().toLowerCase());
        Component structB = setupComponent("structB", "structB", codeModel,
                OOPSourceModelConstants.ComponentType.STRUCT);
        ComponentRelation expectedRelation = new ComponentRelation(
                structA,
                structB, null,
                ComponentAssociation.AGGREGATION);
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        assertTrue(relations.result().contains(expectedRelation));
    }

    @Test
    public void testGenericMethodAssociationRelationExists() {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        Component classA = setupComponent("classA", "classA", codeModel,
                OOPSourceModelConstants.ComponentType.CLASS);
        Component classAMethod = setupComponent("methodA", "classA.methodA", codeModel,
                OOPSourceModelConstants.ComponentType.METHOD);
        classA.insertChildComponent("classA.methodA");
        classAMethod.insertCmpRef(new SimpleTypeReference("classB"));
        Component classB = setupComponent("classB", "classB", codeModel,
                OOPSourceModelConstants.ComponentType.CLASS);
        ComponentRelation expectedRelation = new ComponentRelation(
                classA,
                classB, null,
                ComponentAssociation.WEAK_ASSOCIATION);
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        assertTrue(relations.result().contains(expectedRelation));
    }

    @Test
    public void testGenericSpecializationRelationExists() {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        Component classA = setupComponent("classA", "classA", codeModel,
                OOPSourceModelConstants.ComponentType.CLASS);
        classA.insertCmpRef(new TypeExtensionReference("classB"));
        Component classB = setupComponent("classB", "classB", codeModel,
                OOPSourceModelConstants.ComponentType.CLASS);
        ComponentRelation expectedRelation = new ComponentRelation(
                classA,
                classB, null,
                ComponentAssociation.SPECIALIZATION);
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        assertTrue(relations.result().contains(expectedRelation));
    }

    @Test
    public void testGenericRealizationRelationExists() {
        OOPSourceCodeModel codeModel = new OOPSourceCodeModel();
        Component classA = setupComponent("classA", "classA", codeModel,
                OOPSourceModelConstants.ComponentType.CLASS);
        classA.insertCmpRef(new TypeImplementationReference("classB"));
        Component classB = setupComponent("classB", "classB", codeModel,
                OOPSourceModelConstants.ComponentType.CLASS);
        ComponentRelation expectedRelation = new ComponentRelation(
                classA,
                classB, null,
                ComponentAssociation.REALIZATION);
        final ExtractedRelationships relations = new ExtractedRelationships(codeModel);
        assertTrue(relations.result().contains(expectedRelation));
    }

    private Component setupComponent(String name, String componentName,
            OOPSourceCodeModel codeModel,
            OOPSourceModelConstants.ComponentType type) {
        Component cmp = new Component();
        cmp.setName(name);
        cmp.setComponentName(componentName);
        cmp.setComponentType(type);
        codeModel.insertComponent(cmp);
        return cmp;
    }
}
