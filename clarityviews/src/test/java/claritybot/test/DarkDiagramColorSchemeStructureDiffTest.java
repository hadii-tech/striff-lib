package claritybot.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.scheme.DarkDiagramColorScheme;
import com.clarity.binary.diagram.view.SDView;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class DarkDiagramColorSchemeStructureDiffTest {

    @Test
    public void DarkThemedSDClassLabelsAreGreenTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains("<text class=\"interactiveComponent\" fill=\"#22df80\""));
    }

    @Test
    public void DarkThemedSDClassLabelsUseConsolasTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText()
                .contains("<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas,Arial\""));
    }

    @Test
    public void DarkThemedSDClassLabelsUse16FontSizeTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas,Arial\" font-size=\"18\""));
    }

    @Test
    public void DarkThemedSDUseGeometricPrecisionRendering() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("text-rendering=\"geometricPrecision\""));
    }

    @Test
    public void DarkThemedSDUseGrayArrows() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("style=\"stroke: #C5C8C6; stroke-width: 2.0;\""));
    }

    @Test
    public void DarkThemedSDContainsHTMLTagsStrippedTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("id=\"codebaseA.HtmlTagsStrippedText\""));
    }

    @Test
    public void DarkThemedSDContainsJavaDocSymbolStrippedTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("id=\"codebaseA.JavaDocSymbolStrippedText\""));
    }

    @Test
    public void DarkThemedSDContainsDefaultTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.DefaultText\""));
    }

    @Test
    public void DarkThemedSDContainsDefaultTextUserElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.DefaultTextUser\""));
    }

    @Test
    public void DarkThemedSDContainsTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.Text\""));
    }

    @Test
    public void DarkThemedSDContainsNewTextClassElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.NewTextClass\""));
    }

    //

    @Test
    public void DarkThemedSDHTMLTagsStrippedTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas,Arial\" font-size=\"18\" id=\"codebaseA.HtmlTagsStrippedText\""));
    }

    @Test
    public void DarkThemedSDJavaDocSymbolStrippedTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas,Arial\" font-size=\"18\" id=\"codebaseA.JavaDocSymbolStrippedText\""));
    }

    @Test
    public void DarkThemedSDDefaultTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas,Arial\" font-size=\"18\" id=\"codebaseA.DefaultText\""));
    }

    @Test
    public void DarkThemedSDDefaultTextUserElementIsRed() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#F97D7D\" font-family=\"Consolas,Arial\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.DefaultTextUser\""));
    }

    @Test
    public void DarkThemedSDTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas,Arial\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.Text\""));
    }

    @Test
    public void DarkThemedSDNewTextClassElementIsGreen() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#22df80\" font-family=\"Consolas,Arial\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.NewTextClass\""));
    }

    @Test
    public void DarkThemedSDNewMethodElementIsGreen() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#22df80\" font-family=\"Consolas,Arial\" font-size=\"16\" id=\"codebaseA.HtmlTagsStrippedText.newMethod()\""));
    }

    @Test
    public void DarkThemedSDTestFieldVarIsRed() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(new DarkDiagramColorScheme(), modelA, modelB, true, 75).view();
        assertTrue(view.svgText().contains(
                "fill=\"#F97D7D\" font-family=\"Consolas,Arial\" font-size=\"16\" id=\"codebaseA.JavaDocSymbolStrippedText.text\""));
    }
}
