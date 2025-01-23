package com.hadii.striff.diagram.plantuml;

import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.striff.diagram.ComponentHelper;
import com.hadii.striff.diagram.DiagramComponent;
import com.hadii.striff.diagram.display.DiagramDisplay;
import java.util.Set;
import java.util.stream.Collectors;

final class PUMLPackageCode {

    private final String code;

    PUMLPackageCode(DiagramDisplay diagramDisplay, OOPSourceCodeModel mergedModel, Set<String> addedCmps,
            Set<String> deletedCmps, Set<String> modifiedCmps, Set<DiagramComponent> diagramComponents) {
        this.code = this.generate(diagramDisplay, mergedModel, addedCmps, deletedCmps, modifiedCmps, diagramComponents);
    }

    private String generate(DiagramDisplay diagramDisplay, OOPSourceCodeModel mergedModel, Set<String> addedCmps,
            Set<String> deletedCmps, Set<String> modifiedCmps, Set<DiagramComponent> diagramComponents) {
        StringBuffer stringBuffer = new StringBuffer();
        diagramDisplay.pkgColorMappings().forEach(entry -> {
            Set<DiagramComponent> pkgBaseCmps = diagramComponents.stream()
                    .filter(cmp -> ComponentHelper.packagePath(cmp.pkg()).equals(
                            entry.getKey()) && cmp.componentType().isBaseComponent())
                    .collect(Collectors.toSet());
            if (entry.getKey() == null || entry.getKey().isEmpty()) {
                // PUML namespaces cannot be empty, so we need to use the package keyword...
                stringBuffer.append("package ");
            } else {
                stringBuffer.append("namespace ");
            }
            stringBuffer.append(entry.getKey())
                    .append(" ")
                    .append(entry.getValue())
                    .append(" {\n")
                    .append(new PUMLClassFieldsCode(mergedModel, addedCmps, deletedCmps, modifiedCmps, diagramDisplay)
                            .value(pkgBaseCmps))
                    .append("}\n");
        });
        return stringBuffer.toString();
    }

    public String value() {
        return this.code;
    }
}
