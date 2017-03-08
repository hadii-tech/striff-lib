package clarityviews.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.view.StructureDiffView;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class StructureDiffTest {

    @Test
    public void ClarityViewDiagramClassLabelsAreGreenTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains("<text class=\"interactiveComponent\" fill=\"#22df80\""));
    }

    @Test
    public void ClarityViewDiagramClassLabelsUseConsolasTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText()
                .contains("<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas\""));
    }

    @Test
    public void ClarityViewDiagramClassLabelsUse16FontSizeTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas\" font-size=\"18\""));
    }

    @Test
    public void ClarityViewDiagramUseGeometricPrecisionRendering() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("text-rendering=\"geometricPrecision\""));
    }

    @Test
    public void ClarityViewDiagramUseGrayArrows() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("style=\"stroke: #C5C8C6; stroke-width: 3.0;\""));
    }

    @Test
    public void ClarityViewDiagramContainsHTMLTagsStrippedTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("id=\"codebaseA.HtmlTagsStrippedText\""));
    }

    @Test
    public void ClarityViewDiagramContainsJavaDocSymbolStrippedTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        System.out.println(view.svgText());
        assertTrue(view.svgText().contains("id=\"codebaseA.JavaDocSymbolStrippedText\""));
    }

    @Test
    public void ClarityViewDiagramContainsDefaultTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.DefaultText\""));
    }

    @Test
    public void ClarityViewDiagramContainsDefaultTextUserElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.DefaultTextUser\""));
    }

    @Test
    public void ClarityViewDiagramContainsTextElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.Text\""));
    }

    @Test
    public void ClarityViewDiagramContainsNewTextClassElement() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.NewTextClass\""));
    }

    //

    @Test
    public void ClarityViewDiagramHTMLTagsStrippedTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas\" font-size=\"18\" id=\"codebaseA.HtmlTagsStrippedText\""));
    }

    @Test
    public void ClarityViewDiagramJavaDocSymbolStrippedTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas\" font-size=\"18\" id=\"codebaseA.JavaDocSymbolStrippedText\""));
    }

    @Test
    public void ClarityViewDiagramDefaultTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText()
                .contains("fill=\"#C5C8C6\" font-family=\"Consolas\" font-size=\"18\" id=\"codebaseA.DefaultText\""));
    }

    @Test
    public void ClarityViewDiagramDefaultTextUserElementIsRed() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#F97D7D\" font-family=\"Consolas\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.DefaultTextUser\""));
    }

    @Test
    public void ClarityViewDiagramTextElementIsGray() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#C5C8C6\" font-family=\"Consolas\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.Text\""));
    }

    @Test
    public void ClarityViewDiagramNewTextClassElementIsGreen() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#22df80\" font-family=\"Consolas\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.NewTextClass\""));
    }

    @Test
    public void ClarityViewDiagramNewMethodElementIsGreen() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#22df80\" font-family=\"Consolas\" font-size=\"16\" id=\"codebaseA.HtmlTagsStrippedText.newMethod()\""));
    }

    @Test
    public void ClarityViewDiagramTestFieldVarIsRed() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new StructureDiffView(modelA, modelB, true).view();
        assertTrue(view.svgText().contains(
                "fill=\"#F97D7D\" font-family=\"Consolas\" font-size=\"16\" id=\"codebaseA.JavaDocSymbolStrippedText.text\""));
    }
}
