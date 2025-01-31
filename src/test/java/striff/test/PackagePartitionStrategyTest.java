package striff.test;

import com.hadii.clarpse.compiler.ClarpseProject;
import com.hadii.clarpse.compiler.Lang;
import com.hadii.clarpse.compiler.ProjectFile;
import com.hadii.clarpse.compiler.ProjectFiles;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.striff.diagram.StriffDiagramModel;
import com.hadii.striff.diagram.DiagramComponent;
import com.hadii.striff.diagram.partition.PackagePartitionStrategy;
import com.hadii.striff.parse.CodeDiff;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests to ensure component relations are being extracted correctly.
 */
public class PackagePartitionStrategyTest {

    final String CLASS_A_CODE ="package a; class ClassA { }\n";
    final String CLASS_B_CODE ="package b; class ClassB { }\n";

    @Test
    public void testPackageBasedPartition() throws Exception {
        final ProjectFile fileA = new ProjectFile("/ClassA.java", CLASS_A_CODE);
        final ProjectFile fileB = new ProjectFile("/ClassB.java", CLASS_B_CODE);
        final ProjectFiles pfs = new ProjectFiles();
        pfs.insertFile(fileA);
        pfs.insertFile(fileB);
        final OOPSourceCodeModel codeModel =
           new ClarpseProject(pfs, Lang.JAVA).result().model();
        List<Set<DiagramComponent>> componentPartitions = new PackagePartitionStrategy(
            new StriffDiagramModel(new CodeDiff(new OOPSourceCodeModel(), codeModel))).apply();
        assertEquals(2, componentPartitions.size());
    }

}
