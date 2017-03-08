package clarityviews.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.view.DefaultClarityView;
import com.clarity.binary.extractor.ClassRelationshipsExtractor;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class ClarityViewTest {

    @Test
    public void ClarityViewDiagramClassLabelsAreGreenTest() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("<text class=\"interactiveComponent\" fill=\"#22df80\""));
    }

    @Test
    public void ClarityViewDiagramClassLabelsUseConsolasTest() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText()
                .contains("<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas\""));
    }

    @Test
    public void ClarityViewDiagramClassLabelsUse16FontSizeTest() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains(
                "<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas\" font-size=\"18\""));
    }

    @Test
    public void ClarityViewDiagramUseGeometricPrecisionRendering() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("text-rendering=\"geometricPrecision\""));
        System.out.println(view.svgText());
    }

    @Test
    public void ClarityViewDiagramUseGrayArrows() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("style=\"stroke: #C5C8C6; stroke-width: 3.0;\""));
        System.out.println(view.svgText());
    }

    @Test
    public void ClarityViewDiagramContainsHTMLTagsStrippedTextElement() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.HtmlTagsStrippedText\""));
    }

    @Test
    public void ClarityViewDiagramContainsJavaDocSymbolStrippedTextElement() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.JavaDocSymbolStrippedText\""));
    }

    @Test
    public void ClarityViewDiagramContainsDefaultTextElement() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.DefaultText\""));
    }

    @Test
    public void ClarityViewDiagramContainsDefaultTextUserElement() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.DefaultTextUser\""));
    }

    @Test
    public void ClarityViewDiagramContainsTextElement() throws Exception {
        OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("id=\"codebaseA.Text\""));
    }
    
    @Test
    public void keyClassIsInBlueColor() throws Exception {
    	OOPSourceCodeModel model = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        Diagram view = new DefaultClarityView(
                new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
                model.getComponent("codebaseA.Text")).view();
        assertTrue(view.svgText().contains("fill=\"#59cde2\" font-family=\"Consolas\" font-size=\"18\" font-style=\"italic\" id=\"codebaseA.Text\""));
    	
    }
}
