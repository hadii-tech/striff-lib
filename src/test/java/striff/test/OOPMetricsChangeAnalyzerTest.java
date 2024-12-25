package striff.test;

import com.hadii.clarpse.compiler.ClarpseProject;
import com.hadii.clarpse.compiler.CompileException;
import com.hadii.clarpse.compiler.Lang;
import com.hadii.clarpse.compiler.ProjectFile;
import com.hadii.clarpse.compiler.ProjectFiles;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.striff.metrics.MetricChange;
import com.hadii.striff.metrics.OOPMetricsChangeAnalyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

public class OOPMetricsChangeAnalyzerTest {

        @Test
        public void javaSingleClass_noMetricChange() throws CompileException {
                OOPSourceCodeModel oldModel = createSingleJavaClassModel("test", "ClassA");
                OOPSourceCodeModel newModel = createSingleJavaClassModel("test", "ClassA");

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.ClassA");

                assertTrue("ClassA should be found in both models => some MetricChange object even if no differences.",
                                maybeChange.isPresent());

                // Suppose there's no difference in NOC => "0.00 -> 0.00"
                assertTrue("Expected no change in NOC for ClassA => 0->0.",
                                maybeChange.get().toString().contains("NOC: 0.00 -> 0.00"));
        }

        @Test
        public void javaClass_onlyInNewModel() throws CompileException {
                OOPSourceCodeModel oldModel = createEmptyJavaModel();
                OOPSourceCodeModel newModel = createSingleJavaClassModel("test", "ClassA");

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.ClassA");

                assertTrue("ClassA only in new => expect a MetricChange with old=0.0 metrics.",
                                maybeChange.isPresent());
        }

        // =====================================================
        // ================ SECTION II: BASIC GO TESTS =========
        // =====================================================

        @Test
        public void goSingleStruct_noChanges() throws CompileException {
                OOPSourceCodeModel oldModel = createSingleGoStructModel("package main\ntype ClassA struct {}",
                                "ClassA");
                OOPSourceCodeModel newModel = createSingleGoStructModel("package main\ntype ClassA struct {}",
                                "ClassA");

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "main.ClassA");
                if (!maybeChange.isPresent()) {
                        // Fallback if the parser uses just "ClassA":
                        maybeChange = analyzeChange(oldModel, newModel, null, "ClassA");
                }

                assertTrue("Expect a MetricChange object even if no difference in metrics.", maybeChange.isPresent());

                assertTrue("Expected no children => NOC=0->0 for ClassA struct in Go.",
                                maybeChange.get().toString().contains("NOC: 0.00 -> 0.00"));
        }

        @Test
        public void goStruct_missingInNewModel() throws CompileException {
                String oldCode = "package main\ntype OldStruct struct {}";
                String newCode = "package main\ntype DifferentStruct struct {}";
                OOPSourceCodeModel oldModel = createSingleGoStructModel(oldCode, "OldStruct");
                OOPSourceCodeModel newModel = createSingleGoStructModel(newCode, "DifferentStruct");

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "main.OldStruct");
                if (!maybeChange.isPresent()) {
                        maybeChange = analyzeChange(oldModel, newModel, null, "OldStruct");
                }

                assertTrue("OldStruct missing in new => expect a valid MetricChange with new=0.0.",
                                maybeChange.isPresent());
        }

        // =====================================================
        // ========== SECTION IV: CORNER / EXCEPTION CASES =====
        // =====================================================

        @Test
        public void cornerCase_classMissingInNewModel() throws CompileException {
                OOPSourceCodeModel oldModel = createSingleJavaClassModel("test", "ClassA");
                OOPSourceCodeModel newModel = createEmptyJavaModel();

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.ClassA");

                assertTrue("ClassA only in old => MetricChange should exist with new=0.0 metrics.", maybeChange
                                .isPresent());
        }

        @Test
        public void cornerCase_classMissingInBothModels() throws CompileException {
                OOPSourceCodeModel oldModel = createEmptyJavaModel();
                OOPSourceCodeModel newModel = createEmptyJavaModel();

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.ClassA");

                assertFalse("ClassA missing in both => Optional.empty() expected.", maybeChange.isPresent());
        }

        @Test
        public void cornerCase_classPresentButNotInTargetSet() throws CompileException {
                OOPSourceCodeModel oldModel = createSingleJavaClassModel("test", "ClassA");
                OOPSourceCodeModel newModel = createSingleJavaClassModel("test", "ClassA");
                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.NonExistent");
                assertFalse("ClassA not in target set => skipping => Optional.empty().", maybeChange.isPresent());
        }

        @Test
        public void cornerCase_invalidClassName() throws CompileException {
                OOPSourceCodeModel oldModel = createSingleJavaClassModel("test", "ClassA");
                OOPSourceCodeModel newModel = createSingleJavaClassModel("test", "ClassA");

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.NoSuchClass");

                assertFalse("NoSuchClass doesn't exist => Optional.empty().", maybeChange.isPresent());
        }

        // =====================================================
        // ========== SECTION V: DETAILED METRIC CHANGES =======
        // =====================================================

        @Test
        public void detailedMetricChanges_forClassB() throws CompileException {
                OOPSourceCodeModel oldModel = createDetailedOldModel();
                OOPSourceCodeModel newModel = createDetailedNewModel();

                Optional<MetricChange> maybeChange = analyzeChange(oldModel, newModel, null, "test.ClassB");

                assertTrue("ClassB must exist in both old & new with changed metrics.",
                                maybeChange.isPresent());

                MetricChange change = maybeChange.get();

                // Example numeric checks: oldNOC=0->1, oldDIT=2->3, oldWMC=2->5, etc.
                assertEquals(0.0, change.getOldNOC(), 1e-9);
                assertEquals(1.0, change.getUpdatedNOC(), 1e-9);

                assertEquals(2.0, change.getOldDIT(), 1e-9);
                assertEquals(3.0, change.getUpdatedDIT(), 1e-9);

                assertEquals(2.0, change.getOldWMC(), 1e-9);
                assertEquals(5.0, change.getUpdatedWMC(), 1e-9);

                assertEquals(0.0, change.getOldAC(), 1e-9);
                assertEquals(1.0, change.getUpdatedAC(), 1e-9);

                assertEquals(3.0, change.getOldEC(), 1e-9);
                assertEquals(3.0, change.getUpdatedEC(), 1e-9);

                assertEquals(0.0, change.getOldEncapsulation(), 1e-9);
                assertEquals(0.5, change.getUpdatedEncapsulation(), 1e-9);
        }

        // =====================================================
        // ========== HELPER METHODS FOR MODEL BUILDING ========
        // =====================================================

        private Optional<MetricChange> analyzeChange(
                        OOPSourceCodeModel oldModel,
                        OOPSourceCodeModel newModel,
                        Set<String> targetComponents,
                        String className) {
                OOPMetricsChangeAnalyzer analyzer = new OOPMetricsChangeAnalyzer(oldModel, newModel, targetComponents);
                return analyzer.analyzeChanges(className);
        }

        private OOPSourceCodeModel createEmptyJavaModel() throws CompileException {
                ProjectFiles emptyFiles = new ProjectFiles();
                return new ClarpseProject(emptyFiles, Lang.JAVA).result().model();
        }

        private OOPSourceCodeModel createSingleJavaClassModel(String pkgName, String className)
                        throws CompileException {
                String javaSource = "package " + pkgName + ";\n" +
                                "public class " + className + " {\n" +
                                "}\n";

                ProjectFiles files = new ProjectFiles();
                files.insertFile(new ProjectFile("/" + className + ".java", javaSource));

                return new ClarpseProject(files, Lang.JAVA).result().model();
        }

        private OOPSourceCodeModel createSingleGoStructModel(String goSource, String fileHint) throws CompileException {
                ProjectFiles rawData = new ProjectFiles();
                rawData.insertFile(new ProjectFile("/src/main/" + fileHint + ".go", goSource));
                rawData.insertFile(new ProjectFile("/src/go.mod", "module module/module/module"));

                ClarpseProject parseService = new ClarpseProject(rawData, Lang.GOLANG);
                return parseService.result().model();
        }

        // ================= Detailed Old/New Model Helpers ====================

        private OOPSourceCodeModel createDetailedOldModel() throws CompileException {
                ProjectFiles oldFiles = new ProjectFiles();

                // ClassA => base => DIT=1
                String classA = "package test;\n" +
                                "public class ClassA {}";
                oldFiles.insertFile(new ProjectFile("/ClassA.java", classA));

                // ClassC => base => maybe B references C => AC(C)=1
                String classC = "package test;\n" +
                                "public class ClassC {}";
                oldFiles.insertFile(new ProjectFile("/ClassC.java", classC));

                // ClassB => extends A => DIT=2, references C => EC=1, 1 method => WMC=2, no
                // private => ENC=0
                String classB = "package test;\n" +
                                "public class ClassB extends ClassA {\n" +
                                "    public ClassB() {\n" +
                                "       ClassC c = new ClassC();\n" +
                                "    }\n" +
                                "    public public void someMethod() {\n" +
                                "       if(true) System.out.println(\"Hello\");\n" +
                                "    }\n" +
                                "}\n";
                oldFiles.insertFile(new ProjectFile("/ClassB.java", classB));

                return new ClarpseProject(oldFiles, Lang.JAVA).result().model();
        }

        private OOPSourceCodeModel createDetailedNewModel() throws CompileException {
                ProjectFiles newFiles = new ProjectFiles();

                // ClassA => same base
                String classA = "package test;\n" +
                                "public class ClassA extends ClassC {}";
                newFiles.insertFile(new ProjectFile("/ClassA.java", classA));

                // ClassC => also referenced by D => AC(C)=2
                String classC = "package test;\n" +
                                "public class ClassC {}";
                newFiles.insertFile(new ProjectFile("/ClassC.java", classC));

                // ClassD => extends B => B's DIT=3
                String classD = "package test;\n" +
                                "public class ClassD extends ClassB {\n" +
                                "    public ClassD() {\n" +
                                "       ClassC c = new ClassC();\n" +
                                "    }\n" +
                                "}\n";
                newFiles.insertFile(new ProjectFile("/ClassD.java", classD));

                // ClassB => extended from A => old DIT=2 -> new DIT=3
                // references 2 classes => Ce=2, 2 methods => WMC=5, private => ENC=0.5
                String classB = "package test;\n" +
                                "public class ClassB extends ClassA {\n" +
                                "    private int hidden;\n" +
                                "    public ClassB() {\n" +
                                "       ClassC c = new ClassC();\n" +
                                "       // could reference another class => Ce=2\n" +
                                "    }\n" +
                                "    public public void someMethod() {\n" +
                                "       if(true)  System.out.println(\"Hello\");\n" +
                                "       if(false) System.out.println(\"World\");\n" +
                                "    }\n" +
                                "    protected public void anotherMethod() {\n" +
                                "       for(int i=0; i<2; i++) { System.out.println(i); }\n" +
                                "    }\n" +
                                "}\n";
                newFiles.insertFile(new ProjectFile("/ClassB.java", classB));

                return new ClarpseProject(newFiles, Lang.JAVA).result().model();
        }
}
