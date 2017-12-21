package com.clarity.binary.parse;

import com.clarity.compiler.ClarpseProject;
import com.clarity.compiler.SourceFiles;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class ParsedProject {

    private SourceFiles codebase;

    public ParsedProject(SourceFiles codebase) {
        this.codebase = codebase;
    }

    public OOPSourceCodeModel model() throws Exception {
        return new ClarpseProject(codebase).result();
    }
}
