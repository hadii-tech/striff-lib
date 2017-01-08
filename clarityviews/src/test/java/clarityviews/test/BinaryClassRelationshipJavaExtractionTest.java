package clarityviews.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.clarity.ClarpseUtil;
import com.clarity.binary.diagram.DiagramConstants.BinaryClassAssociation;
import com.clarity.binary.diagram.DiagramConstants.DefaultClassMultiplicities;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.binary.extractor.ClassRelationshipsExtractor;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.parser.Lang;
import com.clarity.parser.ParseRequestContent;
import com.clarity.parser.RawFile;
import com.clarity.sourcemodel.OOPSourceCodeModel;

/**
 * Tests to ensure binary class relationships are being extracted properly.
 */
public class BinaryClassRelationshipJavaExtractionTest {

    @Test
    public void testFieldVarLevelRelationExists() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        System.out.println(ClarpseUtil.fromJavaToJson(codeModel));
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_B = binaryRelationships.get("ClassA<<-->>ClassB");
        Assert.assertTrue("Relationship between ClassA and ClassB does not exist!", A_B != null);
    }

    @Test
    public void testFieldVarLevelRelationTypes() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_B = binaryRelationships.get("ClassA<<-->>ClassB");
        Assert.assertTrue("Relationship from ClassA to ClassB should be 'Composition'!",
                A_B.getaSideAssociation() == BinaryClassAssociation.COMPOSITION);
        Assert.assertTrue("Relationship from ClassB to ClassA should be 'None'!",
                A_B.getbSideAssociation() == BinaryClassAssociation.NONE);
    }

    @Test
    public void testFieldVarLevelRelationMultiplicity() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_B = binaryRelationships.get("ClassA<<-->>ClassB");
        Assert.assertTrue("Multiciplicity from ClassA to ClassB should be '0..*'!",
                A_B.getbSideMultiplicity().getValue().equals(DefaultClassMultiplicities.ZEROTOMANY.getValue()));
        Assert.assertTrue("Multiciplicity from ClassB to ClassA should be '0..*'!",
                A_B.getaSideMultiplicity().getValue().equals(DefaultClassMultiplicities.NONE.getValue()));
    }

    @Test
    public void testInterfaceLevelRelationExists() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "import java.util.ArrayList;" + "public class ClassA implements ClassC {}");
        final RawFile file2 = new RawFile("ClassC.java", "package com.sample; public interface ClassC {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_C = binaryRelationships.get("ClassA<<-->>ClassC");
        Assert.assertTrue("Relationship from ClassA to interfaceC should be 'Realization'",
                A_C.getaSideAssociation() == BinaryClassAssociation.REALIZATION);
        Assert.assertTrue("Relationship from InterfaceC to ClassA should be 'None'",
                A_C.getbSideAssociation() == BinaryClassAssociation.NONE);
    }

    @Test
    public void testInterfaceLevelRelationMultiplicities() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "import java.util.ArrayList;" + "public class ClassA implements ClassC {}");
        final RawFile file2 = new RawFile("ClassC.java", "package com.sample; public interface ClassC {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_C = binaryRelationships.get("ClassA<<-->>ClassC");
        Assert.assertTrue("Multiciplicity from ClassA to InterfaceC should be '0..0'!",
                A_C.getaSideMultiplicity().getValue().equals(DefaultClassMultiplicities.NONE.getValue()));
        Assert.assertTrue("Multiciplicity from InterfaceC to Class A should be '0..0'!",
                A_C.getbSideMultiplicity().getValue().equals(DefaultClassMultiplicities.NONE.getValue()));
    }

    @Test
    public void testExtensionLevelRelationExists() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + " import java.util.ArrayList;" + "public class ClassA extends ClassD {}");
        final RawFile file2 = new RawFile("ClassD.java", "package com.sample; public class ClassD {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_D = binaryRelationships.get("ClassA<<-->>ClassD");
        Assert.assertTrue("Relationship between ClassA and ClassD does not exist!", A_D != null);
    }

    @Test
    public void testExtensionLevelRelationTypes() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;  public class ClassA extends ClassD {}");
        final RawFile file2 = new RawFile("ClassD.java", "package com.sample; public class ClassD {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_D = binaryRelationships.get("ClassA<<-->>ClassD");
        Assert.assertTrue("Relationship from ClassA to ClassD should be 'Generalization'!",
                A_D.getaSideAssociation() == BinaryClassAssociation.GENERALISATION);
        Assert.assertTrue("Relationship from ClassD to ClassA should be 'Generalization'!",
                A_D.getbSideAssociation() == BinaryClassAssociation.NONE);
    }

    @Test
    public void testExtensionLevelRelationMultiplicities() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "import java.util.ArrayList;" + "public class ClassA extends ClassD {}");
        final RawFile file2 = new RawFile("ClassD.java", "package com.sample; public class ClassD {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        file.name("sample.java");
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_D = binaryRelationships.get("ClassA<<-->>ClassD");
        Assert.assertTrue("Multiciplicity from ClassA to ClassD should be 'None'!",
                A_D.getaSideMultiplicity().getValue().equals(DefaultClassMultiplicities.NONE.getValue()));
        Assert.assertTrue("Multiciplicity from ClassD to Class A should be 'None'!",
                A_D.getbSideMultiplicity().getValue().equals(DefaultClassMultiplicities.NONE.getValue()));
    }

    @Test
    public void testMethodReturnTypeLevelRelationMultiplicities() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "import java.util.ArrayList;" + "public class ClassA { ClassE aMethod() {}}");
        final RawFile file2 = new RawFile("ClassE.java", "package com.sample; public class ClassE {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        file.name("sample.java");
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_E = binaryRelationships.get("ClassA<<-->>ClassE");
        Assert.assertTrue("Multiciplicity from ClassA to ClassE should be 'None'!",
                A_E.getaSideMultiplicity().getValue().equals(
                        com.clarity.binary.diagram.DiagramConstants.DefaultClassMultiplicities.NONE.getValue()));
        Assert.assertTrue("Multiciplicity from ClassD to Class A should be 'None'!",
                A_E.getbSideMultiplicity().getValue().equals(
                        com.clarity.binary.diagram.DiagramConstants.DefaultClassMultiplicities.ZEROTOONE.getValue()));
    }

    @Test
    public void testMethodReturnTypeLevelRelationTypes() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "import java.util.ArrayList;" + "public class ClassA { ClassE aMethod() {}}");
        final RawFile file2 = new RawFile("ClassE.java", "package com.sample; public class ClassE {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        file.name("sample.java");
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        final BinaryClassRelationship A_E = binaryRelationships.get("ClassA<<-->>ClassE");
        Assert.assertTrue("Multiciplicity from ClassA to ClassE should be 'None'!",
                A_E.getaSideAssociation() == BinaryClassAssociation.WEAK_ASSOCIATION);
        Assert.assertTrue("Multiciplicity from ClassA to ClassE should be 'None'!",
                A_E.getbSideAssociation() == BinaryClassAssociation.NONE);
    }

    /**
     * Class relationships from overridden methods should not be displayed as
     * the relation is already clear from the original implemented/extended
     * class.
     */
    @Test
    public void testNoRelationsGeneratedFromOverridenMethods() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package test; public interface ClassA { ClassD aMethod();}");
        final RawFile file2 = new RawFile("ClassE.java",
                "package test; public class ClassE implements ClassA { public ClassD aMethod () {} }");
        final RawFile file3 = new RawFile("ClassD.java", "package test; public class ClassD { }");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject((reqCon)).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        assertTrue(binaryRelationships.get("ClassA<<-->>ClassD") != null);
        assertTrue(binaryRelationships.get("ClassA<<-->>ClassE") == null);
    }
}
