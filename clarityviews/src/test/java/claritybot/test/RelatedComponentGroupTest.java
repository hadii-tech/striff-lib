package claritybot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.clarity.binary.diagram.RelatedBaseComponentsGroup;
import com.clarity.binary.extractor.BinaryClassRelationship;
import com.clarity.binary.extractor.BinaryClassRelationshipExtractor;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.parser.Lang;
import com.clarity.parser.ParseRequestContent;
import com.clarity.parser.RawFile;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceCodeModel;

/**
 * The {@link RelatedBaseComponentsGroup} class is used to determine what
 * {@link Component}s to be displayed on a diagram. These tests exist to ensure
 * the optimal {@link Component}s are being chosen based on the given code base
 * and desired.
 */
public class RelatedComponentGroupTest {

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        assertTrue(new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components()
                        .contains(codeModel.getComponent("com.sample.ClassB")));
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.size() == 3);
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.size() == 3);
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.size() == 3);
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassB")));
        assertTrue(cmps.size() == 3);
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassB")));
        assertTrue(cmps.size() == 3);
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassD")));
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassB")));
        assertTrue(cmps.size() == 3);
    }

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
        final BinaryClassRelationshipExtractor<?> bCAS = new BinaryClassRelationshipExtractor<Object>();
        final Map<String, BinaryClassRelationship> binaryRelationships = bCAS
                .generateBinaryClassRelationships(codeModel);
        Set<Component> cmps = new RelatedBaseComponentsGroup(codeModel.getComponents(), binaryRelationships,
                codeModel.getComponent("com.sample.ClassA")).components();
        assertTrue(cmps.contains(codeModel.getComponent("com.sample.ClassA")));
        assertTrue(cmps.size() == 1);
    }
}
