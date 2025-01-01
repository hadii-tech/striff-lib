package com.hadii.striff.diagram;

import com.hadii.striff.diagram.display.DiagramDisplay;
import com.hadii.striff.diagram.plantuml.PUMLDiagram;
import com.hadii.striff.diagram.plantuml.PUMLDrawException;
import com.hadii.striff.extractor.ComponentRelation;
import com.hadii.striff.parse.CodeDiff;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Striff diagram.
 */
public class StriffDiagram {

    private final Set<String> containedPkgs = new HashSet<>();
    private final String svgCode;
    private final Set<DiagramComponent> cmps;
    private Set<ComponentRelation> relations;

    public StriffDiagram(CodeDiff codeDiff, Set<DiagramComponent> diagramComponentSet,
            DiagramDisplay diagramDisplay, Set<ComponentRelation> relations)
            throws IOException, PUMLDrawException {
        this.cmps = diagramComponentSet;
        this.cmps.forEach(cmp -> this.containedPkgs.add(ComponentHelper.packagePath(cmp.pkg())));
        this.svgCode = new PUMLDiagram(codeDiff, diagramComponentSet, diagramDisplay).svgText();
        this.relations = relations;
    }

    public String svg() {
        return this.svgCode;
    }

    public int size() {
        return this.cmps.size();
    }

    public Set<String> containedPkgs() {
        return this.containedPkgs;
    }

    public Set<DiagramComponent> cmps() {
        return this.cmps;
    }

    public Set<ComponentRelation> relations() {
        return this.relations;
    }
}
