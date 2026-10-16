package striff.test.model;

import com.hadi.clarpse.compiler.ClarpseProject;
import com.hadi.clarpse.compiler.CompileResult;
import com.hadi.clarpse.compiler.Lang;
import com.hadi.clarpse.compiler.ProjectFile;
import com.hadi.clarpse.compiler.ProjectFiles;
import com.hadi.clarpse.compiler.typescript.NodeRuntime;
import com.hadi.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadi.clarpse.sourcemodel.OOPSourceModelConstants;
import com.hadi.striff.StriffConfig;
import com.hadi.striff.StriffOperation;
import com.hadi.striff.diagram.StriffDiagram;
import com.hadi.striff.extractor.ExtractedRelationships;
import com.hadi.striff.extractor.RelationsMap;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TypeScript type aliases are in the parsed model but draw nothing: no box for an alias or for a
 * module declaring only aliases, and no relation from or to one. A diagram of code written with
 * aliases is what it was before aliases were modelled, and a change adding an alias alone draws
 * no module.
 */
public class TypeScriptModuleDeclarationsRelationsTest {

    private static final String TS_CONFIG = "{\n"
            + "  \"compilerOptions\": {\n"
            + "    \"target\": \"ES2020\",\n"
            + "    \"module\": \"NodeNext\",\n"
            + "    \"moduleResolution\": \"NodeNext\",\n"
            + "    \"strict\": true,\n"
            + "    \"skipLibCheck\": true\n"
            + "  },\n"
            + "  \"include\": [\"src/**/*.ts\"]\n"
            + "}";

    private OOPSourceCodeModel model;
    private RelationsMap relations;

    @Before
    public void parse() throws Exception {
        Assume.assumeTrue(NodeRuntime.isNodeAvailable());
        ProjectFiles files = new ProjectFiles();
        files.insertFile(new ProjectFile("/src/model.ts", "export class Order { id: number = 1; }\n"));
        files.insertFile(new ProjectFile("/src/types.ts",
                "import { Order } from \"./model\";\n"
                        + "export type OrderList = Order[];\n"
                        + "export type Mode = \"fast\" | \"slow\";\n"));
        files.insertFile(new ProjectFile("/src/consumer.ts",
                "import { OrderList, Mode } from \"./types\";\n"
                        + "export class Consumer { orders: OrderList = []; mode: Mode = \"fast\"; }\n"));
        files.insertFile(new ProjectFile("/src/helpers.ts",
                "import { Mode } from \"./types\";\n"
                        + "export const pick = (mode: Mode): Mode => mode;\n"
                        + "export function fallback(): Mode { return \"slow\"; }\n"));
        writeTsConfig(files);
        CompileResult result = new ClarpseProject(files, Lang.TYPESCRIPT).result();
        Assert.assertTrue("Compile failures: " + result.failures(), result.failures().isEmpty());
        model = result.model();
        relations = new ExtractedRelationships(model).result();
    }

    @Test
    public void aTypeAliasIsModelled() {
        Assert.assertEquals(OOPSourceModelConstants.ComponentType.TYPE_ALIAS,
                model.copyOfComponent("src.types.OrderList").orElseThrow().componentType());
    }

    @Test
    public void aModuleDeclaringOnlyTypeAliasesDrawsNoBoxAndNoRelation() {
        Assert.assertFalse("an alias-only module relates nothing", relations.hasRels("src.module:types"));
        Assert.assertTrue("nothing relates to an alias or its module: " + allRelations(),
                allRelations().stream().noneMatch(rel -> rel.contains("types")));
    }

    @Test
    public void aReferenceWrittenThroughAnAliasDrawsNoRelationToTheAlias() {
        Assert.assertFalse("a class referencing only aliases relates to nothing: " + allRelations(),
                relations.hasRels("src.consumer.Consumer")
                        && relations.significantRels("src.consumer.Consumer").stream()
                        .anyMatch(rel -> rel.targetComponent().uniqueName().contains("types")));
        Assert.assertTrue("a module of functions using only aliases relates to nothing: " + allRelations(),
                allRelations().stream().noneMatch(rel -> rel.startsWith("src.module:helpers")));
    }

    /**
     * A change that only adds a type alias to a module of functions draws no box for that module:
     * the alias draws nothing, so its arrival makes nothing relevant to the diagram.
     */
    @Test
    public void aChangeAddingOnlyATypeAliasDrawsNoModule() throws Exception {
        String picks = "export const pick = (mode: string): string => mode;\n";
        ProjectFiles before = moduleFiles(picks);
        ProjectFiles after = moduleFiles(picks + "export type Picked = string;\n");
        StriffConfig config = new StriffConfig().setLanguages(List.of(Lang.TYPESCRIPT))
                .setFilesFilter(List.of("/src/picks.ts")).setAnalysisDepth(1);
        for (StriffDiagram diagram : new StriffOperation(before, after, config).result().diagrams()) {
            Assert.assertFalse(diagram.pumlSource(), diagram.pumlSource().contains("module-picks"));
        }
    }

    private static ProjectFiles moduleFiles(final String picks) throws Exception {
        ProjectFiles files = new ProjectFiles();
        files.insertFile(new ProjectFile("/src/picks.ts", picks));
        writeTsConfig(files);
        return files;
    }

    private List<String> allRelations() {
        return relations.relMap().entrySet().stream()
                .flatMap(entry -> entry.getValue().values().stream()
                        .flatMap(java.util.Set::stream)
                        .map(rel -> entry.getKey() + " -> " + rel.targetComponent().uniqueName()))
                .collect(Collectors.toList());
    }

    private static void writeTsConfig(final ProjectFiles files) throws Exception {
        Path repoRoot = Paths.get(files.projectDir());
        Files.createDirectories(repoRoot);
        Files.writeString(repoRoot.resolve("tsconfig.json"), TS_CONFIG, StandardCharsets.UTF_8);
    }
}
