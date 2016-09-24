package clarityviews.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.clarity.parser.Lang;
import com.clarity.parser.ParseRequestContent;
import com.clarity.parser.RawFile;
import com.clarity.rest.core.component.CodeSnippet;
import com.clarity.rest.core.component.UsageSnippets;
import com.clarity.rest.parse.CodeBase;
import com.clarity.rest.parse.ParsedProject;
import com.clarity.sourcemodel.OOPSourceCodeModel;
/**
 * Tests to ensure code snippet generation is working properly.
 *
 * @author Muntazir Fadhel
 */
public class SnippetGenerationTest {

    private final String MATRIX_COMPONENT_UNIQUE_NAME = "Matrix";
    private final String STRING_FIELD_COMPONENT_UNIQUE_NAME = "java.lang.String";

    private static OOPSourceCodeModel codeModel;

    private static CodeBase files = new CodeBase();

    @BeforeClass
    public static void setup() throws Exception {
        final RawFile file = new RawFile("test.java",
                "package clarityviews.resources;"
                        + "import ConvertTestUtil; import java.io.IOException; import Matrix; import AStaticClass; import MatrixConverter;"
                        + "import org.junit.Test;"
                        + "public class SampleCodeBaseForSnippetTests {"
                        + "   public String classField;"
                        + "   public void manipulateClassField(String test) {"
                        + "      classField = \"\";"
                        + "      String aString;"
                        + "      Matrix m;"
                        + "      AStaticClass.aStaticMethod(test);"
                        + "   }"
                        + "   public void trivialMethod(String foo) {  }"
                        + "   @Test"
                        + "   public Object testConvertMatrix() throws IOException{"
                        + "      final Matrix input = new Matrix(3, 3);"
                        + "      String test;"
                        + "   }"
                        + "}");

        final ParseRequestContent reqCon = new ParseRequestContent(Lang.JAVA);
        file.name("clarityviews/resources/SampleCodeBaseForSnippetTests.java");
        files.insertFile(file);
        reqCon.insertFile(file);
        codeModel = new ParsedProject(reqCon).model();
    }

    @Test
    public void testSnippetFoundForThrownException() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets("java.io.IOException", codeModel.getComponents())
        .snippets();

        Assert.assertTrue(snippets.get(0).getSnippetScore() == 2);
    }

    @Test
    public void testSnippetFoundForMethodReturnValue() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets("java.lang.Object", codeModel.getComponents()).snippets();

        Assert.assertTrue(snippets.get(0).getSnippetScore() == 2);
    }

    @Test
    public void testCorrectNoOfSnippetsGenerated() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets(MATRIX_COMPONENT_UNIQUE_NAME, codeModel.getComponents())
        .snippets();
        Assert.assertTrue("Returned list of snippets should be 2!", snippets.size() == 2);
    }

    @Test
    public void testCorrectSnippetScores() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets(STRING_FIELD_COMPONENT_UNIQUE_NAME, codeModel.getComponents()).snippets();

        Assert.assertTrue(snippets.get(0).getSnippetScore() == 2);
        Assert.assertTrue(snippets.get(1).getSnippetScore() == 5);
        Assert.assertTrue(snippets.get(2).getSnippetScore() == 2);
    }

    @Test
    public void testNoDuplicateSnippets() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets(MATRIX_COMPONENT_UNIQUE_NAME, codeModel.getComponents())
        .snippets();
        Assert.assertTrue("Returned list of snippets should number should be greater than 1!", snippets.size() > 1);
        for (int i = 0; i < snippets.size(); i++) {
            for (int j = 0; j < snippets.size(); j++) {
                if (i != j) {
                    Assert.assertTrue("Duplicate snippet found!",
                            !snippets.get(i).getCode().equals(snippets.get(j).getCode()));
                }
            }
        }
    }

    @Test
    public void testEnsureTestRelatedSnippetsArePrioritized() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets(MATRIX_COMPONENT_UNIQUE_NAME, codeModel.getComponents())
        .snippets();
        Assert.assertTrue("First snippet found should be test related!", snippets.get(0).getCode().contains("@Test"));
        Assert.assertTrue("Are all methods tests related!?",
                !snippets.get(snippets.size() - 1).getCode().contains("@Test"));
    }

    @Test
    public void testKeyVarsAreHighlighted() throws Exception {

        final List<CodeSnippet> snippets = new UsageSnippets(STRING_FIELD_COMPONENT_UNIQUE_NAME, codeModel.getComponents())
        .snippets();
        Assert.assertTrue("No generated snippets for the String class field!", !snippets.isEmpty());
        Assert.assertTrue("First snippet should contain key variable: aString!", snippets.get(0).getSnippetMainVars()
                .contains("aString"));
        Assert.assertTrue("First snippet should contain key variable: test!", snippets.get(0).getSnippetMainVars()
                .contains("test"));
        Assert.assertTrue("First snippet should contain key variable: classField!", snippets.get(0)
                .getSnippetMainVars().contains("classField"));
        Assert.assertTrue("First snippet should contain key variable: String!", snippets.get(0).getSnippetMainVars()
                .contains("String"));
        Assert.assertTrue("First snippet should contain key variable: foo!", snippets.get(0).getSnippetMainVars()
                .contains("foo"));
    }
}
