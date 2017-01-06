package clarityviews.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.clarity.binary.diagram.DefaultClarityView;
import com.clarity.binary.diagram.DiffClarityView;
import com.clarity.binary.extractor.ClassRelationshipsExtractor;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class DiffClarityViewTest {

	@Test
	public void ClarityViewDiagramDimensionsTest() throws Exception {
		OOPSourceCodeModel oldModel = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
		OOPSourceCodeModel newModel = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
		String view = new DiffClarityView(
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(oldModel),
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(newModel), oldModel,
				newModel, true).view();
		System.out.println(view);
		assertTrue(view.contains("<svg height=\"367px\" style=\"width:1330px;"));
	}

	@Test
	public void ClarityViewDiagramClassLabelsAreGreenTest() throws Exception {
		OOPSourceCodeModel model = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/basic/test/")).model();
		String view = new DefaultClarityView(
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
				model.getComponent("basic.test.Text"), true).view();
		assertTrue(view.contains("<text class=\"interactiveComponent\" fill=\"#22df80\""));
	}

	@Test
	public void ClarityViewDiagramClassLabelsUseConsolasTest() throws Exception {
		OOPSourceCodeModel model = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/basic/test/")).model();
		String view = new DefaultClarityView(
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
				model.getComponent("basic.test.Text"), true).view();
		assertTrue(view.contains("<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas\""));
	}

	@Test
	public void ClarityViewDiagramClassLabelsUse16FontSizeTest() throws Exception {
		OOPSourceCodeModel model = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/basic/test/")).model();
		String view = new DefaultClarityView(
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
				model.getComponent("basic.test.Text"), true).view();
		assertTrue(view.contains(
				"<text class=\"interactiveComponent\" fill=\"#22df80\" font-family=\"Consolas\" font-size=\"16\""));
	}

	@Test
	public void ClarityViewDiagramUseGeometricPrecisionRendering() throws Exception {
		OOPSourceCodeModel model = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/basic/test/")).model();
		String view = new DefaultClarityView(
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
				model.getComponent("basic.test.Text"), true).view();
		assertTrue(view.contains("text-rendering=\"geometricPrecision\""));
		System.out.println(view);
	}

	@Test
	public void ClarityViewDiagramUseGrayArrows() throws Exception {
		OOPSourceCodeModel model = new ParsedProject(
				ClarityTestUtil.parseRequestContentObjFromResourceDir("/basic/test/")).model();
		String view = new DefaultClarityView(
				new ClassRelationshipsExtractor<Object>().generateBinaryClassRelationships(model), model,
				model.getComponent("basic.test.Text"), true).view();
		assertTrue(view.contains("style=\"stroke: #C5C8C6; stroke-width: 2.0;\""));
		System.out.println(view);
	}
}
