package claritybot.test;

import org.junit.Test;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.SignedSDDiagram;
import com.clarity.binary.diagram.view.SDView;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class SDManualTest {

    @Test
    public void structureDiffManualTest() throws Exception {
        OOPSourceCodeModel oldModel = ClarityTestUtil.getGitHubRepoModel("zir0-93", "clarpse", "master");
        OOPSourceCodeModel newModel = ClarityTestUtil.getGitHubRepoModel("zir0-93", "clarpse", "golang_support");
        Diagram view = new SignedSDDiagram(new SDView(oldModel, newModel, true, 400).view());
        System.out.println(view.svgText());
    }
}
