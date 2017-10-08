package claritybot.test;

import org.junit.Test;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.view.SDView;
import com.clarity.binary.parse.ParsedProject;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class SDManualTest {

    @Test
    public void structureDiffManualTest() throws Exception {
        OOPSourceCodeModel modelA = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseA/")).model();
        OOPSourceCodeModel modelB = new ParsedProject(
                ClarityTestUtil.parseRequestContentObjFromResourceDir("/codebaseB/")).model();
        Diagram view = new SDView(modelA, modelB, true).view();
        System.out.println(view.svgText());
    }
}
