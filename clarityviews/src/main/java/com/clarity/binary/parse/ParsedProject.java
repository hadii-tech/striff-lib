package com.clarity.binary.parse;

import java.io.IOException;

import com.clarity.parser.ClarpseProject;
import com.clarity.parser.ParseRequestContent;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class ParsedProject {

    private ParseRequestContent codebase;

    public ParsedProject(ParseRequestContent codebase) throws IOException {
        this.codebase = codebase;
    }

    public OOPSourceCodeModel model() throws Exception {
        return new ClarpseProject(codebase).result();
    }
}
