package claritybot.test;

import org.junit.Test;

import com.clarity.binary.diagram.Diagram;
import com.clarity.binary.diagram.view.SDView;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class SDManualTest {

    @Test
    public void structureDiffManualTest() throws Exception {
        OOPSourceCodeModel oldModel = ClarityTestUtil.getGitHubRepoModel("junit-team", "junit5", "master");
        OOPSourceCodeModel newModel = ClarityTestUtil.getGitHubRepoModel("jensim", "junit5",
                "issue-1151-csvfileSource-skip-headers-option");
        Diagram view = new SDView(oldModel, newModel, true).view();
        System.out.println(view.svgText());
    }
}
