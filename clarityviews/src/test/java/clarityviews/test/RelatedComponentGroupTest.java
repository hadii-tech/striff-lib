package clarityviews.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.clarity.binary.diagram.RelatedComponentsGroup;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.binary.extractor.ClassRelationshipsExtractor;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.parser.Lang;
import com.clarity.parser.ParseRequestContent;
import com.clarity.parser.RawFile;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;

/**
 * The {@link RelatedComponentsGroup} class is used to determine what
 * {@link Component}s to be displayed on a diagram. These tests exist to
 * maintain the optimal {@link Component}s are being chosen based on the given
 * code base and desired result set size.
 */
public class RelatedComponentGroupTest {

    /**
     * A sample code base of three components where there exists an inheritance
     * relationship and a composition relationship. As the desired Result Size
     * is set to 2, we expect the ClassB involved in the Composition
     * relationship to be present in the component grouping result.
     */
    @Test
    public void testA() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java",
                "package com.sample; public class ClassB implements ClassD {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        assertTrue(new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 2).components()
                        .contains(codeModel.getComponent("com.sample.ClassB")));
    }

    /**
     * A sample code base of three components where there exists an inheritance
     * relationship and a composition relationship. As the desired Result Size
     * is set to 3, we expect the component involved in the inheritance
     * relationship to be present in the component grouping result making the
     * overall size 3 components.
     */
    @Test
    public void testB() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java",
                "package com.sample; public class ClassB implements ClassD {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 3).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.size() == 3);
    }

    /**
     * A sample code base of three components where there exists an extensions
     * relationship and a composition relationship. As the desired Result Size
     * is set to 3, we expect the component involved in the extension
     * relationship to be present in the component grouping result making the
     * overall size 3 components.
     */
    @Test
    public void testC() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB extends ClassD {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 3).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.size() == 3);
    }

    /**
     * A sample code base of three components where there exists an inheritance
     * relationship and a composition relationship. As the desired Result Size
     * is set to 1, we expect the component involved in the inheritance
     * relationship to be present in the component grouping result making the
     * overall size 2 components.
     */
    @Test
    public void testD() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + " import java.util.ArrayList;"
                + "public class ClassA implements ClassD {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 1).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.size() == 2);
    }

    /**
     * A sample code base of three components where there exists two Composition
     * relationships. One Component composes the main component, and the main
     * component composes a different third component. With a desired result
     * size of 3, we expect all the components to be returned. This test ensures
     * the diagrams will extend in both directions.
     */
    @Test
    public void testE() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + "import java.util.ArrayList;"
                + "public class ClassA {  private ArrayList<ClassB> b;}");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB {}");
        final RawFile file3 = new RawFile("ClassD.java",
                "package com.sample; public class ClassD  { private ClassA classA;}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 3).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassB")));
        assertTrue(cmps.size() == 3);
    }

    /**
     * A sample code base of three components where there exists two Inheritance
     * relationships. The main component inherits a Component, and a third
     * component inherits the main component. This test ensures the diagrams
     * will extend in both directions.
     */
    @Test
    public void testF() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "public class ClassA implements ClassD { }");
        final RawFile file2 = new RawFile("ClassB.java",
                "package com.sample; public class ClassB implements ClassA {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  { }");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 3).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassB")));
        assertTrue(cmps.size() == 3);
    }

    /**
     * A sample code base of three components where there exists one Inheritance
     * relationship and one extension relationship. The main component inherits
     * a Component, and a third component extends the main component. This test
     * ensures the diagrams will extend in both directions.
     */
    @Test
    public void testG() throws Exception {
        final RawFile file = new RawFile("ClassA.java",
                "package com.sample;" + "public class ClassA implements ClassD { }");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB extends ClassA {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  { }");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 3).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassB")));
        assertTrue(cmps.size() == 3);
    }

    /**
     * A sample code base of three components where there exists no
     * relationships. We expect only the main Component to be returned.
     */
    @Test
    public void testH() throws Exception {
        final RawFile file = new RawFile("ClassA.java", "package com.sample;" + " public class ClassA { }");
        final RawFile file2 = new RawFile("ClassB.java", "package com.sample; public class ClassB {}");
        final RawFile file3 = new RawFile("ClassD.java", "package com.sample; public class ClassD  {}");
        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        final ArrayList<ParseRequestContent> reqCons = new ArrayList<ParseRequestContent>();
        reqCon.insertFile(file);
        reqCon.insertFile(file2);
        reqCon.insertFile(file3);
        reqCons.add(reqCon);
        final OOPSourceCodeModel codeModel = new ParsedProject(reqCon).model();
        final ClassRelationshipsExtractor<?> bCAS = new ClassRelationshipsExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA"), 3).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassA")));
        assertTrue(cmps.size() == 1);
    }
}
