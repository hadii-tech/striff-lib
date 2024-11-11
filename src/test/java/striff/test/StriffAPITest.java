package striff.test;

import com.hadii.clarpse.compiler.Lang;
import com.hadii.clarpse.compiler.ProjectFiles;
import com.hadii.striff.StriffConfig;
import com.hadii.striff.StriffOperation;
import com.hadii.striff.diagram.StriffDiagram;
import com.hadii.striff.diagram.display.OutputMode;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static striff.test.TestUtil.githubProjectFiles;
import static striff.test.TestUtil.writeStriffsToDisk;

public class StriffAPITest {

        /**
         * The following test demonstrates how to generate striff diagrams for two
         * versions of a
         * code base located locally on disk. First replace the `ProjectFiles` paths
         * with valid ones
         * pointing to the two versions of your source code, then remove the @Ignore
         * annotation and
         * run the test. Striff diagrams representing the architectural difference
         * between the two
         * code bases will be outputted as SVG diagrams in the /tmp directory.
         */
        @Ignore
        @Test
        public void testDemonstrateStriffAPI() throws Exception {
                // Note, a ProjectFiles instance can be instantiated with a path to a dir, zip
                // file, or ZipInputStream representing your source code.
                ProjectFiles originalCode = new ProjectFiles("/path/to/original/code");
                ProjectFiles modifiedCode = new ProjectFiles("/path/to/modified/code");
                List<StriffDiagram> striffs = new StriffOperation(
                                originalCode, modifiedCode, new StriffConfig()).result().diagrams();
                System.out.println("Total diagrams generated: " + striffs.size());
                writeStriffsToDisk(striffs, "sample-striffs");
        }

        /**
         * We can also generate striffs based on a Pull Request in GitHub. Ensure
         * the source code refs exist and are still available before running.
         */
        @Test
        public void testDemonstrateStriffAPIWithPR() throws Exception {
                String baseRepoOwner = "hadii-tech";
                String repoName = "clarpse";
                Lang language = Lang.JAVA;
                ProjectFiles oldFiles = githubProjectFiles(
                                baseRepoOwner, repoName, "5e89e78c6588c35170c3e2fc2002e044f4032f9c", language);
                ProjectFiles newFiles = githubProjectFiles(
                                baseRepoOwner, repoName, "314b5959303d8b6027a9c90458ad205b8e6f2ee3", language);
                List<StriffDiagram> striffs = new StriffOperation(
                                oldFiles, newFiles, new StriffConfig()).result().diagrams();
                System.out.println("Total diagrams generated: " + striffs.size());
                writeStriffsToDisk(striffs, "clarpse");
        }
}
